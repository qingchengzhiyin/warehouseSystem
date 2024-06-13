package nwpu.group20.warehouse.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.Exception.RoleException;
import nwpu.group20.warehouse.Exception.StockException;
import nwpu.group20.warehouse.entity.Outbound;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.OutboundAddParam;
import nwpu.group20.warehouse.param.OutboundDetailsParam;
import nwpu.group20.warehouse.param.OutboundParam;
import nwpu.group20.warehouse.param.finalParam.OutboundFinalAddParam;
import nwpu.group20.warehouse.param.finalParam.OutboundFinishStockParam;
import nwpu.group20.warehouse.service.OutboundService;
import nwpu.group20.warehouse.service.impl.UserServiceImpl;
import nwpu.group20.warehouse.vo.OutboundDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j // 可以直接在代码中使用 log
@Validated // 可以限制传入参数
@RequestMapping("/outbound")
public class OutboundController {

    @Autowired
    private OutboundService outboundService;

    @Operation(summary = "增加出库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "增加成功")
    })
    @PostMapping("/addOutbound")
    public HttpResult<Void> addOutbound(@RequestBody OutboundFinalAddParam outboundFinalAddParam) {
        OutboundAddParam outboundAddParam = outboundFinalAddParam.toOutboundAddParam();
        try{
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                outboundService.insertOutbound(outboundAddParam.getOutboundParam(), outboundAddParam.getOutboundDetailsParamList());
                return new HttpResult<>(200, "增加成功");
            }else {
                return new HttpResult<>(401,"只有经理可以增加出库单");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "删除出库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @DeleteMapping("/removeOutbound")
    public HttpResult<Void> removeOutbound(@RequestParam int outboundOrderId) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                outboundService.deleteOutbound(outboundOrderId);
                return new HttpResult<>(200, "删除成功");
            }else {
                return new HttpResult<>(401,"只有经理有权限删除出库单");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "查看出库单详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查看成功")
    })
    @GetMapping("/outboundDetails")
    public HttpResult<List<OutboundDetailsVo>> outboundDetails(@RequestParam int outboundOrderId) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0 || UserServiceImpl.getCurrentUserType() == 1){
                return new HttpResult<>(200, "查看成功", outboundService.selectOutboundOrderById(outboundOrderId));
            }else {
                return new HttpResult<>(401,"只有经理有权限删除出库单");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }
   }

    @Operation(summary = "确认出库")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "确认出库")
    })
    @PostMapping("/finishOutbound")
    public HttpResult<List<OutboundFinishStockParam>> finishOutbound(@RequestParam int outboundOrderId) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 1){
                outboundService.completeOutbound(outboundOrderId);
                return new HttpResult<>(200, "OK");
            }else {
                return new HttpResult<>(401,"只有操作员可以确认出库");
            }
        }catch (RoleException e){
            e.printStackTrace();
            return new HttpResult<>(401,"不是对应操作员");
        }catch (StockException e){
            e.printStackTrace();
            return new HttpResult<>(403,"库存不足");
        }
        catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "返回所有出库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "返回成功")
    })
    @GetMapping("/allOutbound")
    public HttpResult<List<Outbound>> getAllOutbounds(){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            return new HttpResult<>(200,"返回成功",outboundService.loadAllOutbounds());

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

}