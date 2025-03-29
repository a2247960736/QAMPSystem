package com.qa.manager.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.qa.common.constant.BizConstant;
import com.qa.common.constant.ProjectChannelConstants;
import com.qa.common.constant.enums.StatusCode;
import com.qa.common.exception.ServiceException;
import com.qa.manager.domain.Dto.DirNodeDto;
import com.qa.manager.domain.request.dir.*;
import com.qa.manager.mapper.TestCaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qa.manager.mapper.BizMapper;
import com.qa.manager.domain.Biz;
import com.qa.manager.service.IBizService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件夹Service业务层处理
 * 
 * @author jinrong.dong
 * @date 2025-03-22
 */
@Service
public class BizServiceImpl implements IBizService 
{
    @Autowired
    private BizMapper bizMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    /**
     * 查询文件夹
     * 
     * @param id 文件夹主键
     * @return 文件夹
     */
    @Override
    public Biz selectBizById(Long id)
    {
        return bizMapper.selectBizById(id);
    }

    /**
     * 查询文件夹列表
     * 
     * @param biz 文件夹
     * @return 文件夹
     */
    @Override
    public List<Biz> selectBizList(Biz biz)
    {
        return bizMapper.selectBizList(biz);
    }

    /**
     * 新增文件夹
     * 
     * @param biz 文件夹
     * @return 结果
     */
    @Override
    public int insertBiz(Biz biz)
    {
        return bizMapper.insertBiz(biz);
    }

    /**
     * 修改文件夹
     * 
     * @param biz 文件夹
     * @return 结果
     */
    @Override
    public int updateBiz(Biz biz)
    {
        return bizMapper.updateBiz(biz);
    }

    /**
     * 批量删除文件夹
     * 
     * @param ids 需要删除的文件夹主键
     * @return 结果
     */
    @Override
    public int deleteBizByIds(Long[] ids)
    {
        return bizMapper.deleteBizByIds(ids);
    }

    /**
     * 删除文件夹信息
     * 
     * @param id 文件夹主键
     * @return 结果
     */
    @Override
    public int deleteBizById(Long id)
    {
        return bizMapper.deleteBizById(id);
    }


    /**
     * 获取指定业务线和渠道下的目录树
     *
     * @param productLineId 业务线id
     * @param channel 渠道
     * @return 目录树的根节点
     */
    @Override
    public DirNodeDto getDirTree(Long productLineId, Integer channel) {
        // 从业务表中查询指定业务线和渠道的记录
        Biz dbBiz = bizMapper.selectOne(productLineId, channel);
        // 如果有记录，那么就直接返回解析后的目录树
        if (dbBiz != null) {
            return JSONObject.parseObject(dbBiz.getContent(), DirNodeDto.class);
        }

        // 如果没有记录，则会自动生成一个目录树
        DirNodeDto root = new DirNodeDto();
        root.setId("root");
        root.setText("主文件夹");

        // 获取指定业务线和渠道下的测试用例id集合
        Set<String> ids = testCaseMapper.findCaseIdsInBiz(productLineId, channel);

        // 创建一个未分类用例集节点
        DirNodeDto child = new DirNodeDto();
        child.setId("-1");
        child.setParentId(root.getId());
        child.setText("未分类用例集");
        child.setCaseIds(ids);
        // 将未分类用例集节点添加到根节点的子节点列表中
        root.getChildren().add(child);

        // 创建一个新的业务记录
        Biz biz = new Biz();
        biz.setProductLineId(productLineId);
        biz.setChannel(channel);
        biz.setContent(JSONObject.toJSONString(root));
        // 将新记录插入业务表
        bizMapper.insertBiz(biz);
        // 将未分类用例集节点的测试用例id添加到根节点的测试用例id集合中
        root.getCaseIds().addAll(child.getCaseIds());
        return root;
    }


    /**
     * 新增文件夹
     *
     * @param request 包含创建文件夹所需信息的请求体
     * @return 包含新文件夹信息的DirNodeDto对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirNodeDto addDir(DirCreateReq request) {
        // 获取指定业务线和渠道下的目录树
        DirNodeDto root = getDirTree(request.getProductLineId(), request.getChannel());
        // 检查同级节点下是否存在相同名字的子节点
        checkNodeExists(request.getText(), request.getParentId(), root);
        // 根据父节点id获取父节点
        DirNodeDto dir = getDir(request.getParentId(), root);
        if (dir == null) {
            // 若父节点为空，抛出异常
            throw new ServiceException("目录节点获取为空" + StatusCode.INTERNAL_ERROR.getMsg() , StatusCode.INTERNAL_ERROR.getStatus());
        }

        // 获取父节点的子节点列表
        List<DirNodeDto> children = dir.getChildren();
        // 创建新的文件夹节点
        DirNodeDto newDir = new DirNodeDto();
        newDir.setId(UUID.randomUUID().toString().substring(0, 8));
        newDir.setText(request.getText());
        newDir.setParentId(dir.getId());
        // 将新节点添加到父节点的子节点列表中
        children.add(newDir);

        // 更新业务表中的目录树信息
        bizMapper.updateContent(ProjectChannelConstants.PRODUCT_LINE_ID, JSONObject.toJSONString(root), ProjectChannelConstants.CHANNEL);
        return root;
    }

    /**
     * 重命名文件夹
     *
     * @param request 包含重命名所需信息的请求体
     * @return 包含重命名后文件夹信息的DirNodeDto对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public DirNodeDto renameDir(DirRenameReq request) {
        // 获取指定业务线和渠道下的目录树
        DirNodeDto dirTree = getDirTree(request.getProductLineId(), request.getChannel());
        if (!BizConstant.ROOT_BIZ_ID.equalsIgnoreCase(request.getId())) {
            // 若不是根节点，获取其父节点id
            String parentId = getParentIdWithRecursion(request.getId(), dirTree);
            if (null != parentId) {
                // 检查同级节点下是否存在相同名字的子节点
                checkNodeExists(request.getText(), parentId, dirTree);
            }
        }

        // 再次获取指定业务线和渠道下的目录树
        DirNodeDto root = getDirTree(request.getProductLineId(), request.getChannel());
        // 根据节点id获取节点
        DirNodeDto dir = getDir(request.getId(), root);
        if (dir == null) {
            // 若节点为空，抛出异常
            throw new ServiceException("目录节点获取为空" + StatusCode.INTERNAL_ERROR.getMsg(),StatusCode.INTERNAL_ERROR.getStatus() );
        }

        // 修改节点名称
        dir.setText(request.getText());
        // 更新业务表中的目录树信息
        bizMapper.updateContent(request.getProductLineId(), JSONObject.toJSONString(root), request.getChannel());
        return root;
    }

    /**
     * 删除文件夹
     *
     * @param request 包含删除所需信息的请求体
     * @return 包含删除后目录树信息的DirNodeDto对象
     */
    @Override
    public DirNodeDto delDir(DirDeleteReq request) {
        // 获取指定业务线和渠道下的目录树
        DirNodeDto root = getDirTree(request.getProductLineId(), request.getChannel());
        // 根据父节点id获取父节点
        DirNodeDto dir = getDir(request.getParentId(), root);
        if (dir == null) {
            // 若父节点为空，抛出异常
            throw new ServiceException("目录节点获取为空" + StatusCode.INTERNAL_ERROR.getMsg(),StatusCode.INTERNAL_ERROR.getStatus() );
        }

        // 遍历父节点的子节点列表
        Iterator<DirNodeDto> iterator = dir.getChildren().iterator();
        while (iterator.hasNext()) {
            DirNodeDto next = iterator.next();
            if (request.getDelId().equals(next.getId())) {
                // 若找到要删除的节点，将其从列表中移除
                iterator.remove();
                break;
            }
        }
        // 更新业务表中的目录树信息
        bizMapper.updateContent(request.getProductLineId(), JSONObject.toJSONString(root), request.getChannel());
        return root;
    }

    /**
     * 根据业务id和根节点查找目录节点
     *
     * @param bizId 业务id
     * @param root 根节点
     * @return 找到的目录节点，若未找到则返回null
     */
    @Override
    public DirNodeDto getDir(String bizId, DirNodeDto root) {
        if (root == null) {
            return null;
        }
        if (bizId.equals(root.getId())) {
            return root;
        }

        // 获取根节点的子节点列表
        List<DirNodeDto> children = root.getChildren();
        for (DirNodeDto child : children) {
            // 递归查找子节点
            DirNodeDto dir = getDir(bizId, child);
            if (dir != null) {
                return dir;
            }
        }
        return null;
    }



    /**
     * 获取包含所有测试用例的目录树响应对象
     *
     * @param root 目录树的根节点
     * @return 包含所有测试用例的目录树响应对象
     */
    @Override
    public DirTreeResp getAllCaseDir(DirNodeDto root) {
        // 创建一个目录树响应对象
        DirTreeResp resp = new DirTreeResp();
        // 将子目录的所有caseId分配到父目录
        addChildrenCaseIds(root);
        // 将根节点添加到响应对象的子节点列表中
        resp.getChildren().add(root);
        return resp;
    }

    /**
     * 获取指定业务线、业务id和渠道下的测试用例id列表
     *
     * @param productLineId 业务线id
     * @param bizId 业务id
     * @param channel 渠道
     * @return 测试用例id列表，若未找到则返回null
     */
    @Override
    public List<Long> getCaseIds(Long productLineId, String bizId, Integer channel) {
        // 获取包含所有测试用例的目录树响应对象
        DirTreeResp resp = getAllCaseDir(getDirTree(productLineId, channel));
        // 根据业务id获取目录节点
        DirNodeDto dir = getDir(bizId, resp.getChildren().get(0));
        if (dir == null) {
            return null;
        }
        // 获取目录节点的测试用例id集合，并转换为Long类型的列表
        Set<String> caseIds = dir.getCaseIds();
        return caseIds.stream().map(Long::valueOf).collect(Collectors.toList());
    }

    /**
     * 移动文件夹
     *
     * @param req 包含移动所需信息的请求体
     * @return 移动是否成功的标志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveDir(DirMoveReq req) {
        // 从业务表中查询指定业务线和渠道的记录
        Biz biz = bizMapper.selectOne(req.getProductLineId(), req.getChannel());
        if (biz == null) {
            // 若记录为空，抛出异常
            throw new ServiceException("目录节点获取为空" + StatusCode.INTERNAL_ERROR.getMsg(),StatusCode.INTERNAL_ERROR.getStatus() );
        }
        // 解析记录中的目录树信息
        DirNodeDto dataObj = JSONObject.parseObject(biz.getContent(), DirNodeDto.class);

        // 创建一个DirMoveDFS对象，用于进行深度优先搜索
        DirMoveDFS dfs = new DirMoveDFS(req.getFromId(), req.getToId());
        // 查找并删除要移动的文件夹
        dfs.findNodeAndDelete(dataObj);

        if (dfs.getToObj() == null || dfs.getFromObj() == null) {
            // 若被迁移的文件夹或者要迁移的文件夹不存在，抛出异常
            throw new ServiceException("被迁移的文件夹或者要迁移的文件夹不存在" + StatusCode.INTERNAL_ERROR.getMsg(),StatusCode.INTERNAL_ERROR.getStatus() );
        }

        // 将被迁移的文件夹的父节点id设置为目标文件夹的id
        dfs.getFromObj().setParentId(dfs.getToObj().getId());
        // 将被迁移的文件夹添加到目标文件夹的子节点列表中
        dfs.getToObj().getChildren().add(dfs.getFromObj());

        // 更新业务记录中的目录树信息
        biz.setContent(JSON.toJSONString(dataObj));
        // 更新业务表中的记录
        bizMapper.update(biz);
        return true;
    }

    /**
     * 将子目录的所有caseId分配到父目录
     *
     * @param root 当前节点
     */
    private void addChildrenCaseIds(DirNodeDto root) {
        if (root == null) {
            return;
        }
        // 遍历当前节点的子节点
        for (DirNodeDto child : root.getChildren()) {
            // 递归处理子节点
            addChildrenCaseIds(child);
            // 将子节点的测试用例id添加到当前节点的测试用例id集合中
            root.getCaseIds().addAll(child.getCaseIds());
        }
    }

    /**
     * 校验同级节点下是否存在相同名字的子节点
     *
     * @param text 节点名称
     * @param parentId 父节点id
     * @param dirNodeDto 节点内容
     */
    private void checkNodeExists(final String text, final String parentId, final DirNodeDto dirNodeDto) {
        if (parentId.equalsIgnoreCase(dirNodeDto.getId())) {
            // 获取当前节点的子节点列表
            List<DirNodeDto> childrenNodes = dirNodeDto.getChildren();
            if (childrenNodes.stream().anyMatch(node -> text.equalsIgnoreCase(node.getText()))) {
                // 若存在相同名字的子节点，抛出异常
                throw new ServiceException("目标节点已存在" +StatusCode.NODE_ALREADY_EXISTS.getMsg(),StatusCode.NODE_ALREADY_EXISTS.getStatus()  );
            }
        }
        // 获取当前节点的子节点列表
        List<DirNodeDto> childrenNodes = dirNodeDto.getChildren();
        // 递归检查子节点
        childrenNodes.forEach(node -> checkNodeExists(text, parentId, node));
    }

    /**
     * 获取当前节点的父节点id
     *
     * @param nodeId 节点id
     * @param dirTree 节点内容
     * @return 返回父节点id或者null
     */
    private String getParentIdWithRecursion(final String nodeId, final DirNodeDto dirTree) {
        if (nodeId.equalsIgnoreCase(dirTree.getId())) {
            return dirTree.getParentId();
        }
        // 获取当前节点的子节点列表
        List<DirNodeDto> children = dirTree.getChildren();
        for (DirNodeDto node : children) {
            // 递归查找子节点的父节点id
            String parentId = getParentIdWithRecursion(nodeId, node);
            if (parentId != null) {
                return parentId;
            }
        }
        return null;
    }
}
