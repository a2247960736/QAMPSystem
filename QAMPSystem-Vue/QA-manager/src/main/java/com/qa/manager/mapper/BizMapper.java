package com.qa.manager.mapper;

import java.util.List;
import com.qa.manager.domain.Biz;
import org.apache.ibatis.annotations.Param;

/**
 * 文件夹Mapper接口
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
public interface BizMapper 
{
    /**
     * 查询文件夹
     * 
     * @param id 文件夹主键
     * @return 文件夹
     */
    public Biz selectBizById(Long id);

    /**
     * 查询文件夹列表
     * 
     * @param biz 文件夹
     * @return 文件夹集合
     */
    public List<Biz> selectBizList(Biz biz);

    /**
     * 新增文件夹
     * 
     * @param biz 文件夹
     * @return 结果
     */
    public int insertBiz(Biz biz);

    /**
     * 修改文件夹
     * 
     * @param biz 文件夹
     * @return 结果
     */
    public int updateBiz(Biz biz);

    /**
     * 删除文件夹
     * 
     * @param id 文件夹主键
     * @return 结果
     */
    public int deleteBizById(Long id);

    /**
     * 批量删除文件夹
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteBizByIds(Long[] ids);

    /**
     * 根据渠道和业务线获取文件夹
     *
     * @param productLineId 业务线id
     * @param channel 渠道
     * @return 文件夹实体
     */
    Biz selectOne(@Param("productLineId")Long productLineId, @Param("channel")Integer channel);

    /**
     * 更新文件夹树内容
     *
     * @param productLineId 业务线id
     * @param content 文件夹内容
     * @param channel 渠道
     */
    void updateContent(@Param("productLineId")Long productLineId, @Param("content")String content, @Param("channel")Integer channel);

    /**
     * 通过主键修改文件夹
     *
     * @param biz 文件夹实体
     * @return bizId
     */
    void update(Biz biz);
}
