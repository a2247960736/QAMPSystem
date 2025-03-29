package com.qa.manager.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qa.common.constant.SystemConstant;
import com.qa.common.constant.enums.StatusCode;
import com.qa.common.core.domain.entity.SysUser;
import com.qa.common.exception.ServiceException;
import com.qa.common.utils.TimeUtil;
import com.qa.manager.util.TreeUtil;
import com.qa.manager.domain.Biz;
import com.qa.manager.domain.Dto.DirNodeDto;
import com.qa.manager.domain.Dto.RecordNumDto;
import com.qa.manager.domain.request.cases.*;
import com.qa.manager.domain.request.dir.BizListResp;
import com.qa.manager.domain.request.dir.DirTreeResp;
import com.qa.manager.mapper.BizMapper;
import com.qa.manager.mapper.ExecRecordMapper;
import com.qa.manager.service.IBizService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.TestCaseMapper;
import com.qa.manager.domain.TestCase;
import com.qa.manager.service.ITestCaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试用例Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
@Service
public class TestCaseServiceImpl implements ITestCaseService 
{
    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private IBizService bizService;

    @Autowired
    private BizMapper bizMapper;

    @Autowired
    private ExecRecordMapper execRecordMapper;


    /**
     * 查询测试用例
     * 
     * @param id 测试用例主键
     * @return 测试用例
     */
    @Override
    public TestCase selectTestCaseById(Long id)
    {
        return testCaseMapper.selectTestCaseById(id);
    }

    /**
     * 查询测试用例列表
     * 
     * @param testCase 测试用例
     * @return 测试用例
     */
    @Override
    public List<TestCase> selectTestCaseList(TestCase testCase)
    {
        return testCaseMapper.selectTestCaseList(testCase);
    }

    /**
     * 新增测试用例
     * 
     * @param testCase 测试用例
     * @return 结果
     */
    @Override
    public int insertTestCase(TestCase testCase)
    {
        return testCaseMapper.insertTestCase(testCase);
    }

    /**
     * 修改测试用例
     * 
     * @param testCase 测试用例
     * @return 结果
     */
    @Override
    public int updateTestCase(TestCase testCase)
    {
        return testCaseMapper.update(testCase);
    }

    /**
     * 批量删除测试用例
     * 
     * @param ids 需要删除的测试用例主键
     * @return 结果
     */
    @Override
    public int deleteTestCaseByIds(Long[] ids)
    {
        return testCaseMapper.deleteTestCaseByIds(ids);
    }

    /**
     * 删除测试用例信息
     * 
     * @param id 测试用例主键
     * @return 结果
     */
    @Override
    public int deleteTestCaseById(Long id)
    {
        return testCaseMapper.deleteTestCaseById(id);
    }

    @Override
// 获取用例列表的方法，入参为用例查询请求对象，返回分页模块，包含用例列表响应数据
    public List<CaseListResp> getCaseList(CaseQueryReq request) {
        // 创建一个空的用例列表响应集合，用于存储最终要返回的用例数据
        List<CaseListResp> res = new ArrayList<>();
        // 根据请求中的业务线ID、业务ID和渠道，调用dirService获取相关的用例ID列表
        List<Long> caseIds = bizService.getCaseIds(request.getLineId(), request.getBizId(), request.getChannel());

        // 如果获取到的用例ID列表为空，直接返回一个空的分页模块
        if (CollectionUtils.isEmpty(caseIds)) {
            return new ArrayList<>();
        }

        // 将请求中的开始时间字符串转换为Date类型，转换失败或为空时返回null
        Date beginTime = transferTime(request.getBeginTime());
        // 将请求中的结束时间字符串转换为Date类型，转换失败或为空时返回null
        Date endTime = transferTime(request.getEndTime());
        // 使用PageHelper进行分页设置，传入请求中的页码和页大小
//        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        // 根据各种条件（用例类型、用例ID列表、标题、创建者等）调用caseMapper进行用例搜索，获取用例列表

        List<TestCase> caseList = testCaseMapper.search(request.getCaseType(), caseIds, request.getTitle(), request.getCreator(),
                request.getPlanId(), beginTime, endTime, request.getChannel(), request.getLineId(), "");



//         根据用例ID列表，调用recordMapper获取每个用例的记录数量数据传输对象列表
        List<RecordNumDto> recordNumDtos = execRecordMapper.getRecordNumByCaseIds(caseIds);
        // 将记录数量数据传输对象列表转换为一个Map，键为用例ID，值为记录数量
        Map<Long, Integer> recordMap = recordNumDtos.stream().collect(Collectors.toMap(RecordNumDto::getCaseId, RecordNumDto::getRecordNum));

        // 遍历查询到的用例列表，将每个用例转换为用例列表响应对象，并添加到结果集合中
        for (TestCase testCase : caseList) {
            res.add(buildListResp(testCase, recordMap.get(testCase.getId())));
        }


        // 根据结果集合和查询到的用例总数，构建并返回分页模块
        return res;
    }

    @Override
// 获取用例详情的方法，入参为用例ID，返回用例详情响应对象
    public CaseDetailResp getCaseDetail(Long caseId) {
        // 根据用例ID调用caseMapper查询用例信息
        TestCase testCase = testCaseMapper.selectOne(caseId);
        // 如果查询到的用例为空，抛出用例不存在的异常
        if (testCase == null) {
            throw new ServiceException("用例不存在" + StatusCode.INTERNAL_ERROR.getMsg() , StatusCode.INTERNAL_ERROR.getStatus());
        }
        // 如果用例的删除标志为已删除，抛出用例已删除的异常
        if (testCase.getIsDelete().equals(SystemConstant.IS_DELETE)) {
            throw new ServiceException("用例不存在" + StatusCode.INTERNAL_ERROR.getMsg() , StatusCode.INTERNAL_ERROR.getStatus());
        }
        // 将查询到的用例转换为用例详情响应对象并返回
        return buildDetailResp(testCase);
    }

    @Override
// 插入或复制用例的方法，入参为用例创建请求对象，返回新插入或复制的用例ID，该方法添加了事务支持
    @Transactional(rollbackFor = Exception.class)
    public Long insertOrDuplicateCase(CaseCreateReq request) {
        // 根据请求对象构建新的用例对象
        TestCase testcase = buildCase(request);
        // 将新的用例对象插入数据库
        testCaseMapper.insert(testcase);
        // 获取指定业务线ID和渠道的目录树结构，可能会有多个用例加入，所以不使用dirService.addCase()方法
        DirNodeDto tree = bizService.getDirTree(testcase.getProductLineId(), testcase.getChannel());
        // 将请求中的业务ID字符串按逗号分割为列表
        List<String> addBizs = Arrays.asList(request.getBizId().split(SystemConstant.COMMA));
        // 深度优先搜索更新目录树结构，添加新用例ID到相关节点
        updateDFS(packageTree(tree), String.valueOf(testcase.getId()), new HashSet<>(addBizs), new HashSet<>());
        // 更新用例相关的业务信息
        updateBiz(testcase, tree);
        // 返回新插入或复制的用例ID
        return testcase.getId();
    }

    @Override
// 更新用例的方法，入参为用例编辑请求对象，返回目录树响应对象，该方法添加了事务支持
    @Transactional(rollbackFor = Exception.class)
    public DirTreeResp updateCase(CaseEditReq request) {
        // 根据用例ID调用caseMapper查询用例信息
        TestCase testCase = testCaseMapper.selectOne(request.getId());
        // 如果查询到的用例为空，抛出用例不存在的异常
        if (testCase == null) {
            throw new ServiceException("用例不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }

        // 获取新的业务ID列表与旧的业务ID列表的差异，即新增的业务ID列表
        List<String> addBizs = getDiffSet(request.getBizId(), testCase.getBizId());
        // 获取旧的业务ID列表与新的业务ID列表的差异，即要移除的业务ID列表
        List<String> rmBizs = getDiffSet(testCase.getBizId(), request.getBizId());

        // 将请求对象的属性复制到用例对象中，更新相关属性
        BeanUtils.copyProperties(request, testCase);
        testCase.setGmtModified(new Date());
        testCase.setModifier(request.getModifier());
        // 获取指定业务线ID和渠道的目录树结构
        DirNodeDto tree = bizService.getDirTree(testCase.getProductLineId(), testCase.getChannel());
        // 深度优先搜索更新目录树结构，添加和移除相关用例ID
        updateDFS(packageTree(tree), String.valueOf(request.getId()), new HashSet<>(addBizs), new HashSet<>(rmBizs));
        // 更新用例相关的业务信息
        updateBiz(testCase, tree);

        // 更新数据库中的用例信息
        testCaseMapper.update(testCase);

        // 返回更新后的所有用例目录树结构
        return bizService.getAllCaseDir(tree);
    }

    @Override
// 删除用例的方法，入参为用例ID，返回目录树响应对象，该方法添加了事务支持
    @Transactional(rollbackFor = Exception.class)
    public DirTreeResp deleteCase(Long caseId) {
        // 根据用例ID调用caseMapper查询用例信息
        TestCase testCase = testCaseMapper.selectOne(caseId);
        // 设置用例的删除标志为已删除
        testCase.setIsDelete(SystemConstant.IS_DELETE);

//        // 根据用例ID调用recordMapper获取该用例的所有操作记录列表
//        List<ExecRecord> execRecords = recordMapper.getRecordListByCaseId(testCase.getId());
//        // 如果操作记录列表不为空，批量删除这些操作记录
//        if (!CollectionUtils.isEmpty(execRecords)) {
//            recordMapper.batchDelete(execRecords.stream().map(ExecRecord::getId).collect(Collectors.toList()));
//        }

        // 获取指定业务线ID和渠道的目录树结构
        DirNodeDto tree = bizService.getDirTree(testCase.getProductLineId(), testCase.getChannel());
        // 深度优先搜索更新目录树结构，移除相关用例ID
        updateDFS(packageTree(tree), String.valueOf(caseId), new HashSet<>(), new HashSet<>(convertToList(testCase.getBizId())));
        // 更新用例相关的业务信息
        updateBiz(testCase, tree);

        // 删除数据库中的用例信息
        testCaseMapper.delete(testCase.getId());
        // 返回更新后的所有用例目录树结构
        return bizService.getAllCaseDir(tree);
    }

    @Override
// 获取用例创建者列表的方法，入参为用例类型和业务线ID，返回人员响应对象列表
    public  List<SysUser> listCreators(Integer caseType, Long lineId) {
        // 创建一个空的人员响应对象列表，用于存储最终要返回的创建者信息
        List<SysUser> list = new ArrayList<>();
        // 根据用例类型和业务线ID调用caseMapper获取创建者名称列表
        List<String> names = testCaseMapper.listCreators(caseType, lineId);

        // 如果获取到的创建者名称列表为空，直接返回空的人员响应对象列表
        if (CollectionUtils.isEmpty(names)) {
            return list;
        }

        // 将创建者名称列表转换为人员响应对象列表，设置人员的拼音名称和中文名称（目前英文名称未处理）
        return names.stream().map(name -> {
            SysUser person = new SysUser();
            person.setNickName(name);
            person.setUserName(name);
            return person;
        }).collect(Collectors.toList());
    }

    @Override
// 根据条件获取用例统计信息的方法，入参为用例条件请求对象，返回用例条件响应对象
    public CaseConditionResp getCountByCondition(CaseConditionReq req) {
        // 创建一个用例条件响应对象
        CaseConditionResp res = new CaseConditionResp();

        // 根据用例ID调用caseMapper查询用例信息
        TestCase testCase = testCaseMapper.selectOne(req.getCaseId());
        // 将用例的内容解析为JSONObject对象
        JSONObject content = JSONObject.parseObject(testCase.getCaseContent());
        // 获取用例内容的根节点JSONObject对象
        JSONObject caseRoot = content.getJSONObject("root");

        // 创建一个空的HashSet用于存储标签
        HashSet<String> tags = new HashSet<>();
        // 根据用例内容根节点计算用例数量，并将相关标签添加到tags集合中
        Integer caseNum = TreeUtil.getCaseNum(caseRoot, tags);

        // 设置总用例数量和标签列表到响应对象中
        res.setTotalCount(caseNum);
        res.setTaglist(tags);

        // 创建HashSet用于存储筛选条件中的优先级集合
        HashSet<String> prioritySet;
        // 创建HashSet用于存储筛选条件中的资源集合
        HashSet<String> resourceSet;
        // 如果筛选条件中的优先级列表不为空
        if (!CollectionUtils.isEmpty(req.getPriority())) {
            prioritySet = new HashSet<>(req.getPriority());
            // 根据优先级筛选条件过滤用例内容根节点，如果不满足条件则将根节点设置为空
            if (!TreeUtil.getChosenCase(caseRoot, prioritySet, "priority")) {
                caseRoot = null;
            }
        }
        // 如果筛选条件中的资源列表不为空
        if (!CollectionUtils.isEmpty(req.getResource())) {
            resourceSet = new HashSet<>(req.getResource());
            // 根据资源筛选条件过滤用例内容根节点，如果不满足条件则将根节点设置为空
            if (!TreeUtil.getChosenCase(caseRoot, resourceSet, "resource")) {
                caseRoot = null;
            }
        }
        // 如果没有筛选条件，设置统计的用例数量为null
        caseNum = (req.getPriority().size() == 0 && req.getResource().size() == 0) ? null : TreeUtil.getCaseNum(caseRoot, tags);
        // 设置统计的用例数量到响应对象中
        res.setCount(caseNum);
        // 返回用例条件响应对象
        return res;
    }

    @Override
// 获取用例基本信息的方法，入参为用例ID，返回用例基本信息响应对象
    public CaseGeneralInfoResp getCaseGeneralInfo(Long caseId) {
        // 根据用例ID调用caseMapper查询用例信息
        TestCase testCase = testCaseMapper.selectOne(caseId);
        // 如果查询到的用例为空，抛出用例不存在的异常
        if (testCase == null) {
            throw new ServiceException("用例不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
        }
        // 创建一个用例基本信息响应对象
        CaseGeneralInfoResp resp = new CaseGeneralInfoResp();
        // 设置用例的ID、业务线ID、需求ID和标题到响应对象中
        resp.setId(testCase.getId());
        resp.setPlanId(testCase.getPlanId());
        resp.setProductLineId(testCase.getProductLineId());
        resp.setTitle(testCase.getTitle());
        // 返回用例基本信息响应对象
        return resp;
    }
//
//    @Override
//// 保存WebSocket相关信息的方法，入参为WebSocket保存请求对象
//    public void wsSave(WsSaveReq req) {
////        List<String> editors = WebSocket.getEditingUser(String.valueOf(req.getId()),
//                StringUtils.isEmpty(req.getRecordId())?"undefined":String.valueOf(req.getRecordId()));
//        if (editors.size() < 1) {
//            throw new CaseServerException("用例ws链接已经断开，当前保存可能丢失，请刷新页面重建ws链接。", StatusCode.WS_UNKNOWN_ERROR);
//        }
//
//        // 记录当前线程名称和提示信息，表示开始保存用例
//        LOGGER.info(Thread.currentThread().getName() + ": http开始保存用例。");
//
//        // 创建一个用例备份对象
////        CaseBackup caseBackup = new CaseBackup();
////
////        // 设置备份对象的用例ID、用例内容、创建者等信息
////        caseBackup.setCaseId(req.getId());
////        caseBackup.setCaseContent(req.getCaseContent());
////        caseBackup.setRecordContent("");
////        caseBackup.setCreator(req.getModifier());
////        caseBackup.setExtra("");
////        // 将用例备份对象插入数据库
////        caseBackupService.insertBackup(caseBackup);
//
//        // 记录当前线程名称和提示信息，表示保存用例结束
//        LOGGER.info(Thread.currentThread().getName() + ": http开始保存结束。");
//
//    }

    /**
     * 字符串时间转date
     *
     * @param time 时间字符串
     * @return 如果字符串为空，那么Date也为空
     */
// 将字符串时间转换为Date类型的方法，空字符串返回null
    private Date transferTime(String time) {
        // 如果时间字符串为空，返回null
        if (time == null) {
            return null;
        }
        // 调用TimeUtil工具类的方法将字符串时间转换为Date类型
        return TimeUtil.transferStrToDateInSecond(time);
    }

    // 获取两个字符串表示的ID列表的差异的方法，返回新列表中存在而旧列表中不存在的ID列表
    private List<String> getDiffSet(String newStr, String oldStr) {
        // 将新的ID字符串按逗号分割为列表
        List<String> newIds = convertToList(newStr);
        //将旧的ID字符串按逗号分割为列表
        List<String> oldIds = convertToList(oldStr);
        // 从新列表中移除在旧列表中存在的ID
        newIds.removeIf(oldIds::contains);
        // 返回差异后的ID列表
        return newIds;
    }

    // 将字符串按逗号分割为列表的方法
    private List<String> convertToList(String str) {
        // 使用Stream将字符串按逗号分割后转换为列表
        return Arrays.stream(str.split(SystemConstant.COMMA)).collect(Collectors.toList());
    }

    /**
     * 构造/list下的用例列表
     *
     * @param testCase 测试用例
     * @return 列表单条
     * @see #getCaseList
     */
// 根据测试用例和记录数量构建用例列表响应对象的方法
    private CaseListResp buildListResp(TestCase testCase, Integer recordNum) {
        // 创建一个用例列表响应对象
        CaseListResp resp = new CaseListResp();
        // 将测试用例的属性复制到响应对象中
        BeanUtils.copyProperties(testCase, resp);
        // 设置响应对象的记录数量，如果记录数量为null则设置为0
        resp.setRecordNum(recordNum == null? 0 : recordNum);
        // 返回构建好的用例列表响应对象
        return resp;
    }

    /**
     * 构造用例详情内容
     *
     * @param testCase 测试用例
     * @return 详情单条
     * @see #getCaseDetail
     */
// 根据测试用例构建用例详情响应对象的方法
    private CaseDetailResp buildDetailResp(TestCase testCase) {
        // 创建一个用例详情响应对象
        CaseDetailResp resp = new CaseDetailResp();
        // 将测试用例的属性复制到响应对象中
        BeanUtils.copyProperties(testCase, resp);
        // 设置响应对象的业务信息，过滤出选中的业务信息列表
        resp.setBiz(
                getBizFlatList(testCase.getProductLineId(), Arrays.asList(testCase.getBizId().split(SystemConstant.COMMA)), testCase.getChannel())
                        .stream().filter(BizListResp::isSelect).collect(Collectors.toList())
        );
        // 设置响应对象的业务线ID
        resp.setProductLineId(testCase.getProductLineId());
        // 返回构建好的用例详情响应对象
        return resp;
    }

    /**
     * 查看详情时，显示关联的需求，以及所有的需求
     *
     * @param lineId 业务线id
     * @param bizIds 关联的文件夹id列表
     * @return 去掉顶级文件夹的文件夹树
     * @see #buildDetailResp
     */
// 根据业务线ID、业务ID列表和渠道获取扁平化的业务列表的方法
    private List<BizListResp> getBizFlatList(Long lineId, List<String> bizIds, Integer channel) {
        // 获取指定业务线ID和渠道的目录树结构
        DirNodeDto root = bizService.getDirTree(lineId, channel);
        // 创建一个空的业务列表响应对象列表，用于存储最终的业务信息
        List<BizListResp> list = new ArrayList<>();
        // 调用深度优先搜索方法，将目录树结构扁平化，将结果存储到list中
        flatDfs(root, list, new ArrayList<>(), bizIds);
        // 移除列表中的第一个元素（通常是顶级文件夹）
        list.remove(0);
        // 返回处理后的业务列表响应对象列表
        return list;
    }

    // 深度优先搜索方法，用于将目录树结构扁平化，将每个节点转换为业务列表响应对象并添加到列表中
    private void flatDfs(DirNodeDto node, List<BizListResp> list, List<String> path, List<String> bizIds) {
        // 根据当前节点、路径和业务ID列表构建业务列表响应对象，并添加到列表中
        list.add(buildBizList(node, path, bizIds));

        // 如果当前节点没有子节点，结束递归
        if (CollectionUtils.isEmpty(node.getChildren())) {
            return;
        }

        // 遍历当前节点的所有子节点
        for (int i = 0; i < node.getChildren().size(); i++) {
            // 将当前子节点的文本添加到路径列表中
            path.add(node.getChildren().get(i).getText());
            // 递归调用flatDfs方法处理子节点
            flatDfs(node.getChildren().get(i), list, path, bizIds);
            // 递归返回后，移除路径列表中最后一个元素，恢复路径
            path.remove(path.size() - 1);
        }
    }

    // 根据目录节点、路径和业务ID列表构建业务列表响应对象的方法
    private BizListResp buildBizList(DirNodeDto node, List<String> path, List<String> bizIds) {
        // 创建一个业务列表响应对象
        BizListResp obj = new BizListResp();
        // 设置业务ID
        obj.setBizId(node.getId());
        // 设置业务文本，将路径列表中的元素用">"连接起来
        obj.setText(String.join(">", path));
        // 设置是否选中的标志，根据业务ID列表判断当前节点是否被选中
        obj.setSelect(bizIds.contains(node.getId()));
        // 返回构建好的业务列表响应对象
        return obj;
    }

    /**
     * 新建/复制时，构建新的用例
     *
     * @param request 请求体
     * @return 新的请求体
     * @see #insertOrDuplicateCase
     */
// 根据用例创建请求对象构建新的测试用例对象的方法
    private TestCase buildCase(CaseCreateReq request) {
        // 获取请求中的用例内容
        String content = request.getCaseContent();
        // 如果请求中包含用例ID，说明是复制操作
        if (request.getId() != null) {
            // 根据用例ID查询原始用例信息
            TestCase testCase = testCaseMapper.selectOne(request.getId());
            // 如果原始用例不存在，抛出异常
            if (testCase == null) {
                throw new ServiceException("用例不存在" + StatusCode.NOT_FOUND_ENTITY.getMsg() , StatusCode.NOT_FOUND_ENTITY.getStatus());
            }
            // 使用原始用例的内容作为新用例的内容
            content = testCase.getCaseContent();
        }

        // 创建一个新的测试用例对象
        TestCase ret = new TestCase();
        // 设置测试用例的标题
        ret.setTitle(request.getTitle());
        // 设置测试用例的需求ID
        ret.setPlanId(request.getPlanId());
        // 设置测试用例的业务ID
        ret.setBizId(request.getBizId());
        // 设置测试用例的组ID
        ret.setGroupId(1L);
        // 设置测试用例的业务线ID
        ret.setProductLineId(request.getProductLineId());
        // 设置测试用例的描述
        ret.setDescription(request.getDescription());
        // 设置测试用例的创建者
        ret.setCreator(request.getCreator());
        // 设置测试用例的修改者为创建者
        ret.setModifier(ret.getCreator());
        // 设置测试用例的渠道
        ret.setChannel(request.getChannel());
        // 设置测试用例的额外信息为空字符串
        ret.setExtra(SystemConstant.EMPTY_STR);
        // 设置测试用例的创建时间为当前时间
        ret.setGmtCreated(new Date());
        // 设置测试用例的修改时间为当前时间
        ret.setGmtModified(new Date());
        // 设置测试用例的内容
        ret.setCaseContent(content);
        // 返回构建好的测试用例对象
        return ret;
    }

    /**
     * 更新json体
     *
     * @param node 树
     * @param addSet 需要新增caseId的set
     * @param rmSet 需要删减caseId的set
     */
// 深度优先搜索更新目录树结构的方法，根据新增和移除的用例ID集合更新节点信息
    private void updateDFS(DirNodeDto node, String caseId, Set<String> addSet, Set<String> rmSet) {
        // 如果当前节点没有子节点，结束递归
        if (CollectionUtils.isEmpty(node.getChildren())) {
            return;
        }

        // 遍历当前节点的所有子节点
        for (int i = 0; i < node.getChildren().size(); i++) {
            // 获取当前子节点
            DirNodeDto childNode = node.getChildren().get(i);
            // 如果当前子节点的ID在新增用例ID集合中，将用例ID添加到该节点的用例ID集合中
            if (addSet.contains(childNode.getId())) {
                childNode.getCaseIds().add(caseId);
            }
            // 如果当前子节点的ID在移除用例ID集合中，从该节点的用例ID集合中移除用例ID
            if (rmSet.contains(childNode.getId())) {
                childNode.getCaseIds().remove(caseId);
            }
            // 递归调用updateDFS方法处理子节点
            updateDFS(childNode, caseId, addSet, rmSet);
        }
    }

    /**
     * dir - 封装一下树的开头，这样树的头结点也可以进行插入
     */
// 封装目录树结构的方法，创建一个新的根节点，将原根节点作为其子节点
    private DirNodeDto packageTree(DirNodeDto node) {
        // 创建一个新的目录节点对象
        DirNodeDto pack = new DirNodeDto();
        // 将原目录节点添加到新节点的子节点列表中
        pack.getChildren().add(node);
        // 返回封装后的目录节点
        return pack;
    }

    /**
     * 更新文件夹
     *
     * @param testCase 测试用例
     * @param tree 树
     */
// 更新业务信息的方法，根据测试用例和目录树结构更新业务表中的内容
    public void updateBiz(TestCase testCase, DirNodeDto tree) {
        // 根据业务线ID和渠道查询业务信息
        Biz biz = bizMapper.selectOne(testCase.getProductLineId(), testCase.getChannel());
        // 将目录树结构转换为JSON字符串并更新到业务信息的内容字段中
        biz.setContent(JSON.toJSONString(tree));
        // 设置业务信息的修改时间为当前时间
        biz.setGmtModified(new Date());
        // 更新数据库中的业务信息
        bizMapper.update(biz);
    }


}
