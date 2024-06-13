package nwpu.group20.warehouse.service;


import nwpu.group20.warehouse.vo.LocationVo;

import java.util.List;

public interface LocationService {
    List<LocationVo> loadALLLocations();

    LocationVo loadLoactionById(int locationId);

    void changeLocationById(int locationId, float capacityChange);
}
