package com.qa.manager.service;

import java.util.List;

import com.qa.manager.domain.Dto.RecordWsDto;
import com.qa.manager.domain.TestCase;
import com.qa.manager.domain.request.cases.CaseDetailResp;
import com.qa.manager.domain.request.record.*;

/**
 * 用例执行记录Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-23
 */
public interface IExecRecordService 
{
    /**
     * 查询用例执行记录
     * 
     * @param id 用例执行记录主键
     * @return 用例执行记录
     */
    public ExecRecord selectExecRecordById(Long id);

    /**
     * 查询用例执行记录列表
     * 
     * @param execRecord 用例执行记录
     * @return 用例执行记录集合
     */
    public List<ExecRecord> selectExecRecordList(ExecRecord execRecord);

    /**
     * 新增用例执行记录
     * 
     * @param execRecord 用例执行记录
     * @return 结果
     */
    public int insertExecRecord(ExecRecord execRecord);

    /**
     * 修改用例执行记录
     * 
     * @param execRecord 用例执行记录
     * @return 结果
     */
    public int updateExecRecord(ExecRecord execRecord);

    /**
     * 批量删除用例执行记录
     * 
     * @param ids 需要删除的用例执行记录主键集合
     * @return 结果
     */
    public int deleteExecRecordByIds(Long[] ids);

    /**
     * 删除用例执行记录信息
     * 
     * @param id 用例执行记录主键
     * @return 结果
     */
    public int deleteExecRecordById(Long id);



    /**
     * 根据用例集caseId，查询该用例集下所有的执行任务
     *
     * @param caseId 任务所属的用例集id
     * @return 执行任务列表
     */
    List<RecordListResp> getListByCaseId(Long caseId);

    /**
     * 协同页面，获取上方基础信息
     *
     * @param recordId 操作记录id
     * @return 基础信息
     */
    RecordGeneralInfoResp getGeneralInfo(Long recordId);

    /**
     * 添加执行任务
     *
     * @param req 请求体
     * @return 任务id
     */
    Long addRecord(RecordAddReq req);

    /**
     * 逻辑删除执行任务
     *
     * @param recordId 任务id
     */
    void delete(Long recordId);

    /**
     * 编辑执行任务的属性
     *
     * @param req 请求体
     */
    void editRecord(RecordUpdateReq req);

    /**
     * 给websocket使用的获取执行任务的方法
     *
     * @param recordId 任务id
     * @return 转换体
     */
    RecordWsDto getWsRecord(Long recordId);

    /**
     * 修改记录
     *
     * @param record 任务实体
     */
    void modifyRecord(ExecRecord record);

    /**
     * 协同页面，清除执行记录
     *
     * @param req 请求体
     * @return 由于不知道前端到底用了什么字段，所以就直接返回entity吧
     */
    ExecRecord wsClearRecord(RecordWsClearReq req);


    /**
     * 根据文件夹id获取用例集列表
     *
     * @param recordId 记录id caseId 用例id
     * @return 用例集详情
     */
    TestCase getCaseDetail(Long recordId, Long caseId);

    void updateRecord(RecordUpdateReq req);

}
