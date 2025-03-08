package com.ruoyi.qampSystem.mapper;

import java.util.List;
import com.ruoyi.qampSystem.domain.TbDish;
import com.ruoyi.qampSystem.domain.DishFlavor;

/**
 * 菜品管理Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public interface TbDishMapper 
{
    /**
     * 查询菜品管理
     * 
     * @param id 菜品管理主键
     * @return 菜品管理
     */
    public TbDish selectTbDishById(Long id);

    /**
     * 查询菜品管理列表
     * 
     * @param tbDish 菜品管理
     * @return 菜品管理集合
     */
    public List<TbDish> selectTbDishList(TbDish tbDish);

    /**
     * 新增菜品管理
     * 
     * @param tbDish 菜品管理
     * @return 结果
     */
    public int insertTbDish(TbDish tbDish);

    /**
     * 修改菜品管理
     * 
     * @param tbDish 菜品管理
     * @return 结果
     */
    public int updateTbDish(TbDish tbDish);

    /**
     * 删除菜品管理
     * 
     * @param id 菜品管理主键
     * @return 结果
     */
    public int deleteTbDishById(Long id);

    /**
     * 批量删除菜品管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbDishByIds(Long[] ids);

    /**
     * 批量删除菜品口味关系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDishFlavorByDishIds(Long[] ids);
    
    /**
     * 批量新增菜品口味关系
     * 
     * @param dishFlavorList 菜品口味关系列表
     * @return 结果
     */
    public int batchDishFlavor(List<DishFlavor> dishFlavorList);
    

    /**
     * 通过菜品管理主键删除菜品口味关系信息
     * 
     * @param id 菜品管理ID
     * @return 结果
     */
    public int deleteDishFlavorByDishId(Long id);
}
