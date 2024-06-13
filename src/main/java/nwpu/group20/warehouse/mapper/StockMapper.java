package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.Inbound;
import nwpu.group20.warehouse.entity.Stock;
import nwpu.group20.warehouse.vo.InboundDetailsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface StockMapper {
    void addStock(List<Stock> Stock);

    int getTotalStock(int productId);

    List<Stock> getStockByProductIdOrderedByProductionDate(int productId);

    void updateStock(int productId, Date productionDate, int locationId, int i);

    Stock getStock(int productId, Date productionDate, int locationId);

    void addSingleStock(Stock stock);
}
