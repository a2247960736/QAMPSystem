package com.qa.manager.service;

import java.util.List;
import com.qa.manager.domain.Biz;
import com.qa.manager.domain.Dto.DirNodeDto;
import com.qa.manager.domain.request.dir.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件夹Service接口
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
public interface IBizService 
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
     * 批量删除文件夹
     * 
     * @param ids 需要删除的文件夹主键集合
     * @return 结果
     */
    public int deleteBizByIds(Long[] ids);

    /**
     * 删除文件夹信息
     * 
     * @param id 文件夹主键
     * @return 结果
     */
    public int deleteBizById(Long id);


    /**
     * 获取指定业务线和渠道下的目录树
     *
     * @param productLineId 业务线id
     * @param channel 渠道
     * @return 目录树的根节点
     */
    public DirNodeDto getDirTree(Long productLineId, Integer channel);

    /**
     * 新增文件夹
     *
     * @param request 包含创建文件夹所需信息的请求体
     * @return 包含新文件夹信息的DirNodeDto对象
     */
    public DirNodeDto addDir(DirCreateReq request);


    /**
     * 重命名文件夹
     *
     * @param request 包含重命名所需信息的请求体
     * @return 包含重命名后文件夹信息的DirNodeDto对象
     */
    public DirNodeDto renameDir(DirRenameReq request);

    /**
     * 删除文件夹
     *
     * @param request 包含删除所需信息的请求体
     * @return 包含删除后目录树信息的DirNodeDto对象
     */
    public DirNodeDto delDir(DirDeleteReq request);

    /**
     * 根据业务id和根节点查找目录节点
     *
     * @param bizId 业务id
     * @param root 根节点
     * @return 找到的目录节点，若未找到则返回null
     */
    public DirNodeDto getDir(String bizId, DirNodeDto root);

    /**
     * 获取包含所有测试用例的目录树响应对象
     *
     * @param root 目录树的根节点
     * @return 包含所有测试用例的目录树响应对象
     */
    public DirTreeResp getAllCaseDir(DirNodeDto root);

    /**
     * 获取指定业务线、业务id和渠道下的测试用例id列表
     *
     * @param productLineId 业务线id
     * @param bizId 业务id
     * @param channel 渠道
     * @return 测试用例id列表，若未找到则返回null
     */
    public List<Long> getCaseIds(Long productLineId, String bizId, Integer channel);

    /**
     * 移动文件夹
     *
     * @param req 包含移动所需信息的请求体
     * @return 移动是否成功的标志
     */
    public boolean moveDir(DirMoveReq req);
}
