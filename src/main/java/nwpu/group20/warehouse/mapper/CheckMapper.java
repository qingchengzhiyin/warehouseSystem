package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.param.CheckAddParam;
import nwpu.group20.warehouse.param.StockNumberParam;
import nwpu.group20.warehouse.vo.CheckDetailsVo;
import nwpu.group20.warehouse.vo.CheckVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CheckMapper {
    // 获取所有盘点单
    List<CheckVo> loadAllChecks();
    // 获取某一盘点的detail
    List<CheckDetailsVo> loadCheckDetailById(int checkId);
    // 新建一条盘点(只用输入operatorId，创建时间更新时间默认为当前系统时间)
    void insertCheck(CheckAddParam checkAddParam);
    // 新建一条盘点明细(理想数量从库存中自动获取，操作员输入实际数量)
    void insertCheckDetail(CheckDetailsVo checkDetailsVo);
    // 根据checkId删除盘点
    void deleteCheck(int checkId);
    // 根据checkId删除盘点明细
    void deleteCheckDetails(int checkId);
    // 根据product_id location_id来查询对应商品的库存量
    StockNumberParam loadProductNumber(int productId, int locationId);
    // 输入checkId.productId,locationId和实际数量，更改对应checkDetail的实际数量，并改变对应check的更新时间
    void changeCheckDetail(int checkId,int productId,int locationId,int checkNumber);
    // 根据checkId改变其updateTime
    void changeCheckUpdateTime(int checkId);
}
