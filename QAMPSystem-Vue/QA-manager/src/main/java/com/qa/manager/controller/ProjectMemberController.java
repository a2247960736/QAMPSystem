package com.qa.manager.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qa.common.exception.ServiceException;
import com.qa.manager.domain.Vo.ProjectMemberDTO;
import com.qa.manager.domain.Vo.ProjectMemberVo;
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
import com.qa.manager.domain.ProjectMember;
import com.qa.manager.service.IProjectMemberService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

/**
 * 项目成员Controller
 *
 * @author jinrong.dong
 * @date 2025-03-11
 */
@Api(tags = "项目成员管理接口", description = "提供项目成员的增删改查等操作")
@RestController
@RequestMapping("/manager/member")
public class ProjectMemberController extends BaseController {
    @Autowired
    private IProjectMemberService projectMemberService;

    /**
     * 查询项目成员列表
     */
    @ApiOperation(value = "查询项目成员列表", notes = "根据传入的项目成员信息查询项目成员列表，支持分页")
    @PreAuthorize("@ss.hasPermi('manager:member:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam(value = "项目成员查询条件", required = false) ProjectMember projectMember) {
        startPage();
        List<ProjectMemberVo> list = projectMemberService.selectProjectMemberVoList(projectMember);
        return getDataTable(list);
    }

    /**
     * 导出项目成员列表
     */
    @ApiOperation(value = "导出项目成员列表", notes = "根据传入的项目成员信息导出项目成员列表到 Excel 文件")
    @PreAuthorize("@ss.hasPermi('manager:member:export')")
    @Log(title = "项目成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@ApiParam(value = "响应对象，用于输出 Excel 文件", required = true) HttpServletResponse response,
                       @ApiParam(value = "项目成员查询条件", required = false) ProjectMember projectMember) {
        List<ProjectMember> list = projectMemberService.selectProjectMemberList(projectMember);
        ExcelUtil<ProjectMember> util = new ExcelUtil<ProjectMember>(ProjectMember.class);
        util.exportExcel(response, list, "项目成员数据");
    }

    /**
     * 获取项目成员详细信息
     */
    @ApiOperation(value = "获取项目成员详细信息", notes = "根据项目成员 ID 获取项目成员的详细信息")
    @PreAuthorize("@ss.hasPermi('manager:member:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(value = "项目成员 ID", required = true) @PathVariable("id") Long id) {
        return success(projectMemberService.selectProjectMemberById(id));
    }

    /**
     * 新增项目成员
     */
    @ApiOperation(value = "新增项目成员", notes = "批量新增项目成员，会检查成员是否已加入项目")
    @PreAuthorize("@ss.hasPermi('manager:member:add')")
    @Log(title = "项目成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam(value = "项目成员新增信息，包含项目 ID 和成员 ID 列表", required = true) @RequestBody ProjectMemberDTO projectMemberDTO) {
        checkUserNotInProject(projectMemberDTO);
        return toAjax(projectMemberService.batchInsertProjectMembers(projectMemberDTO.getProjectId(), projectMemberDTO.getUserIds()));
    }

    private void checkUserNotInProject(ProjectMemberDTO projectMemberDTO) {
        //检验成员是否已经加入该项目
        //1、获取项目id
        Long projectId = projectMemberDTO.getProjectId();
        //2、获取成员id
        List<Long> userIds = projectMemberDTO.getUserIds();
        //3、调用service方法，判断成员是否已经加入该项目
        ProjectMember projectMemberToCheckUser = new ProjectMember();
        projectMemberToCheckUser.setProjectId(projectId);
        for (Long userId : userIds) {
            projectMemberToCheckUser.setUserId(userId);
            List<ProjectMember> list = projectMemberService.selectProjectMemberList(projectMemberToCheckUser);
            if (list.size() > 0) {
                throw new ServiceException("所选择成员中有成员已经加入该项目，请重新选择！");
            }
        }
    }

    /**
     * 修改项目成员
     */
    @ApiOperation(value = "修改项目成员信息", notes = "根据传入的项目成员信息修改项目成员的相关信息")
    @PreAuthorize("@ss.hasPermi('manager:member:edit')")
    @Log(title = "项目成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam(value = "项目成员修改信息", required = true) @RequestBody ProjectMember projectMember) {
        return toAjax(projectMemberService.updateProjectMember(projectMember));
    }

    /**
     * 删除项目成员
     */
    @ApiOperation(value = "删除项目成员", notes = "根据项目成员 ID 数组批量删除项目成员")
    @PreAuthorize("@ss.hasPermi('manager:member:remove')")
    @Log(title = "项目成员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(value = "项目成员 ID 数组", required = true) @PathVariable Long[] ids) {
        return toAjax(projectMemberService.deleteProjectMemberByIds(ids));
    }

    //查询
}