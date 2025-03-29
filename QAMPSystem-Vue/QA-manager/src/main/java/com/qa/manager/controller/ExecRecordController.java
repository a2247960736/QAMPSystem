package com.qa.manager.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.qa.manager.domain.request.record.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qa.common.annotation.Log;
import com.qa.common.core.controller.BaseController;
import com.qa.common.core.domain.AjaxResult;
import com.qa.common.enums.BusinessType;
import com.qa.manager.service.IExecRecordService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

/**
 * 用例执行记录 Controller
 *
 * @author jinrong.dong
 * @date 2025 - 03 - 23
 */
@Api(tags = "用例执行记录管理")
@RestController
@RequestMapping("/manager/record")
public class ExecRecordController extends BaseController {
    @Autowired
    private IExecRecordService execRecordService;

    /**
     * 列表 - 根据用例下获取所有执行任务
     *
     * @param caseId 用例集 id
     * @return 响应体
     */
    @ApiOperation(value = "根据用例集 ID 获取所有执行任务", notes = "根据指定的用例集 ID 获取对应的所有执行任务列表")
    @PreAuthorize("@ss.hasPermi('manager:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam(value = "用例集 ID，不能为空", required = true) @RequestParam @NotNull(message = "用例 id 为空") Long caseId) {
        startPage();
        List<RecordListResp> list = execRecordService.getListByCaseId(caseId);
        return getDataTable(list);
    }

    /**
     * 导出用例执行记录列表
     */
    @ApiOperation(value = "---废弃---导出用例执行记录列表", notes = "将符合条件的用例执行记录列表导出为 Excel 文件")
    @PreAuthorize("@ss.hasPermi('manager:record:export')")
    @Log(title = "用例执行记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ExecRecord execRecord) {
        List<ExecRecord> list = execRecordService.selectExecRecordList(execRecord);
        ExcelUtil<ExecRecord> util = new ExcelUtil<ExecRecord>(ExecRecord.class);
        util.exportExcel(response, list, "用例执行记录数据");
    }

    /**
     * 获取用例执行记录详细信息
     */
    @ApiOperation(value = "---废弃---详细信息", notes = "根据用例执行记录的 ID 获取其详细信息")
    @PreAuthorize("@ss.hasPermi('manager:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(value = "用例执行记录的 ID", required = true) @PathVariable("id") Long id) {
        return success(execRecordService.selectExecRecordById(id));
    }

    /**
     * 新增用例执行记录
     */
    @ApiOperation(value = "---废弃---新增用例执行记录", notes = "新增一条用例执行记录信息")
    @PreAuthorize("@ss.hasPermi('manager:record:add')")
    @Log(title = "用例执行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam(value = "用例执行记录实体对象", required = true) @RequestBody ExecRecord execRecord) {
        return toAjax(execRecordService.insertExecRecord(execRecord));
    }

    /**
     * 修改用例执行记录
     */
    @ApiOperation(value = "---废弃---修改用例执行记录", notes = "根据传入的用例执行记录信息修改已有的记录")
    @PreAuthorize("@ss.hasPermi('manager:record:edit')")
    @Log(title = "用例执行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam(value = "用例执行记录实体对象，包含要修改的信息", required = true) @RequestBody ExecRecord execRecord) {
        return toAjax(execRecordService.updateExecRecord(execRecord));
    }

    /**
     * 删除用例执行记录
     */
    @ApiOperation(value = "---废弃---删除用例执行记录", notes = "根据传入的用例执行记录 ID 数组删除对应的记录")
    @PreAuthorize("@ss.hasPermi('manager:record:remove')")
    @Log(title = "用例执行记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(value = "用例执行记录的 ID 数组", required = true) @PathVariable Long[] ids) {
        return toAjax(execRecordService.deleteExecRecordByIds(ids));
    }



    /**
     * 列表 - 新增执行任务
     *
     * @param req 前端传参
     * @return 响应体
     */
    @ApiOperation(value = "新增执行任务", notes = "在列表中新增一条执行任务记录")
    @PreAuthorize("@ss.hasPermi('manager:record:add')")
    @Log(title = "用例执行记录", businessType = BusinessType.INSERT)
    @PostMapping(value = "/create")
    public AjaxResult createRecord(@ApiParam(value = "新增执行任务的请求参数对象", required = true) @RequestBody RecordAddReq req) {
        req.setCreator(getUsername());
        req.validate();
        return success(execRecordService.addRecord(req));
    }

    /**
     * 列表 - 修改执行任务
     *
     * @param req 请求体
     * @return 响应体
     */
    @ApiOperation(value = "修改执行任务", notes = "在列表中修改已有的执行任务记录")
    @PreAuthorize("@ss.hasPermi('manager:record:edit')")
    @Log(title = "用例执行记录", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public AjaxResult editRecord(@ApiParam(value = "修改执行任务的请求参数对象", required = true) @RequestBody RecordUpdateReq req) {
        req.setModifier(getUsername());
        req.validate();
        execRecordService.editRecord(req);
        return success();
    }

    /**
     * 列表 - 删除执行任务
     *
     * @param req 请求体
     * @return 响应体
     */
    @ApiOperation(value = "删除执行任务", notes = "在列表中删除指定的执行任务记录")
    @PreAuthorize("@ss.hasPermi('manager:record:remove')")
    @Log(title = "用例执行记录", businessType = BusinessType.DELETE)
    @PostMapping(value = "/delete")
    public AjaxResult deleteRecord(@ApiParam(value = "删除执行任务的请求参数对象", required = true) @RequestBody RecordDeleteReq req) {
        req.validate();
        execRecordService.delete(req.getId());
        return success("删除成功");
    }

    /**
     * 脑图 - 清理 json 中所有的执行记录
     *
     * @param req 请求体
     * @return 响应体
     */
    @ApiOperation(value = "脑图清理执行记录", notes = "清理脑图 JSON 中所有的执行记录")
    @PreAuthorize("@ss.hasPermi('manager:record:remove')")
    @Log(title = "用例执行记录", businessType = BusinessType.DELETE)
    @PostMapping(value = "/clear")
    public AjaxResult clearRecord(@ApiParam(value = "清理执行记录的请求参数对象", required = true) @RequestBody RecordWsClearReq req) {
        req.validate();
        return success(execRecordService.wsClearRecord(req));
    }

    /**
     * 脑图 - 获取该任务用例上方的统计信息
     *
     * @param id 执行任务 id
     * @return 响应体
     */
    @ApiOperation(value = "获取任务用例统计信息", notes = "获取指定执行任务用例上方的统计信息")
    @PreAuthorize("@ss.hasPermi('manager:record:list')")
    @GetMapping(value = "/getRecordInfo")
    public AjaxResult getRecordGeneralInfo(@ApiParam(value = "执行任务的 ID，不能为空", required = true) @RequestParam @NotNull(message = "任务 id 为空") Long id) {
        return success(execRecordService.getGeneralInfo(id));
    }

    /**
     * 列表 - 查看执行计划详情
     *
     * @param recordId 记录id
     * @return 响应体
     */
    @ApiOperation("列表 - 查看执行计划详情")
    @ApiImplicitParam(name = "caseId", value = "用例 ID", required = true, dataType = "Long", paramType = "query")
    @PreAuthorize("@ss.hasPermi('manager:case:list')")
    @GetMapping(value = "/detail")
    public AjaxResult getRecordTail(@ApiParam(value ="记录id", required = true) @RequestParam @NotNull(message = "记录id为空") Long recordId
            , @ApiParam(value ="用例id", required = true) @RequestParam @NotNull(message = "用例id为空") Long caseId) {
        return success(execRecordService.getCaseDetail(recordId,caseId));
    }


    /**
     * 更新执行任务
     * req.setExecutors(getUsername());
     *         req.validate();
     * @param req 请求体
     * @return 响应体
     */
    @ApiOperation(value = "更新执行任务", notes = "更新当前的执行任务记录")
    @PreAuthorize("@ss.hasPermi('manager:record:edit')")
    @Log(title = "用例执行记录", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/updateRecord")
    public AjaxResult updateRecord(@ApiParam(value = "修改执行任务的请求参数对象", required = true) @RequestBody RecordUpdateReq req) {
//        RecordUpdateReq req = new RecordUpdateReq();
//        req.setId(recordId);
//        req.setCaseId(caseId);
//        req.setCaseContent(caseContent);
        req.setExecutors(getUsername());

        execRecordService.updateRecord(req);
        return success();
    }
}