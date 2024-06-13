package nwpu.group20.warehouse.mapper;

import nwpu.group20.warehouse.entity.Location;
import nwpu.group20.warehouse.vo.LocationVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LocationMapper {
    // 获取所有库位信息
    List<LocationVo> loadAllLocations();
    // 根据库位id查询单个库位信息
    LocationVo loadLocationById(int locationId);
    /**
     * 根据库位id和入库/出库量更改库存余量
     * @param locationId 要更改的库位id
     * @param capacityChange 库存余量变化量，有符号数，负数代表出库正数代表入库
     */
    void changeLocationById(int locationId, float capacityChange);

}
