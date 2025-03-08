package com.ruoyi.qampSystem.service;

import java.util.List;
import com.ruoyi.qampSystem.domain.TbDish;

/**
 * 菜品管理Service接口
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
public interface ITbDishService 
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
     * 批量删除菜品管理
     * 
     * @param ids 需要删除的菜品管理主键集合
     * @return 结果
     */
    public int deleteTbDishByIds(Long[] ids);

    /**
     * 删除菜品管理信息
     * 
     * @param id 菜品管理主键
     * @return 结果
     */
    public int deleteTbDishById(Long id);
}
