package com.qa.manager.controller;

import io.swagger.annotations.*;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.qa.common.constant.ProjectChannelConstants;
import com.qa.manager.domain.request.cases.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qa.common.annotation.Log;
import com.qa.common.core.controller.BaseController;
import com.qa.common.core.domain.AjaxResult;
import com.qa.common.enums.BusinessType;
import com.qa.manager.domain.TestCase;
import com.qa.manager.service.ITestCaseService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

/**
 * 测试用例Controller
 *
 * @author jinrong.dong
 * @date 2025-03-22
 */
@Api(tags = "测试用例管理接口")
@RestController
@RequestMapping("/manager/case")
public class TestCaseController extends BaseController {
    @Autowired
    private ITestCaseService testCaseService;

    /**
     * 查询测试用例列表
     */
    @ApiOperation("查询测试用例列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bizId", value = "文件夹 ID，必须选择", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "用例标题", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "creator", value = "用例创建者", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "PlanId", value = "计划 ID", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "caseKeyWords", value = "用例关键字", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "beginTime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, dataType = "String", paramType = "query")
    })
    @PreAuthorize("@ss.hasPermi('manager:case:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam(value ="用例相关信息", required = true)  TestCase testCase
            ) {
        startPage();

        //beginTime: 2025-03-25 00:00:00
//        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.getGmtCreated());
        String beginTime = null;
        //endTime: 2025-03-30  23:59:59
//        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(testCase.getGmtCreated());
        String endTime = null;
        List<CaseListResp> list = testCaseService.getCaseList(
                new CaseQueryReq(0, testCase.getTitle(), testCase.getCreator(), testCase.getPlanId(), beginTime,
                        endTime, ProjectChannelConstants.CHANNEL, testCase.getBizId(), ProjectChannelConstants.PRODUCT_LINE_ID));
        return getDataTable(list);
    }

    /**
     * 导出测试用例列表
     */
    @ApiOperation("----废弃----导出测试用例列表")
    @ApiImplicitParam(name = "testCase", value = "测试用例对象", required = true, dataType = "TestCase", paramType = "body")
    @PreAuthorize("@ss.hasPermi('manager:case:export')")
    @Log(title = "测试用例", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TestCase testCase) {
        List<TestCase> list = testCaseService.selectTestCaseList(testCase);
        ExcelUtil<TestCase> util = new ExcelUtil<TestCase>(TestCase.class);
        util.exportExcel(response, list, "测试用例数据");
    }

    /**
     * 获取测试用例详细信息
     */
    @ApiOperation("----废弃----获取测试用例详细信息")
    @ApiImplicitParam(name = "id", value = "测试用例 ID", required = true, dataType = "Long", paramType = "path")
    @PreAuthorize("@ss.hasPermi('manager:case:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(testCaseService.selectTestCaseById(id));
    }

    /**
     * 新增测试用例
     */
    @ApiOperation("废弃----新增测试用例")
    @ApiImplicitParam(name = "testCase", value = "测试用例对象", required = true, dataType = "TestCase", paramType = "body")
    @PreAuthorize("@ss.hasPermi('manager:case:add')")
    @Log(title = "测试用例", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TestCase testCase) {
        return toAjax(testCaseService.insertTestCase(testCase));
    }

    /**
     * 修改测试用例
     */
    @ApiOperation("修改测试用例")
    @ApiImplicitParam(name = "testCase", value = "测试用例对象", required = true, dataType = "TestCase", paramType = "body")
    @PreAuthorize("@ss.hasPermi('manager:case:edit')")
    @Log(title = "测试用例", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TestCase testCase) {
        return toAjax(testCaseService.updateTestCase(testCase));
    }

    /**
     * 删除测试用例
     */
    @ApiOperation("删除测试用例")
    @ApiImplicitParam(name = "ids", value = "测试用例 ID 数组", required = true, dataType = "Long[]", paramType = "path")
    @PreAuthorize("@ss.hasPermi('manager:case:remove')")
    @Log(title = "测试用例", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        CaseDeleteReq request = new CaseDeleteReq();
        request.setId(ids[0]);
        request.validate();
        return success(testCaseService.deleteCase(request.getId()));
    }






    /**
     * 列表 - 创建或者复制用例
     *
     * @param request 请求体
     * @return 响应体
     */
    @ApiOperation("列表 - 创建或者复制用例")
    @ApiImplicitParam(name = "request", value = "用例创建请求对象", required = true, dataType = "CaseCreateReq", paramType = "body")
    @PreAuthorize("@ss.hasPermi('manager:case:edit')")
    @Log(title = "测试用例", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/create")
    public AjaxResult createOrCopyCase(@ApiParam(value ="创建用例", required = true) @RequestBody CaseCreateReq request) {
        request.setCreator(getUsername());
        request.setChannel(ProjectChannelConstants.CHANNEL);
        request.setProductLineId(ProjectChannelConstants.PRODUCT_LINE_ID);
        request.setCaseType(0);
        request.validate();
        return success(testCaseService.insertOrDuplicateCase(request));
    }

    /**
     * 列表 - 修改用例属性
     *
     * @param request 请求体
     * @return 响应体
     */
    @ApiOperation("列表 - 修改用例属性")
    @ApiImplicitParam(name = "request", value = "用例编辑请求对象", required = true, dataType = "CaseEditReq", paramType = "body")
    @PreAuthorize("@ss.hasPermi('manager:case:edit')")
    @Log(title = "测试用例", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/edit")
    public AjaxResult editCase(@ApiParam(value ="修改用例", required = true) @RequestBody CaseEditReq request) {
        request.setModifier(getUsername());
        request.setChannel(ProjectChannelConstants.CHANNEL);

        request.validate();
        return success(testCaseService.updateCase(request));
    }

    /**
     * 列表 - 删除用例
     *
     * @param id 用例id
     * @return 响应体
     */
    @ApiOperation("-----废弃----列表 - 删除用例")
//    @ApiImplicitParam(name = "request", value = "用例删除请求对象", required = true, dataType = "CaseDeleteReq", paramType = "body")
    @PreAuthorize("@ss.hasPermi('manager:case:remove')")
    @Log(title = "测试用例", businessType = BusinessType.DELETE)
    @PostMapping(value = "/delete")
    public AjaxResult deleteCase(@ApiParam(value ="删除用例", required = true) @PathVariable Long id) {
        CaseDeleteReq request = new CaseDeleteReq();
        request.setId(id);
        request.validate();
        return success(testCaseService.deleteCase(request.getId()));
    }

    /**
     * 列表 - 查看用例详情
     *
     * @param caseId 用例id
     * @return 响应体
     */
    @ApiOperation("列表 - 查看用例详情")
    @ApiImplicitParam(name = "caseId", value = "用例 ID", required = true, dataType = "Long", paramType = "query")
    @PreAuthorize("@ss.hasPermi('manager:case:list')")
    @GetMapping(value = "/detail")
    public AjaxResult getCaseDetail(@ApiParam(value ="用例id", required = true) @RequestParam @NotNull(message = "用例id为空") Long caseId) {
        return success(testCaseService.getCaseDetail(caseId));
    }

    /**
     * 配合list 筛选时获取所有创建人的列表
     *
     * @param caseType 用例类型
     * @param productLineId 业务线id
     * @return 响应体
     */
    @ApiOperation("配合 list 筛选时获取所有创建人的列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "caseType", value = "用例类型", required = true, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "productLineId", value = "业务线 ID", required = true, dataType = "Long", paramType = "query")
    })
    @PreAuthorize("@ss.hasPermi('manager:case:list')")
    @GetMapping(value = "/listCreators")
    public AjaxResult listCreators(@ApiParam(value ="用例类型", required = true) @RequestParam @NotNull(message = "用例类型为空") Integer caseType,
                                   @ApiParam(value ="业务线") @RequestParam @NotNull(message = "业务线为空") Long productLineId) {
        productLineId = ProjectChannelConstants.PRODUCT_LINE_ID;
        return success(testCaseService.listCreators(caseType, productLineId));
    }

    /**
     * 配合detail 修改圈选用例时统计的用例条目数据
     *
     * @param caseId 用例id
     * @param priority 优先级列表
     * @param resource 资源列表
     * @return 响应体
     */
    @ApiOperation("配合 detail 修改圈选用例时统计的用例条目数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "caseId", value = "用例 ID", required = true, dataType = "Long", paramType = "query"),
            @ApiImplicitParam(name = "priority", value = "优先级列表", required = true, dataType = "String[]", paramType = "query"),
            @ApiImplicitParam(name = "resource", value = "资源列表", required = true, dataType = "String[]", paramType = "query")
    })
    @PreAuthorize("@ss.hasPermi('manager:case:list')")
    @GetMapping(value = "/countByCondition")
    public AjaxResult getCountByCondition(@RequestParam @NotNull(message = "用例id为空") Long caseId,
                                          @RequestParam @NotNull(message = "圈选优先级为空") String[] priority,
                                          @RequestParam @NotNull(message = "圈选资源为空") String[] resource) {
        CaseConditionReq req = new CaseConditionReq(caseId, priority, resource);
        req.validate();

        return success(testCaseService.getCountByCondition(req));
    }

    /**
     * 脑图 - 获取上方用例概览信息
     *
     * @param id 用例id
     * @return 概览信息
     */
    @ApiOperation("脑图 - 获取上方用例概览信息")
    @ApiImplicitParam(name = "id", value = "用例 ID", required = true, dataType = "Long", paramType = "query")
    @PreAuthorize("@ss.hasPermi('manager:case:list')")
    @GetMapping(value = "/getCaseInfo")
    public AjaxResult getCaseGeneralInfo(@ApiParam("用例id")@RequestParam @NotNull(message = "用例id为空") Long id) {
        return success(testCaseService.getCaseGeneralInfo(id));
    }

}