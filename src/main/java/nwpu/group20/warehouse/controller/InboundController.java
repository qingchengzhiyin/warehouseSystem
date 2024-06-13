package nwpu.group20.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.Exception.RoleException;
import nwpu.group20.warehouse.Exception.StockException;
import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.InboundAddParam;
import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.param.finalParam.InboundDetailsUpdateParam;
import nwpu.group20.warehouse.param.finalParam.InboundFinalAddParam;
import nwpu.group20.warehouse.service.InboundService;
import nwpu.group20.warehouse.service.impl.UserServiceImpl;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j//可以直接在代码写console.log
@Validated//可以限制传入参数
@RequestMapping("/inbound")
public class InboundController {
    @Autowired
    private InboundService inboundService;
    @Operation(summary = "增加入库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "增加成功")
    })
    @PostMapping("/addInbound")
    public HttpResult<Void> addInbound(@RequestBody InboundFinalAddParam inboundFinalAddParam){
        InboundAddParam inboundAddParam = inboundFinalAddParam.toInboundAddParam();
        try {
//            System.out.println(SecurityContextHolder.getContext().getAuthentication().toString());
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                inboundService.insertInbound(inboundAddParam.getInboundParam(),inboundAddParam.getInboundDetailsParamList());
                return new HttpResult<>(200,"增加成功");
            }else {
                return new HttpResult<>(401,"只有经理有权限添加入库单");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "删除入库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @DeleteMapping("/removeInbound")
    public HttpResult<Void> removeInbound(@RequestParam int inboundOrderId){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                inboundService.deleteInbound(inboundOrderId);
                return new HttpResult<>(200,"删除成功");
            }else {
                return new HttpResult<>(401,"只有经理可以删除入库单");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "查看入库单详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查看成功")
    })
    @GetMapping("/inboundDetails")
    public HttpResult<List<InboundDetailsVo>> inboundDetails(@RequestParam int inboundOrderId){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            return new HttpResult<>(200,"查看成功",inboundService.selectInboundOrderById(inboundOrderId));
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "确认入库")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "确认入库")
    })
    @PostMapping("/finishInbound")
    public HttpResult<Void> finishInbound(@RequestBody List<InboundDetailsUpdateParam> inboundDetailsUpdateParams){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if (UserServiceImpl.getCurrentUserType() == 1) {
                inboundService.completeInbound(inboundDetailsUpdateParams);
                return new HttpResult<>(200, "OK");
            } else {
                return new HttpResult<>(401, "只有对应的操作员可以确认入库");
            }
        } catch (RoleException e) {
            e.printStackTrace();
            return new HttpResult<>(401, "不是对应操作员");
        } catch (StockException e) {
            e.printStackTrace();
            return new HttpResult<>(403, "库位不足存放");
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResult<>(503, "服务器无法处理请求");
        }

    }

    @Operation(summary = "返回所有入库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "返回成功")
    })
    @GetMapping("/allInbound")
    public HttpResult<List<Inbound>> getAllInbounds(){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            return new HttpResult<>(200,"返回成功",inboundService.loadAllInbounds());

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

}
