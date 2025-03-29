package com.qa.manager.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.qa.common.constant.SystemConstant;
import com.qa.common.constant.enums.StatusCode;
import com.qa.common.enums.EnvEnum;
import com.qa.common.exception.ServiceException;
import com.qa.common.utils.StringUtils;
import com.qa.manager.util.TreeUtil;
import com.qa.manager.domain.Dto.MergeCaseDto;
import com.qa.manager.domain.Dto.PickCaseDto;
import com.qa.manager.domain.Dto.RecordWsDto;
import com.qa.manager.domain.TestCase;
import com.qa.manager.domain.request.record.*;
import com.qa.manager.domain.ximd.IntCount;
import com.qa.manager.mapper.TestCaseMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.ExecRecordMapper;
import com.qa.manager.service.IExecRecordService;
import org.springframework.transaction.annotation.Transactional;

import static com.qa.common.utils.TimeUtil.compareToOriginalDate;

/**
 * 用例执行记录Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-23
 */
@Service
public class ExecRecordServiceImpl implements IExecRecordService
{

    private static final String OE_PICK_ALL = "\"priority\":[\"0\"]";

    private static final Integer DEFAULT_ENV = 0;


    @Autowired
    private ExecRecordMapper execRecordMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    /**
     * 查询用例执行记录
     * 
     * @param id 用例执行记录主键
     * @return 用例执行记录
     */
    @Override
    public ExecRecord selectExecRecordById(Long id)
    {
        return execRecordMapper.selectExecRecordById(id);
    }

    /**
     * 查询用例执行记录列表
     * 
     * @param execRecord 用例执行记录
     * @return 用例执行记录
     */
    @Override
    public List<ExecRecord> selectExecRecordList(ExecRecord execRecord)
    {
        return execRecordMapper.selectExecRecordList(execRecord);
    }

    /**
     * 新增用例执行记录
     * 
     * @param execRecord 用例执行记录
     * @return 结果
     */
    @Override
    public int insertExecRecord(ExecRecord execRecord)
    {
        return execRecordMapper.insertExecRecord(execRecord);
    }

    /**
     * 修改用例执行记录
     * 
     * @param execRecord 用例执行记录
     * @return 结果
     */
    @Override
    public int updateExecRecord(ExecRecord execRecord)
    {
        return execRecordMapper.updateExecRecord(execRecord);
    }

    /**
     * 批量删除用例执行记录
     * 
     * @param ids 需要删除的用例执行记录主键
     * @return 结果
     */
    @Override
    public int deleteExecRecordByIds(Long[] ids)
    {
        return execRecordMapper.deleteExecRecordByIds(ids);
    }

    /**
     * 删除用例执行记录信息
     * 
     * @param id 用例执行记录主键
     * @return 结果
     */
    @Override
    public int deleteExecRecordById(Long id)
    {
        return execRecordMapper.deleteExecRecordById(id);
    }



    @Override
    public List<RecordListResp> getListByCaseId(Long caseId) {
        List<RecordListResp> res = new ArrayList<>();
        TestCase testCase = testCaseMapper.selectOne(caseId);
        if (testCase == null) {
            throw new ServiceException("用例不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }

        List<ExecRecord> execRecordList = execRecordMapper.getRecordListByCaseId(caseId);
        for (ExecRecord record : execRecordList) {
            res.add(buildList(record));
        }
        return res;
    }

    @Override
    public RecordGeneralInfoResp getGeneralInfo(Long recordId) {
        ExecRecord record = execRecordMapper.selectOne(recordId);
        if (record == null) {
            throw new ServiceException("操作记录不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }

        TestCase testCase = testCaseMapper.selectOne(record.getCaseId());
        JSONObject merged = getData(new MergeCaseDto(testCase.getId(), record.getChooseContent(), record.getCaseContent(), record.getEnv(), 0L));

        // 开始构建响应体
        return buildGeneralInfoResp(record, testCase, merged);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addRecord(RecordAddReq req) {
        // 根据caseContent和recordContent进行一次节点修剪合并
        JSONObject merged = getData(new MergeCaseDto(req.getCaseId(), req.getChooseContent(), SystemConstant.EMPTY_STR, DEFAULT_ENV, 0L));
        ExecRecord record = buildExecRecord(req, merged);
        return execRecordMapper.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long recordId) {
        execRecordMapper.delete(recordId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void editRecord(RecordUpdateReq req) {
        // 需要注意的是 圈选用例的对content的修改与脑图patch的修改不是同一频段
        // 所以这里修改的是圈选用例的话 一定要将websocket的redis清空
        ExecRecord record = execRecordMapper.selectOne(req.getId());
        if (record == null) {
            throw new ServiceException("对应执行任务不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }
        // 如果修改圈选用例时，有人在协同修改任务或者用例，那么就不让改
//        if (!record.getChooseContent().equals(req.getChooseContent())) {
//            Room room = getWsEditingCount(record);
//            if (room != null && room.players.size() > 0) {
//                LOGGER.info("[异步编辑任务属性]入参={}, 这些人正在编辑={}", req.toString(), room.getRoomPlayersName());
//                throw new CaseServerException("当前" + room.getRoomPlayersName() + "正在修改，不允许修改圈选用例", StatusCode.INTERNAL_ERROR);
//            }
//        }
        // 日期类转换,在request.validate()已经解决单边输入为null的不完整问题
        if (req.getExpectStartTime() != null) {
            // 就是说这里是有日期区间的
            record.setExpectStartTime(new Date(req.getExpectStartTime()/1000*1000));
            record.setExpectEndTime(new Date(req.getExpectEndTime()/1000*1000));
        } else {
            // 没有区间设置默认值 1970-01-01 00:00:00
            record.setExpectStartTime(new Date(31507200000L));
            record.setExpectEndTime(new Date(31507200000L));
        }

        record.setModifier(req.getModifier());
        record.setTitle(req.getTitle());
        record.setChooseContent(req.getChooseContent());
        record.setDescription(StringUtils.isEmpty(req.getDescription()) ? SystemConstant.EMPTY_STR : req.getDescription());
        record.setOwner(StringUtils.isEmpty(req.getOwner()) ? SystemConstant.EMPTY_STR : req.getOwner());

        execRecordMapper.edit(record);
    }

    @Override
    public RecordWsDto getWsRecord(Long recordId) {
        ExecRecord record = execRecordMapper.selectOne(recordId);
        if (record == null) {
            throw new ServiceException("执行任务不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }
        RecordWsDto dto = new RecordWsDto();
        dto.setCaseContent(record.getCaseContent());
        dto.setChooseContent(record.getChooseContent());
        dto.setEnv(record.getEnv());
        dto.setExecutors(record.getExecutors());
        dto.setUpdateTime(record.getGmtModified());

        return dto;
    }

    @Override
    public void modifyRecord(ExecRecord record) {
        if (record == null) {
            throw new ServiceException("对应执行任务不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }
        if (StringUtils.isEmpty(record.getCaseContent())) {
            throw new ServiceException("用例/任务内容为空" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }
        if (StringUtils.isEmpty(record.getModifier())) {
            throw new ServiceException("修改人为空" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }
        ExecRecord dbRecord = execRecordMapper.selectOne(record.getId());
        BeanUtils.copyProperties(record, dbRecord);
        execRecordMapper.update(dbRecord);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExecRecord wsClearRecord(RecordWsClearReq req) {
        ExecRecord record = execRecordMapper.selectOne(req.getId());
        record.setCaseContent(SystemConstant.EMPTY_STR);
        record.setSuccessCount(0);
        record.setPassCount(0);
        record.setFailCount(0);
        record.setBlockCount(0);
        record.setIgnoreCount(0);
        record.setModifier(req.getModifier());
        record.setGmtModified(new Date());

        execRecordMapper.update(record);

        JSONObject merged = getData(new MergeCaseDto(record.getCaseId(), record.getChooseContent(), record.getCaseContent(), record.getEnv(), record.getId()));
        record.setCaseContent(merged.get("content").toString());
        return record;
    }

    /**
     * 根据文件夹id获取用例集列表
     *
     * @param recordId 记录id caseId 用例id
     * @return 用例集详情
     */
    @Override
    public TestCase getCaseDetail(Long recordId, Long caseId) {
        ExecRecord execRecord = execRecordMapper.selectOne(recordId);
        TestCase testCase =   testCaseMapper.selectOne(caseId);
        String caseContent = execRecord.getCaseContent();

        // 合并执行记录和用例内容
        String recordContent = mergeRecord(recordId, testCase.getCaseContent(), execRecordMapper);
        if(caseContent == null || caseContent.isEmpty()){
            caseContent = testCase.getCaseContent();
        } else {
            caseContent = execRecord.getCaseContent();
        }
        // 更新用例的内容
        testCase.setCaseContent(caseContent);
        // 设置用例的组ID为执行记录的ID
        testCase.setGroupId(recordId);
        // 初始化当前类的recordMapper属性
        return testCase;
    }

    @Override
    public void updateRecord(RecordUpdateReq req) {

       TestCase testCase = testCaseMapper.selectOne(req.getCaseId());
        // 合并执行记录和用例内容
        String recordContent = mergeRecord(req.getId(), req.getCaseContent(), execRecordMapper);
        // 更新用例的内容
        testCase.setCaseContent(recordContent);
        // 设置用例的组ID为执行记录的ID
        testCase.setGroupId(req.getId());
        //版本号做版本控制
//        try {
//            ObjectMapper jsonMapper = new ObjectMapper();
//            ArrayNode patch = (ArrayNode) jsonMapper.readTree(data.getPatch());
//            JsonNode roomContent = jsonMapper.readTree(room.getCaseContent());
//            int serverCaseCurrentVersion = roomContent.get("base").asInt();
//            int serverCaseExpectVersion = serverCaseCurrentVersion + 1;
//
//            ArrayNode patchNew = patchTraverse(patch);
//            ObjectNode basePatch = FACTORY.objectNode();
//            basePatch.put("op", "replace");
//            basePatch.put("path", "/base");
//            basePatch.put("value", serverCaseExpectVersion);
//            patchNew.add(basePatch);
//
//            JsonNode roomContentNew;
//
//            if (serverCaseCurrentVersion > data.getCaseVersion()) { // 服务端版本大于前端
//                LOGGER.warn("version of case in memory is bigger than client. version is: " + roomContent.get("base").asInt() + ", client version: " + data.getCaseVersion());
//                roomContentNew = JsonPatch.apply(patchNew, roomContent);
//
//                ArrayNode patchAck = (ArrayNode) JsonDiff.asJson(jsonMapper.readTree(data.getCaseContent()), roomContentNew, EnumSet.of(ADD_ORIGINAL_VALUE_ON_REPLACE, OMIT_MOVE_OPERATION));
//                executorEgressService.submit(new AckEgressTask("edit_ack_event", PushMessage.builder().message(patchAck.toString()).build(), client));
//                executorEgressService.submit(new NotifyExcludeEgressTask("edit_notify_event", PushMessage.builder().message(patchNew.toString()).build(), client, broadcastOperations));
////                    client.sendEvent("edit_ack_event", PushMessage.builder().message(patchAck.toString()).build(), PushMessage.builder().message(patchNew.toString()).build());
////                    broadcastOperations.sendEvent("edit_notify_event", client, PushMessage.builder().message(patchNew.toString()).build());
//            } else { // 服务端版本小于等于前端，中间链接断开，导致变更未传递到后端
//                LOGGER.warn("version of case in memory is smaller than client. version is: " + roomContent.get("base").asInt() + ", client version: " + data.getCaseVersion());
//                // todo：为避免丢失，此处应该先保存客户端的case
//                String clientExceptContent = data.getCaseContent().replace("\"base\":" + data.getCaseVersion(), "\"base\":" + (data.getCaseVersion() + 1));
//                roomContentNew = jsonMapper.readTree(clientExceptContent);
//                ArrayNode patchNotify = (ArrayNode) JsonDiff.asJson(roomContent, jsonMapper.readTree(clientExceptContent), EnumSet.of(ADD_ORIGINAL_VALUE_ON_REPLACE, OMIT_MOVE_OPERATION));
//                executorEgressService.submit(new AckEgressTask("edit_ack_event", PushMessage.builder().message("[[{\"op\":\"replace\",\"path\":\"/base\",\"value\":" + (data.getCaseVersion() + 1) + "}]]").build(), client));
//                executorEgressService.submit(new NotifyExcludeEgressTask("edit_notify_event", PushMessage.builder().message(patchNotify.toString()).build(), client, broadcastOperations));
//
//            }
//
//            room.setCaseContent(roomContentNew.toString());
//
//        } catch (Exception e) {
//            LOGGER.error("json 操作失败。", e);
//            executorEgressService.submit(new AckEgressTask("warning", PushMessage.builder().message("可能存在编辑冲突，请刷新重试.").build(), client));
////            client.sendEvent("warning", PushMessage.builder().message("可能存在编辑冲突，请刷新重试.").build());
//        } finally {
//            room.unlock();
//        }


        // 更新用例的修改时间
        testCase.setGmtModified(new Date(System.currentTimeMillis()));
        // 获取客户端握手数据中的用户信息
        String user = req.getExecutors();
        // 根据客户端握手数据中的执行记录ID查询执行记录
        ExecRecord record = execRecordMapper
                .selectOne(Long.valueOf(req.getId()));
        // 如果执行记录不存在，抛出异常
//        if (record == null) {
//            throw new CaseServerException("执行任务不存在", StatusCode.NOT_FOUND_ENTITY);
//        }

        // 解析用例内容为JSONObject
        JSONObject jsonObject = TreeUtil.parse(testCase.getCaseContent());
        // 获取用例内容中的进度信息
        JSONObject jsonProgress = jsonObject.getJSONObject("progress");
        // 获取用例的总数量
        Integer totalCount = jsonObject.getInteger("totalCount");
        int a = totalCount.intValue();
        // 获取通过的用例数量
        Integer passCount = jsonObject.getInteger("passCount");
        // 获取失败的用例数量
        Integer failCount = jsonObject.getInteger("failCount");
        // 获取阻塞的用例数量
        Integer blockCount = jsonObject.getInteger("blockCount");
        // 获取成功的用例数量
        Integer successCount = jsonObject.getInteger("successCount");
        // 获取忽略的用例数量
        Integer ignoreCount = jsonObject.getInteger("ignoreCount");

        // 将执行记录中的执行者字符串按逗号分割为列表，并过滤掉空字符串
        List<String> names = Arrays.stream(record.getExecutors().split(SystemConstant.COMMA)).filter(e -> !StringUtils.isEmpty(e))
                .collect(Collectors.toList());
        // 统计当前用户在执行者列表中的出现次数
        long count = names.stream().filter(e -> e.equals(user)).count();

        // 如果当前用户在执行者列表中已存在，不做处理
        if (count > 0) {
            // 有重合，不管了
        } else {
            // 如果当前用户不在执行者列表中，将其添加到列表末尾
            names.add(user);
        }

        // 更新执行记录中的执行者列表
        record.setExecutors(String.join(",", names));
        // 设置执行记录的修改者为当前用户
        record.setModifier(user);
        // 更新执行记录的修改时间
        record.setGmtModified(new Date(System.currentTimeMillis()));
        // 设置执行记录的用例内容为进度信息的JSON字符串
        record.setCaseContent(jsonProgress.toJSONString());
        // 更新执行记录中的失败用例数量
        record.setFailCount(failCount);
        // 更新执行记录中的阻塞用例数量
        record.setBlockCount(blockCount);
        // 更新执行记录中的忽略用例数量
        record.setIgnoreCount(ignoreCount);
        // 更新执行记录中的通过用例数量
        record.setPassCount(passCount);
        // 更新执行记录中的总用例数量
        record.setTotalCount(totalCount);
        // 更新执行记录中的成功用例数量
        record.setSuccessCount(successCount);
        // 更新执行记录到数据库
        record.setCaseContent(req.getCaseContent());
        execRecordMapper.update(record);
    }


    // 合并执行记录和用例内容的方法
    private String mergeRecord(Long recordId, String caseContentStr, ExecRecordMapper execRecordMapper) {

        String retCaseContent;

        // 根据执行记录ID查询执行记录
        ExecRecord record = execRecordMapper.selectOne(recordId);
        // 如果执行记录为空，记录错误信息（这里理论上不应该为空）
        if (record == null) {
            // todo: 在controller层应该已经创建了任务，因此这里一定不为空
        }

        // 获取执行记录中的用例内容
        String recordContent = record.getCaseContent();
        // 创建一个JSONObject用于存储执行记录内容
        JSONObject recordObj = new JSONObject();
        // 如果执行记录内容为空，说明当前任务还没有任何执行记录
        if (StringUtils.isEmpty(recordContent)) {
            // 其实当前任务还没有任何执行记录
        } else if (recordContent.startsWith("[{")) {
            // 如果执行记录内容是JSON数组格式，解析JSON数组并将其转换为JSONObject
            JSONArray jsonArray = JSON.parseArray(recordContent);
            for (Object o : jsonArray) {
                recordObj.put(((JSONObject) o).getString("id"), ((JSONObject) o).getLong("progress"));
            }
        } else {
            // 如果执行记录内容是普通的JSONObject格式，直接解析
            recordObj = JSON.parseObject(recordContent);
        }

        // 创建一个IntCount对象，用于统计执行记录中的数量
        IntCount ExecCount = new IntCount(recordObj.size());
        // 如果当前record是圈选了部分的圈选用例
        if (!StringUtils.isEmpty(record.getChooseContent())
                && !record.getChooseContent().contains("\"priority\":[\"0\"]")) {
            // 解析圈选内容为Map
            Map<String, List<String>> chosen = JSON.parseObject(record.getChooseContent(), Map.class);

            // 解析用例内容为JSONObject
            JSONObject caseContent = JSON.parseObject(caseContentStr);
            // 获取用例内容中的根节点
            JSONObject caseRoot = caseContent.getJSONObject("root");
            // 创建一个栈用于检查节点
            Stack<JSONObject> objCheck = new Stack<>();

            // 创建一个栈用于检查数量
            Stack<IntCount> iCheck = new Stack<>();
            // 将根节点压入栈中
            objCheck.push(caseRoot);

            // 获取圈选的优先级列表
            List<String> priority = chosen.get("priority");
            // 获取圈选的资源列表
            List<String> resource = chosen.get("resource");
            // 获取对应级别用例
            if (!CollectionUtils.isEmpty(priority)) {
                TreeUtil.getPriority(objCheck, iCheck, caseRoot, priority);
            }
            if (!CollectionUtils.isEmpty(resource)) {
                TreeUtil.getChosenCase(caseRoot, new HashSet<>(resource), "resource");
            }

            // 合并执行记录和用例内容
            TreeUtil.mergeExecRecord(caseContent.getJSONObject("root"), recordObj, ExecCount);
            // 将合并后的用例内容转换为JSON字符串
            retCaseContent = caseContent.toJSONString();
        } else {
            // 如果是全部的，那么直接把testcase 给 merge过来
            // 解析用例内容为JSONObject
            JSONObject caseContent = JSON.parseObject(caseContentStr);
            // 合并执行记录和用例内容
            TreeUtil.mergeExecRecord(caseContent.getJSONObject("root"), recordObj, ExecCount);
            // 将合并后的用例内容转换为JSON字符串
            retCaseContent = caseContent.toJSONString();
        }
        return retCaseContent;
    }

    /**
     * 任务列表
     *
     * @param record 执行任务实体
     * @return 响应体
     */
    private RecordListResp buildList(ExecRecord record) {
        RecordListResp resp = new RecordListResp();
        resp.setId(record.getId());
        resp.setRecordId(record.getId());
        resp.setCaseId(record.getCaseId());
        resp.setTitle(record.getTitle());
        resp.setOwner(record.getOwner());
        resp.setCreator(record.getCreator());
        resp.setExecutors(record.getExecutors());

        // 其实本质上不能通过数据库去获取，因为还需要考虑chooseContent
        JSONObject object = getData(new MergeCaseDto(record.getCaseId(), record.getChooseContent(), record.getCaseContent(), record.getEnv(), 0L));
        resp.setBugNum(record.getFailCount());
        resp.setBlockNum(record.getBlockCount());
        resp.setSuccessNum(record.getSuccessCount());
        resp.setExecuteNum(record.getPassCount());
        resp.setTotalNum(record.getTotalCount());
        resp.setChooseContent(record.getChooseContent());
        resp.setCreateTime(record.getGmtCreated().getTime());
        resp.setDescription(record.getDescription());
        resp.setExpectStartTime(
                compareToOriginalDate(record.getExpectStartTime()) ? null : record.getExpectStartTime());
        resp.setExpectEndTime(
                compareToOriginalDate(record.getExpectEndTime()) ? null : record.getExpectEndTime());
        return resp;
    }

    /**
     * 构建任务实体
     *
     * @param req 请求体
     * @param merged 合并后的内容
     * @return
     */
    private ExecRecord buildExecRecord(RecordAddReq req, JSONObject merged) {
        ExecRecord record = new ExecRecord();
        // 统计信息传入
        record.setFailCount(merged.getInteger("failCount"));
        record.setBlockCount(merged.getInteger("blockCount"));
        record.setSuccessCount(merged.getInteger("successCount"));
        record.setPassCount(merged.getInteger("passCount"));
        record.setIgnoreCount(merged.getInteger("ignoreCount"));
        record.setTotalCount(merged.getInteger("totalCount"));
        // 基础信息传入
        record.setTitle(req.getTitle());
        record.setCaseId(req.getCaseId());
        record.setIsDelete(SystemConstant.NOT_DELETE);
        record.setCaseContent(SystemConstant.EMPTY_STR);
        record.setCreator(req.getCreator());
        record.setModifier(SystemConstant.EMPTY_STR);
        record.setGmtCreated(new Date());
        record.setGmtModified(new Date());
        record.setChooseContent(StringUtils.isEmpty(req.getChooseContent()) ? SystemConstant.EMPTY_STR : req.getChooseContent());
        record.setDescription(StringUtils.isEmpty(req.getDescription()) ? SystemConstant.EMPTY_STR : req.getDescription());
        record.setExecutors(SystemConstant.EMPTY_STR);
        record.setOwner(req.getOwner());
        record.setEnv(0);

        if (req.getExpectStartTime() != null) {
            // 就是说这里是有日期区间的
            record.setExpectStartTime(new Date(req.getExpectStartTime()/1000*1000));
            record.setExpectEndTime(new Date(req.getExpectEndTime()/1000*1000));
        } else {
            // 没有区间设置默认值
            record.setExpectStartTime(new Date(31507200000L));
            record.setExpectEndTime(new Date(31507200000L));
        }
        return record;
    }

    /**
     * 脑图统计条
     *
     * @param record 执行任务实体
     * @param testCase 用例
     * @param merged 合并后的json体
     * @return 响应体
     */
    private RecordGeneralInfoResp buildGeneralInfoResp(ExecRecord record, TestCase testCase, JSONObject merged) {
        RecordGeneralInfoResp resp = new RecordGeneralInfoResp();
        resp.setId(record.getId());
        resp.setTitle(record.getTitle());
        resp.setCaseId(testCase.getId());
        resp.setRequirementIds(testCase.getPlanId());
        resp.setExpectStartTime(
                compareToOriginalDate(record.getExpectStartTime()) ? null : record.getExpectStartTime());
        resp.setExpectEndTime(
                compareToOriginalDate(record.getExpectEndTime()) ? null : record.getExpectEndTime());
        if (merged.getInteger("totalCount") == 0 || merged.getInteger("totalCount") == null) {
            // RecordGeneralInfoResp 内部给统计数据默认了0
            return resp;
        }
        resp.setPassCount(merged.getInteger("passCount"));
        resp.setBugNum(merged.getInteger("failCount"));
        resp.setSuccessCount(merged.getInteger("successCount"));
        resp.setBlockCount(merged.getInteger("blockCount"));
        resp.setIgnoreCount(merged.getInteger("ignoreCount"));
        resp.setTotalCount(merged.getInteger("totalCount"));

        BigDecimal passRate = BigDecimal.valueOf((double) resp.getSuccessCount() * 100 / (double) resp.getTotalCount());
        //通过率=执行成功用例/总用例
        resp.setPassRate(passRate.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        return resp;
    }

    /**
     * 获取一份用例下正在编辑的所有人  包括任务+用例
     *
     * @param record 执行任务实体
     * @return 响应体
     */
//    public Room getWsEditingCount(ExecRecord record) {
//        TestCase testCase = caseMapper.selectOne(record.getCaseId());
//        if (testCase == null) {
//            throw new CaseServerException("当前用例不存在", StatusCode.INTERNAL_ERROR);
//        }
//
//        return WebSocket.getRoom(false, BitBaseUtil.mergeLong(record.getId(), record.getCaseId()));
//    }

    /**
     * ☆将当前record的操作记录和用例集的内容进行merge，返回合并后的内容
     */
    public JSONObject getData(MergeCaseDto dto) {
        String websocketCaseContent = null;
        if (dto.getRecordId() > 0L) {
//            websocketCaseContent = WebSocket.getRoom(false, BitBaseUtil.mergeLong(dto.getRecordId(), dto.getCaseId())).getTestCaseContent();
        }

        String caseContent = testCaseMapper.selectOne(dto.getCaseId()).getCaseContent();
        JSONObject content = JSON.parseObject(caseContent);
        if (websocketCaseContent != null) {
            JSONObject websocketContent = JSON.parseObject(websocketCaseContent);
            long currentBase = websocketContent.getLong("base");
            content.put("base", currentBase);
        }

        // 如果不是全部圈选的圈选条件
        if (!StringUtils.isEmpty(dto.getChooseContent()) && !dto.getChooseContent().contains(OE_PICK_ALL)) {
            PickCaseDto pickCaseDto = JSON.parseObject(dto.getChooseContent(), PickCaseDto.class);

            // 似乎是想用BFS做广度遍历
            JSONObject caseRoot = content.getJSONObject("root");
            Stack<JSONObject> objCheck = new Stack<>();
            Stack<IntCount> iCheck = new Stack<>();
            objCheck.push(caseRoot);

            //获取对应级别用例
            if (!CollectionUtils.isEmpty(pickCaseDto.getPriority())) {
                TreeUtil.getPriority(objCheck, iCheck, caseRoot, pickCaseDto.getPriority());
            }
            if (!CollectionUtils.isEmpty(pickCaseDto.getResource())) {
                TreeUtil.getChosenCase(caseRoot, new HashSet<>(pickCaseDto.getResource()), "resource");
            }
        } else {
            // 给未来的环境选择做好打算...
            if (EnvEnum.TestQaEnv.getValue().equals(dto.getEnv()) || EnvEnum.TestRdEnv.getValue().equals(dto.getEnv())) {
                // 似乎是想用BFS做广度遍历
                JSONObject caseRoot = content.getJSONObject("root");
                Stack<JSONObject> objCheck = new Stack<>();
                Stack<IntCount> iCheck = new Stack<>();
                objCheck.push(caseRoot);

                // 这里就是默认圈选全部用例
                TreeUtil.getPriority0(objCheck, iCheck, caseRoot);
            }
        }
        //合并用例
        String recordContent = dto.getRecordContent();
        JSONObject recordObj = new JSONObject();

        if (StringUtils.isEmpty(recordContent)) {
            // 脏数据，不管
        } else if (recordContent.startsWith("[{")) {
            for (Object o : JSON.parseArray(recordContent)) {
                recordObj.put(((JSONObject) o).getString("id"), ((JSONObject) o).getLong("progress"));
            }
        } else {
            recordObj = JSON.parseObject(recordContent);
        }

        IntCount execCount = new IntCount(recordObj.size());
        TreeUtil.mergeExecRecord(content.getJSONObject("root"), recordObj, execCount);
        return TreeUtil.parse(content.toJSONString());
    }






}
