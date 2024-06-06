package nwpu.group20.warehouse.controller;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.Product;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.param.ProductInfoParam;
import nwpu.group20.warehouse.service.ProductService;
import nwpu.group20.warehouse.vo.ProductStockVo;
import nwpu.group20.warehouse.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
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




    @GetMapping("/allProducts")
    public HttpResult getAllProducts(){
        List<ProductVo> products = productService.loadAllProducts();
        return new HttpResult(200,"找到所有商品信息",products);
    }

    @GetMapping("loadProductById")
    public HttpResult getProductById(@RequestParam int productId){
        ProductVo product = productService.loadProductVoById(productId);
        return new HttpResult(200,"查询商品成功",product );
    }

    @PostMapping("/changeProductInfo")
    public HttpResult changeProductInfo(@RequestBody int productId, @RequestBody ProductInfoParam productInfoParam){
        productService.changeProductInfo(productId,productInfoParam);
        return new HttpResult(200,"更新成功");
    }

    @GetMapping("/productStocksById")
    public List<ProductStockVo> getProductStockVoById(@RequestParam int productId) {
        return productService.loadProductStockVoById(productId);
    }
}
