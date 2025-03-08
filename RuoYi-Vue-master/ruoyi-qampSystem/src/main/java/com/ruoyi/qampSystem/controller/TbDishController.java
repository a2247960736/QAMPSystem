package com.ruoyi.qampSystem.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.qampSystem.domain.TbDish;
import com.ruoyi.qampSystem.service.ITbDishService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 菜品管理Controller
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@RestController
@RequestMapping("/qampSystem/dish")
public class TbDishController extends BaseController
{
    @Autowired
    private ITbDishService tbDishService;

    /**
     * 查询菜品管理列表
     */
    @PreAuthorize("@ss.hasPermi('qampSystem:dish:list')")
    @GetMapping("/list")
    public TableDataInfo list(TbDish tbDish)
    {
        startPage();
        List<TbDish> list = tbDishService.selectTbDishList(tbDish);
        return getDataTable(list);
    }

    /**
     * 导出菜品管理列表
     */
    @PreAuthorize("@ss.hasPermi('qampSystem:dish:export')")
    @Log(title = "菜品管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TbDish tbDish)
    {
        List<TbDish> list = tbDishService.selectTbDishList(tbDish);
        ExcelUtil<TbDish> util = new ExcelUtil<TbDish>(TbDish.class);
        util.exportExcel(response, list, "菜品管理数据");

    }

    /**
     * 获取菜品管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('qampSystem:dish:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tbDishService.selectTbDishById(id));
    }

    /**
     * 新增菜品管理
     */
    @PreAuthorize("@ss.hasPermi('qampSystem:dish:add')")
    @Log(title = "菜品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TbDish tbDish)
    {
        return toAjax(tbDishService.insertTbDish(tbDish));
    }

    /**
     * 修改菜品管理
     */
    @PreAuthorize("@ss.hasPermi('qampSystem:dish:edit')")
    @Log(title = "菜品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TbDish tbDish)
    {
        return toAjax(tbDishService.updateTbDish(tbDish));
    }

    /**
     * 删除菜品管理
     */
    @PreAuthorize("@ss.hasPermi('qampSystem:dish:remove')")
    @Log(title = "菜品管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tbDishService.deleteTbDishByIds(ids));
    }
}
