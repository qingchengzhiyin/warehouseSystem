package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.entity.Product;
import nwpu.group20.warehouse.param.ProductInfoParam;
import nwpu.group20.warehouse.vo.ProductStockVo;
import nwpu.group20.warehouse.vo.ProductVo;

import java.util.List;

public interface ProductService {
    List<ProductVo> loadAllProducts();

    void changeProductInfo(int productId, ProductInfoParam productInfoParam);


    ProductVo loadProductVoById(int userId);

    public List<ProductStockVo> loadProductStockVoById(int productId);

    void insertProduct(ProductInfoParam productInfoParam);

    void deleteProduct(int productId);

    String loadProductDescriptionById(int productId);
}
