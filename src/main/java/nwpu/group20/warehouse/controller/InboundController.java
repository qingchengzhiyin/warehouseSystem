package nwpu.group20.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.InboundAddParam;
import nwpu.group20.warehouse.param.InboundDetailsParam;
import nwpu.group20.warehouse.param.InboundParam;
import nwpu.group20.warehouse.service.InboundService;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import org.springframework.beans.factory.annotation.Autowired;
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
    public HttpResult<Void> addInbound(@RequestBody InboundAddParam inboundAddParam){
        inboundService.insertInbound(inboundAddParam.getInboundParam(),inboundAddParam.getInboundDetailsParamList());
        return new HttpResult<>(200,"增加成功");
    }

    @Operation(summary = "删除入库单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @PostMapping("/removeInbound")
    public HttpResult<Void> removeInbound(@RequestParam int inboundOrderId){
        inboundService.deleteInbound(inboundOrderId);
        return new HttpResult<>(200,"删除成功");
    }

    @Operation(summary = "查看入库单详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查看成功")
    })
    @GetMapping("/inboundDetails")
    public HttpResult<List<InboundDetailsVo>> inboundDetails(@RequestParam int inboundOrderId){
        return new HttpResult<>(200,"查看成功",inboundService.selectInboundOrderById(inboundOrderId));
    }


}
