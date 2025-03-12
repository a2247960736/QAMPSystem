package com.qa.manager.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.qa.common.constant.ProjConstants;
import com.qa.common.exception.ServiceException;
import com.qa.manager.domain.Vo.ProRequirementVo;
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
import com.qa.manager.domain.ProRequirement;
import com.qa.manager.service.IProRequirementService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * 需求管理Controller
 *
 * @author ruoyi
 * @date 2025-03-09
 */
@Api(tags = "需求管理接口", description = "提供需求管理的增删改查等操作")
@RestController
@RequestMapping("/manager/requirement")
public class ProRequirementController extends BaseController {
    @Autowired
    private IProRequirementService proRequirementService;

    /**
     * 查询需求管理列表
     */
    @ApiOperation(value = "查询需求管理列表", notes = "根据传入的需求信息查询需求列表，支持分页")
    @PreAuthorize("@ss.hasPermi('manager:requirement:list')")
    @GetMapping("/list")
    public TableDataInfo list(@ApiParam(value = "需求查询条件", required = false) ProRequirement proRequirement) {
        startPage();
        List<ProRequirementVo> list = proRequirementService.selectProRequirementVoList(proRequirement);
        return getDataTable(list);
    }

    /**
     * 导出需求管理列表
     */
    @ApiOperation(value = "导出需求管理列表", notes = "根据传入的需求信息导出需求列表到 Excel 文件")
    @PreAuthorize("@ss.hasPermi('manager:requirement:export')")
    @Log(title = "需求管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@ApiParam(value = "响应对象，用于输出 Excel 文件", required = true) HttpServletResponse response,
                       @ApiParam(value = "需求查询条件", required = false) ProRequirement proRequirement) {
        List<ProRequirement> list = proRequirementService.selectProRequirementList(proRequirement);
        ExcelUtil<ProRequirement> util = new ExcelUtil<ProRequirement>(ProRequirement.class);
        util.exportExcel(response, list, "需求管理数据");
    }

    /**
     * 获取需求管理详细信息
     */
    @ApiOperation(value = "获取需求管理详细信息", notes = "根据需求 ID 获取需求的详细信息")
    @PreAuthorize("@ss.hasPermi('manager:requirement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(value = "需求 ID", required = true) @PathVariable("id") Long id) {
        return success(proRequirementService.selectProRequirementById(id));
    }

    /**
     * 新增需求管理
     */
    @ApiOperation(value = "新增需求管理", notes = "新增一条需求信息，会检查业务 ID 是否唯一")
    @PreAuthorize("@ss.hasPermi('manager:requirement:add')")
    @Log(title = "需求管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam(value = "需求新增信息", required = true) @RequestBody ProRequirement proRequirement) {
        //查询业务ID是否唯一
        CheckReqIdIsUnique(proRequirement);
        return toAjax(proRequirementService.insertProRequirement(proRequirement));
    }

    /**
     * 修改需求管理
     */
    @ApiOperation(value = "修改需求管理", notes = "修改需求信息，若需求状态为已完成则禁止修改")
    @PreAuthorize("@ss.hasPermi('manager:requirement:edit')")
    @Log(title = "需求管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam(value = "需求修改信息", required = true) @RequestBody ProRequirement proRequirement) {
        //检查需求状态是否为已完成
        //1、获取数据库中需求当前的状态
        ProRequirement proRequirementFromDB = proRequirementService.selectProRequirementById(proRequirement.getId());

        String reqStatus = proRequirementFromDB.getStatus();
        if (reqStatus.equals(ProjConstants.STATUS_DONE)) {
            //当前需求已完成，禁止修改 如需修改请联系管理员

            throw new ServiceException("当前需求已完成，禁止修改！如需帮助请联系管理员");
        }
        return toAjax(proRequirementService.updateProRequirement(proRequirement));
    }

    //校验业务ID是否唯一
    private void CheckReqIdIsUnique(ProRequirement proRequirement) {

        String reqId = proRequirement.getReqId();
        ProRequirement proRequirementToReqId = new ProRequirement();
        proRequirementToReqId.setReqId(reqId);
        List<ProRequirement> list = proRequirementService.selectProRequirementList(proRequirementToReqId);
        if (list.size() > 0) {
            throw new ServiceException("业务标识已存在，请重新创建！");
        }
    }

    /**
     * 删除需求管理
     */
    @ApiOperation(value = "删除需求管理", notes = "根据需求 ID 数组批量删除需求信息")
    @PreAuthorize("@ss.hasPermi('manager:requirement:remove')")
    @Log(title = "需求管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(value = "需求 ID 数组", required = true) @PathVariable Long[] ids) {
        return toAjax(proRequirementService.deleteProRequirementByIds(ids));
    }
}