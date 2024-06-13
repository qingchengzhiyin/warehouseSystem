package nwpu.group20.warehouse.controller;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.http.HttpResult;
import nwpu.group20.warehouse.mapper.LocationMapper;
import nwpu.group20.warehouse.service.LocationService;
import nwpu.group20.warehouse.vo.LocationVo;
import nwpu.group20.warehouse.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j//可以直接在代码写console.log
@Validated//可以限制传入参数
@RequestMapping("/location")
public class LocationController {

    // 为service注入实例
    @Autowired
    private LocationService locationService;

    // 获取所有库位信息
    @GetMapping("/allLocations")
    public HttpResult<List<LocationVo>> getAllLocations(){
        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            // 返回未登录的 HttpResult
            return new HttpResult<>(401, "未登录");
        }
        List<LocationVo> locationVos = locationService.loadALLLocations();
        return new HttpResult<>(200,"获取所有库位信息",locationVos);
    }

    // 通过id查询库位信息
    @GetMapping("/loadLocationById")
    public HttpResult<LocationVo> getLocationById(@RequestParam int locationId){
        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            // 返回未登录的 HttpResult
            return new HttpResult<>(401, "未登录");
        }
        LocationVo locationVo = locationService.loadLoactionById(locationId);
        return new HttpResult<>(200,"查询库位成功",locationVo);
    }

    // 更改库位库存余量，若更改后的库存余量>=0且<=capacity才会更新。capacityChange>0代表入库，反之为出库
    @PostMapping("/changeLocationById")
    public HttpResult<Void> updateLocationById(@RequestParam int locationId,@RequestParam float capacityChange){
        if (SecurityContextHolder.getContext().getAuthentication() == null ||
                !SecurityContextHolder.getContext().getAuthentication().isAuthenticated() || SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            // 返回未登录的 HttpResult
            return new HttpResult<>(401, "未登录");
        }
        locationService.changeLocationById(locationId,capacityChange);
        return new HttpResult<>(200,"更新成功");
    }
}
