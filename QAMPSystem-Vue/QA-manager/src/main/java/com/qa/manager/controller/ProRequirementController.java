package com.qa.manager.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

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

/**
 * 需求管理Controller
 * 
 * @author ruoyi
 * @date 2025-03-09
 */
@RestController
@RequestMapping("/manager/requirement")
public class ProRequirementController extends BaseController
{
    @Autowired
    private IProRequirementService proRequirementService;

    /**
     * 查询需求管理列表
     */
    @PreAuthorize("@ss.hasPermi('manager:requirement:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProRequirement proRequirement)
    {
        startPage();
        List<ProRequirementVo> list = proRequirementService.selectProRequirementVoList(proRequirement);
        return getDataTable(list);
    }

    /**
     * 导出需求管理列表
     */
    @PreAuthorize("@ss.hasPermi('manager:requirement:export')")
    @Log(title = "需求管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProRequirement proRequirement)
    {
        List<ProRequirement> list = proRequirementService.selectProRequirementList(proRequirement);
        ExcelUtil<ProRequirement> util = new ExcelUtil<ProRequirement>(ProRequirement.class);
        util.exportExcel(response, list, "需求管理数据");
    }

    /**
     * 获取需求管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('manager:requirement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(proRequirementService.selectProRequirementById(id));
    }

    /**
     * 新增需求管理
     */
    @PreAuthorize("@ss.hasPermi('manager:requirement:add')")
    @Log(title = "需求管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProRequirement proRequirement)
    {
        //查询业务ID是否唯一
        CheckReqIdIsUnique(proRequirement);
        return toAjax(proRequirementService.insertProRequirement(proRequirement));
    }

    /**
     * 修改需求管理
     */
    @PreAuthorize("@ss.hasPermi('manager:requirement:edit')")
    @Log(title = "需求管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProRequirement proRequirement)
    {
        return toAjax(proRequirementService.updateProRequirement(proRequirement));
    }

    //校验业务ID是否唯一
    private void CheckReqIdIsUnique(ProRequirement proRequirement) {

        String reqId = proRequirement.getReqId();
        ProRequirement proRequirementToReqId = new ProRequirement();
        proRequirementToReqId.setReqId(reqId);
        List<ProRequirement> list = proRequirementService.selectProRequirementList(proRequirementToReqId);
        if(list.size()>0){
            throw  new ServiceException("业务Id已存在，请重新创建！");
        }
    }

    /**
     * 删除需求管理
     */
    @PreAuthorize("@ss.hasPermi('manager:requirement:remove')")
    @Log(title = "需求管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(proRequirementService.deleteProRequirementByIds(ids));
    }
}
