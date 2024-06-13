package nwpu.group20.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.Product;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.ProductInfoParam;
import nwpu.group20.warehouse.param.finalParam.part.ProductChangeInfoParam;
import nwpu.group20.warehouse.service.ProductService;
import nwpu.group20.warehouse.service.impl.UserServiceImpl;
import nwpu.group20.warehouse.vo.ProductStockVo;
import nwpu.group20.warehouse.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j//可以直接在代码写console.log
@Validated//可以限制传入参数
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;



    @Operation(summary = "获取所有产品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "找到所有商品信息")
    })
    @GetMapping("/allProducts")
    public HttpResult<List<ProductVo>> getAllProducts(){
        try{
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            List<ProductVo> products = productService.loadAllProducts();
            return new HttpResult<>(200,"找到所有商品信息",products);
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "通过ID获取产品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询商品成功")
    })
    @GetMapping("/loadProductById")
    public HttpResult<ProductVo> getProductById(@RequestParam int productId){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            ProductVo product = productService.loadProductVoById(productId);
            return new HttpResult<>(200,"查询商品成功",product );
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "更新产品信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功")
    })
    @PostMapping("/changeProductInfo")
    public HttpResult<Void> changeProductInfo(@RequestBody ProductChangeInfoParam productInfoParam1){
        int productId = productInfoParam1.getProductId();
        ProductInfoParam productInfoParam = productInfoParam1.getProductInfoParam();
        try{
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                productService.changeProductInfo(productId,productInfoParam);
                return new HttpResult<>(200,"更新成功");
            }else {
                return new HttpResult<>(401,"只有经理有权限更改产品信息");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "获取产品库存信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询库存成功")
    })
    @GetMapping("/productStocksById")
    public HttpResult<List<ProductStockVo>> getProductStockVoById(@RequestParam int productId) {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            List<ProductStockVo> productStocks = productService.loadProductStockVoById(productId);
            return new HttpResult<>(200, "查询库存成功", productStocks);
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }
    @Operation(summary = "增加产品")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "插入成功")
    })
    @PostMapping("/addProduct")
    public HttpResult<Void> addProduct(@RequestBody ProductInfoParam productInfoParam){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                productService.insertProduct(productInfoParam);
                return new HttpResult<>(200,"插入成功");
            }else {
                return new HttpResult<>(401,"只有经理可以增加产品");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "删除产品")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    @DeleteMapping("/removeProduct")
    public HttpResult<Void> removeProduct(@RequestParam int productId){
        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null ||
                    !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
                // 返回未登录的 HttpResult
                return new HttpResult<>(401, "未登录");
            }
            if(UserServiceImpl.getCurrentUserType() == 0){
                productService.deleteProduct(productId);
                return new HttpResult<>(200,"删除成功");
            }else {
                return new HttpResult<>(401,"只有经理可以删除产品");
            }

        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"服务器无法处理请求");
        }

    }

    @Operation(summary = "根据id找产品描述")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "返回成功")
    })
    @PostMapping("/selectProductDescriptionById")
    public HttpResult<String> selectProductDescriptionById(@RequestParam int productId){
        try{
            return new HttpResult<>(200,"返回成功",productService.loadProductVoById(productId).getDescription());
        }catch (Exception e){
            e.printStackTrace();
            return new HttpResult<>(503,"运行时异常");
        }

    }
}
