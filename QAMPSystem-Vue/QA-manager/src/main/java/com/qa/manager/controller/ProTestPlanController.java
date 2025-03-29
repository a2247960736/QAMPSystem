package com.qa.manager.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.qa.manager.domain.Vo.ProTestPlanVo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qa.common.annotation.Log;
import com.qa.common.core.controller.BaseController;
import com.qa.common.core.domain.AjaxResult;
import com.qa.common.enums.BusinessType;
import com.qa.manager.domain.ProTestPlan;
import com.qa.manager.service.IProTestPlanService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

/**
 * 测试计划Controller
 * 
 * @author jinrong.dong
 * @date 2025-03-14
 */
@RestController
@RequestMapping("/manager/plan")
public class ProTestPlanController extends BaseController
{
    @Autowired
    private IProTestPlanService proTestPlanService;

    /**
     * 查询测试计划列表
     */
    @PreAuthorize("@ss.hasPermi('manager:plan:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProTestPlan proTestPlan)
    {
        startPage();
        List<ProTestPlanVo> list = proTestPlanService.selectProTestPlanListVo(proTestPlan);
        return getDataTable(list);
    }

    /**
     * 导出测试计划列表
     */
    @PreAuthorize("@ss.hasPermi('manager:plan:export')")
    @Log(title = "测试计划", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProTestPlan proTestPlan)
    {
        List<ProTestPlan> list = proTestPlanService.selectProTestPlanList(proTestPlan);
        ExcelUtil<ProTestPlan> util = new ExcelUtil<ProTestPlan>(ProTestPlan.class);
        util.exportExcel(response, list, "测试计划数据");
    }

    /**
     * 获取测试计划详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:plan:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(proTestPlanService.selectProTestPlanById(id));
    }

    /**
     * 新增测试计划
     */
    @PreAuthorize("@ss.hasPermi('manager:plan:add')")
    @Log(title = "测试计划", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProTestPlan proTestPlan)
    {
        return toAjax(proTestPlanService.insertProTestPlan(proTestPlan));
    }

    /**
     * 修改测试计划
     */
    @PreAuthorize("@ss.hasPermi('manager:plan:edit')")
    @Log(title = "测试计划", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProTestPlan proTestPlan)
    {
        return toAjax(proTestPlanService.updateProTestPlan(proTestPlan));
    }

    /**
     * 删除测试计划
     */
    @PreAuthorize("@ss.hasPermi('manager:plan:remove')")
    @Log(title = "测试计划", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(proTestPlanService.deleteProTestPlanByIds(ids));
    }


}
