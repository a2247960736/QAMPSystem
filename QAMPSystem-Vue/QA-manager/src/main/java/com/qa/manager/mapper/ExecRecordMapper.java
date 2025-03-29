package com.qa.manager.mapper;

import java.util.List;

import com.qa.manager.domain.Dto.RecordNumDto;
import com.qa.manager.domain.request.record.ExecRecord;

/**
 * 用例执行记录Mapper接口
 * 
 * @author jinrong.dong
 * @date 2025-03-23
 */
public interface ExecRecordMapper 
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
     * 删除用例执行记录
     * 
     * @param id 用例执行记录主键
     * @return 结果
     */
    public int deleteExecRecordById(Long id);

    /**
     * 批量删除用例执行记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExecRecordByIds(Long[] ids);






    /**
     * 新增记录
     *
     * @param record 操作记录实体
     * @return recordId
     */
    Long insert(ExecRecord record);

    /**
     * id查询执行任务
     *
     * @param id 执行任务id
     * @return 执行记录实体
     */
    ExecRecord selectOne(Long id);

    /**
     * 根据用例id获取所属的所有执行任务
     *
     * @param caseId 用例id
     * @return 任务列表
     */
    List<ExecRecord> getRecordListByCaseId(Long caseId);


    /**
     * testcase的list接口需要展示每个case有多少任务
     *
     * @param caseIds 用例id列表
     * @return 数量统计
     */
    List<RecordNumDto> getRecordNumByCaseIds(List<Long> caseIds);

    /**
     * 脑图更新执行任务，与统计数据有关
     *
     * @param record 任务实体
     */
    void update(ExecRecord record);

    /**
     * 编辑任务的基本属性，和统计数据无关
     *
     * @param record 任务实体
     * @return 任务id
     */
    Integer edit(ExecRecord record);

    /**
     * 删除任务
     *
     * @param recordId 执行任务id
     */
    void delete(Long recordId);

    /**
     * 批量删除执行任务
     *
     * @param recordIds 执行任务id列表
     */
    void batchDelete(List<Long> recordIds);
}
