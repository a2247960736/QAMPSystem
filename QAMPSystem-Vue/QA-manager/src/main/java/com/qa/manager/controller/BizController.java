package com.qa.manager.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import com.qa.common.constant.ProjectChannelConstants;
import com.qa.manager.domain.request.dir.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qa.common.annotation.Log;
import com.qa.common.core.controller.BaseController;
import com.qa.common.core.domain.AjaxResult;
import com.qa.common.enums.BusinessType;
import com.qa.manager.domain.Biz;
import com.qa.manager.service.IBizService;
import com.qa.common.utils.poi.ExcelUtil;
import com.qa.common.core.page.TableDataInfo;

/**
 * 文件夹Controller
 *
 * @author jinrong.dong
 * @date 2025-03-22
 */
@Api(tags = "文件夹管理接口")
@RestController
@RequestMapping("/manager/biz")
public class BizController extends BaseController
{
    @Autowired
    private IBizService bizService;

    /**
     * 获取业务线下的目录树
     *
     * @return 包含目录树信息的响应体
     */
    @ApiOperation(value = "获取业务线下的目录树", notes = "获取业务线下的目录树信息")
    @PreAuthorize("@ss.hasPermi('manager:biz:list')")
    @GetMapping("/list")
    public AjaxResult list(Biz biz)
    {
        List<DirTreeResp> list = new ArrayList<>();
        return success(bizService.getAllCaseDir(bizService.getDirTree(ProjectChannelConstants.PRODUCT_LINE_ID, ProjectChannelConstants.CHANNEL)));
    }

    /**
     * 导出文件夹列表
     */
    @ApiOperation(value = "导出文件夹列表", notes = "将文件夹列表导出为 Excel 文件")
    @PreAuthorize("@ss.hasPermi('manager:biz:export')")
    @Log(title = "文件夹", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Biz biz)
    {
        List<Biz> list = bizService.selectBizList(biz);
        ExcelUtil<Biz> util = new ExcelUtil<Biz>(Biz.class);
        util.exportExcel(response, list, "文件夹数据");
    }

    /**
     * 获取文件夹详细信息
     */
    @ApiOperation(value = "获取文件夹详细信息", notes = "根据文件夹 ID 获取文件夹的详细信息")
    @PreAuthorize("@ss.hasPermi('manager:biz:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@ApiParam(name = "id", value = "文件夹 ID", required = true) @PathVariable("id") Long id)
    {
        return success(bizService.selectBizById(id));
    }

    /**
     * 选中父节点，增加其下的文件夹
     * 创建同级 - 选中DirNode中的parentId
     * 创建子级 - 选在DirNode中的id
     *
     * @param request 包含创建文件夹所需信息的请求体
     * @return 包含创建结果的响应体
     */
    @ApiOperation(value = "增加文件夹", notes = "选中父节点，增加其下的文件夹，可创建同级或子级文件夹")
    @PreAuthorize("@ss.hasPermi('manager:biz:add')")
    @Log(title = "文件夹", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@ApiParam(name = "request", value = "创建文件夹请求体", required = true) @RequestBody DirCreateReq request)
    {
        // 对请求体进行验证
        request.setProductLineId(ProjectChannelConstants.PRODUCT_LINE_ID);
        request.validate();
        // 调用DirService的方法创建文件夹，并将结果封装到响应体中返回
        return success(bizService.addDir(request));
        // 捕获自定义异常，重新抛出该异常
    }

    /**
     * 修改文件夹
     */
    @ApiOperation(value = "修改文件夹", notes = "修改文件夹的相关信息")
    @PreAuthorize("@ss.hasPermi('manager:biz:edit')")
    @Log(title = "文件夹", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@ApiParam(name = "biz", value = "文件夹信息", required = true) @RequestBody Biz biz)
    {
        return toAjax(bizService.updateBiz(biz));
    }

    /**
     * 删除文件夹
     */
    @ApiOperation(value = "删除文件夹", notes = "根据文件夹 ID 删除文件夹")
    @PreAuthorize("@ss.hasPermi('manager:biz:remove')")
    @Log(title = "文件夹", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@ApiParam(name = "ids", value = "文件夹 ID 数组", required = true) @PathVariable Long[] ids)
    {
        return toAjax(bizService.deleteBizByIds(ids));
    }


    /**
     * 重命名节点名称
     *
     * @param request 包含重命名所需信息的请求体
     * @return 包含重命名结果的响应体
     */
    @ApiOperation(value = "重命名文件夹", notes = "重命名文件夹节点的名称")
    @PreAuthorize("@ss.hasPermi('manager:biz:edit')")
    @Log(title = "文件夹", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/rename")
    public AjaxResult renameDir(@ApiParam(name = "request", value = "重命名请求体", required = true) @RequestBody DirRenameReq request) {
        // 对请求体进行验证
        request.setProductLineId(ProjectChannelConstants.PRODUCT_LINE_ID);
        request.validate();
        // 调用DirService的方法重命名文件夹，并将结果封装到响应体中返回
        return success(bizService.renameDir(request));
    }

    /**
     * 删除节点
     *
     * @param request 包含删除所需信息的请求体
     * @return 包含删除结果的响应体
     */
    @ApiOperation(value = "删除文件夹节点", notes = "删除文件夹节点及其相关信息")
    @PreAuthorize("@ss.hasPermi('manager:biz:remove')")
    @Log(title = "文件夹", businessType = BusinessType.DELETE)
    @PostMapping(value = "/delete")
    public AjaxResult deleteDir(@ApiParam(name = "request", value = "删除请求体", required = true) @RequestBody DirDeleteReq request) {
        // 对请求体进行验证
        request.setProductLineId(ProjectChannelConstants.PRODUCT_LINE_ID);
        request.validate();
        // 调用DirService的方法删除文件夹，并将结果封装到响应体中返回
        return success(bizService.getAllCaseDir(bizService.delDir(request)));
    }


    /**
     * 新增、更新卡片下的目录树
     * 这里是返回没有头节点(顶级文件夹)的树
     *
     * @param productLineId 业务线id，不能为空
     * @param channel 渠道，不能为空
     * @return 包含目录树信息的响应体
     */
    @ApiOperation(value = "获取卡片下的目录树", notes = "新增、更新卡片下的目录树，返回没有头节点的树")
    @PreAuthorize("@ss.hasPermi('manager:biz:add')")
    @Log(title = "文件夹", businessType = BusinessType.INSERT)
    @GetMapping(value = "/cardTree")
    public AjaxResult getDirCardTree(@ApiParam(name = "productLineId", value = "业务线 ID", required = true) @RequestParam @NotNull(message = "业务线id为空") Long productLineId,
                                     @ApiParam(name = "channel", value = "渠道", required = true) @RequestParam @NotNull(message = "渠道为空") Integer channel) {
        // 调用DirService的方法获取目录树，并将结果封装到响应体中返回
        return success(bizService.getDirTree(productLineId, channel));
    }

    /**
     * 移动文件夹，会将文件夹下及其子文件夹全部移动
     * 不允许移动root文件夹 -- 通过传参校验
     * 不允许从父文件夹移动到子文件夹 -- 通过dfs路径计算
     * 不允许移动不存在的文件夹，或者移动到不存在的文件夹 -- 通过dfs埋点
     *
     * @param req 包含移动所需信息的请求体
     * @return 包含移动结果的响应体
     */
    @ApiOperation(value = "移动文件夹", notes = "移动文件夹及其子文件夹，有相关移动限制")
    @PreAuthorize("@ss.hasPermi('manager:biz:edit')")
    @Log(title = "文件夹", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/move")
    public AjaxResult moveDir(@ApiParam(name = "req", value = "移动请求体", required = true) @RequestBody DirMoveReq req) {
        // 对请求体进行验证
        req.setProductLineId(ProjectChannelConstants.PRODUCT_LINE_ID);
        req.validate();
        // 调用DirService的方法移动文件夹，并将结果封装到响应体中返回
        return success(bizService.moveDir(req));
    }
}