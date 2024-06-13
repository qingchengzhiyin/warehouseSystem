package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.mapper.LocationMapper;
import nwpu.group20.warehouse.service.LocationService;
import nwpu.group20.warehouse.vo.LocationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationMapper locationMapper;
    @Override
    public List<LocationVo> loadALLLocations() {
        return locationMapper.loadAllLocations();
    }

    @Override
    public LocationVo loadLoactionById(int locationId) {
        return locationMapper.loadLocationById(locationId);
    }

    @Override
    public void changeLocationById(int locationId, float capacityChange) {
        locationMapper.changeLocationById(locationId,capacityChange);
    }
}
