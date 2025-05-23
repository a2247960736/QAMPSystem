package com.qa.manager.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qa.common.exception.ServiceException;
import com.qa.manager.domain.Vo.ProProjectVo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.qa.common.annotation.Log;
import com.qa.common.core.controller.BaseController;
import com.qa.common.core.domain.AjaxResult;
import com.qa.common.enums.BusinessType;
import com.qa.manager.domain.ProProject;
import com.qa.manager.service.IProProjectService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

/**
 * 项目管理Controller
 *
 * @author jinrong.dong
 * @date 2025-03-09
 */
@Api(tags = "项目管理接口")
@RestController
@RequestMapping("/manager/project")
public class ProProjectController extends BaseController {

    @Autowired
    private IProProjectService proProjectService;

    /**
     * 查询项目管理列表
     */
    @ApiOperation("查询项目管理列表")
    @PreAuthorize("@ss.hasPermi('manager:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam("项目查询条件") ProProject proProject) {
        startPage();
        //查询项目信息
        List<ProProjectVo> list = proProjectService.selectProProjectVoList(proProject);
        //查询项目成员
        return getDataTable(list);
    }

    /**
     * 导出项目管理列表
     */
    @ApiOperation("导出项目管理列表")
    @PreAuthorize("@ss.hasPermi('manager:project:export')")
    @Log(title = "项目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@ApiParam("响应对象") HttpServletResponse response, @ApiParam("项目查询条件") ProProject proProject) {
        List<ProProject> list = proProjectService.selectProProjectList(proProject);
        ExcelUtil<ProProject> util = new ExcelUtil<ProProject>(ProProject.class);
        util.exportExcel(response, list, "项目管理数据");
    }

    /**
     * 获取项目管理详细信息
     */
    @ApiOperation("获取项目管理详细信息")
    @PreAuthorize("@ss.hasPermi('manager:project:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam("项目 ID") @PathVariable("id") Long id) {
        return success(proProjectService.selectProProjectById(id));
    }

    /**
     * 新增项目管理
     */

    @ApiOperation("新增项目管理")
    @PreAuthorize("@ss.hasPermi('manager:project:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam("项目信息") @RequestBody ProProject proProject) {
        CheckProjectIdIsUnique(proProject);
        return toAjax(proProjectService.insertProProject(proProject));
    }

    /**
     * 修改项目管理
     */
    @ApiOperation("修改项目管理")
    @PreAuthorize("@ss.hasPermi('manager:project:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam("项目信息") @RequestBody ProProject proProject) {
        return toAjax(proProjectService.updateProProject(proProject));
    }

    /**
     * 删除项目管理
     */
    @ApiOperation("删除项目管理")
    @PreAuthorize("@ss.hasPermi('manager:project:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam("项目 ID 数组") @PathVariable Long[] ids) {
        return toAjax(proProjectService.deleteProProjectByIds(ids));
    }

    //校验projectId是否唯一
    private void CheckProjectIdIsUnique(ProProject proProject) {
        //查询业务ID是否唯一
        String projectId = proProject.getProjectId();
        ProProject proProjectToProjectId = new ProProject();
        proProjectToProjectId.setProjectId(projectId);
        List<ProProject> list = proProjectService.selectProProjectList(proProjectToProjectId);
        if (list.size() > 0) {
            throw new ServiceException("项目 ID 已存在，请重新输入！");
        }
    }
}