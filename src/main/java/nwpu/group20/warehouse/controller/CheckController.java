package nwpu.group20.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.mapper.StockMapper;
import nwpu.group20.warehouse.param.CheckAddParam;
import nwpu.group20.warehouse.param.StockNumberParam;
import nwpu.group20.warehouse.service.CheckService;
import nwpu.group20.warehouse.vo.CheckDetailsVo;
import nwpu.group20.warehouse.vo.CheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j//可以直接在代码写console.log
@Validated//可以限制传入参数
@RequestMapping("/check")
public class CheckController {
    @Autowired
    private CheckService checkService;
    @Autowired
    private StockMapper stockMapper;

    @Operation(summary = "查询所有盘点单")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询所有盘点单")
    })
    @GetMapping("/allChecks")
    public HttpResult<List<CheckVo>> getAllChecks(){
        try {
            List<CheckVo> checkVos = checkService.loadAllChecks();
            return new HttpResult<>(200, "获取所有盘点单成功", checkVos);
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "添加一个盘点单，并将自动生成的盘点单号返回")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "添加一个盘点单")
    })
    @PostMapping("/insertCheck")
    public HttpResult<Integer> addCheck(CheckAddParam checkAddParam){
        try {
            checkService.insertCheck(checkAddParam);
            return new HttpResult<>(200,"插入成功 ",checkAddParam.getCheckId());
        } catch (Exception e){
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "根据checkId删除盘点和其所有明细")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "根据checkId删除盘点和其所有明细")
    })
    // 根据checkId删除盘点和其所有明细
    @DeleteMapping("/deleteCheckAndDetails")
    public HttpResult<Void> deleteCheck(@RequestParam int checkId){
        try{
            checkService.deleteCheck(checkId);
            checkService.deleteCheckDetails(checkId);
            return new HttpResult<>(200,"删除成功");
        } catch (Exception e){
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "添加一个盘点明细（理论数量自动从数据库获取，并返回给前端）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "添加一个盘点明细（理论数量自动从数据库获取）")
    })
    @PostMapping("/insertCheckDetail")
    public HttpResult<Integer> addCheckDetail(@RequestBody  int checkId,int productId,int locationId,int checkNumber){
        try{
            // 更新时间
            checkService.changeCheckUpdateTime(checkId);
            StockNumberParam stockNumberParam = checkService.loadProductNumber(productId,locationId);
            int number = stockNumberParam.getNumber();
            CheckDetailsVo checkDetailsVo = new CheckDetailsVo();
            // 盘点单号
            checkDetailsVo.setCheckId(checkId);
            // 商品号
            checkDetailsVo.setProductId(productId);
            // 库位号
            checkDetailsVo.setLocationId(locationId);
            // 理论值
            checkDetailsVo.setCheckNumberTheory(number);
            // 实际值
            checkDetailsVo.setCheckNumber(checkNumber);
            checkService.insertCheckDetail(checkDetailsVo);
            return new HttpResult<>(200,"添加盘点明细成功",number);
        } catch (Exception e){
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "根据id查询盘点明细")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "根据id查询盘点明细")
    })
    @GetMapping("/loadCheckDetailsById")
    public HttpResult<List<CheckDetailsVo>> getCheckDetailsById(@RequestParam int checkId){
        try{
            List<CheckDetailsVo> checkDetailsVos = checkService.loadCheckDetailById(checkId);
            return new HttpResult<>(200,"查询成功",checkDetailsVos);
        } catch (Exception e){
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }

    @Operation(summary = "更改盘点明细中的实际数目")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更改盘点明细中的实际数目")
    })
    @PostMapping("/changeCheckDetail")
    public HttpResult<Void> updateCheckDetail(@RequestBody int checkId,int productId,int locationId,int checkNumber){
        try{
            // 更新时间
            checkService.changeCheckUpdateTime(checkId);
            checkService.changeCheckDetail(checkId,productId,locationId,checkNumber);
            return new HttpResult<>(200,"更改成功");
        } catch (Exception e){
            return new HttpResult<>(503,"服务器无法处理请求");
        }
    }
}

