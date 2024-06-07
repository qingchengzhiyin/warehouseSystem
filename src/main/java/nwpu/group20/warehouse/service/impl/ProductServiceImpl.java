package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.entity.Product;
import nwpu.group20.warehouse.mapper.ProductMapper;
import nwpu.group20.warehouse.param.ProductInfoParam;
import nwpu.group20.warehouse.service.ProductService;
import nwpu.group20.warehouse.vo.ProductStockVo;
import nwpu.group20.warehouse.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Override
    public List<ProductVo> loadAllProducts() {
        return productMapper.loadAllProducts();
    }

    @Override
    public void changeProductInfo(int productId, ProductInfoParam productInfoParam) {
        Product product = productMapper.loadProductById(productId);
        product.setDescription(productInfoParam.getDescription());
        product.setProvider(productInfoParam.getProvider());
        product.setCost(productInfoParam.getCost());
        product.setPrice(productInfoParam.getPrice());
        productMapper.updateProductById(productId,product);
    }

    @Override
    public ProductVo loadProductVoById(int productId) {
        return productMapper.loadProductVoById(productId);
    }

    public List<ProductStockVo> loadProductStockVoById(int productId) {
        return productMapper.loadProductStockVoById(productId);
    }

    @Override
    public void insertProduct(ProductInfoParam productInfoParam) {
        productMapper.insertProduct(productInfoParam);
    }

    @Override
    public void deleteProduct(int productId) {
        productMapper.deleteProduct(productId);
    }


}
