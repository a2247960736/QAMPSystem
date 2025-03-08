package com.ruoyi.qampSystem.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.qampSystem.domain.DishFlavor;
import com.ruoyi.qampSystem.mapper.TbDishMapper;
import com.ruoyi.qampSystem.domain.TbDish;
import com.ruoyi.qampSystem.service.ITbDishService;

/**
 * 菜品管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-15
 */
@Service
public class TbDishServiceImpl implements ITbDishService 
{
    @Autowired
    private TbDishMapper tbDishMapper;

    /**
     * 查询菜品管理
     * 
     * @param id 菜品管理主键
     * @return 菜品管理
     */
    @Override
    public TbDish selectTbDishById(Long id)
    {
        return tbDishMapper.selectTbDishById(id);
    }

    /**
     * 查询菜品管理列表
     * 
     * @param tbDish 菜品管理
     * @return 菜品管理
     */
    @Override
    public List<TbDish> selectTbDishList(TbDish tbDish)
    {
        return tbDishMapper.selectTbDishList(tbDish);
    }

    /**
     * 新增菜品管理
     * 
     * @param tbDish 菜品管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTbDish(TbDish tbDish)
    {
        tbDish.setCreateTime(DateUtils.getNowDate());
        int rows = tbDishMapper.insertTbDish(tbDish);
        insertDishFlavor(tbDish);
        return rows;
    }

    /**
     * 修改菜品管理
     * 
     * @param tbDish 菜品管理
     * @return 结果
     */
    @Transactional
    @Override
    public int updateTbDish(TbDish tbDish)
    {
        tbDish.setUpdateTime(DateUtils.getNowDate());
        tbDishMapper.deleteDishFlavorByDishId(tbDish.getId());
        insertDishFlavor(tbDish);
        return tbDishMapper.updateTbDish(tbDish);
    }

    /**
     * 批量删除菜品管理
     * 
     * @param ids 需要删除的菜品管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTbDishByIds(Long[] ids)
    {
        tbDishMapper.deleteDishFlavorByDishIds(ids);
        return tbDishMapper.deleteTbDishByIds(ids);
    }

    /**
     * 删除菜品管理信息
     * 
     * @param id 菜品管理主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTbDishById(Long id)
    {
        tbDishMapper.deleteDishFlavorByDishId(id);
        return tbDishMapper.deleteTbDishById(id);
    }

    /**
     * 新增菜品口味关系信息
     * 
     * @param tbDish 菜品管理对象
     */
    public void insertDishFlavor(TbDish tbDish)
    {
        List<DishFlavor> dishFlavorList = tbDish.getDishFlavorList();
        Long id = tbDish.getId();
        if (StringUtils.isNotNull(dishFlavorList))
        {
            List<DishFlavor> list = new ArrayList<DishFlavor>();
            for (DishFlavor dishFlavor : dishFlavorList)
            {
                dishFlavor.setDishId(id);
                list.add(dishFlavor);
            }
            if (list.size() > 0)
            {
                tbDishMapper.batchDishFlavor(list);
            }
        }
    }
}
