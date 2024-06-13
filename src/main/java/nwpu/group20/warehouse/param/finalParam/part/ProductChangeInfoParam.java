package nwpu.group20.warehouse.param.finalParam.part;

import lombok.Data;
import nwpu.group20.warehouse.param.ProductInfoParam;

@Data
public class ProductChangeInfoParam {
    private int productId;
    private ProductInfoParam productInfoParam;
}
