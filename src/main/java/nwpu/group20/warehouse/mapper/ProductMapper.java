package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.Product;
import nwpu.group20.warehouse.vo.ProductStockVo;
import nwpu.group20.warehouse.vo.ProductVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductVo> loadAllProducts();

    Product loadProductById(int productId);

    void updateProductById(int productId, Product product);

    ProductVo loadProductVoById(int productId);
    List<ProductStockVo> loadProductStockVoById(@Param("productId") int productId);
}
