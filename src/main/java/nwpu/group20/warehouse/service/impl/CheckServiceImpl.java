package nwpu.group20.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import nwpu.group20.warehouse.mapper.CheckMapper;
import nwpu.group20.warehouse.param.CheckAddParam;
import nwpu.group20.warehouse.param.StockNumberParam;
import nwpu.group20.warehouse.service.CheckService;
import nwpu.group20.warehouse.vo.CheckDetailsVo;
import nwpu.group20.warehouse.vo.CheckVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CheckServiceImpl implements CheckService {
    @Autowired
    private CheckMapper checkMapper;
    @Override
    public List<CheckVo> loadAllChecks() {
        return checkMapper.loadAllChecks();
    }

    @Override
    public List<CheckDetailsVo> loadCheckDetailById(int checkId) {
        return checkMapper.loadCheckDetailById(checkId);
    }

    @Override
    public void insertCheck(CheckAddParam checkAddParam) {
        checkMapper.insertCheck(checkAddParam);
    }

    @Override
    public void insertCheckDetail(CheckDetailsVo checkDetailsVo) {
        checkMapper.insertCheckDetail(checkDetailsVo);
    }

    @Override
    public void deleteCheck(int checkId) {
        checkMapper.deleteCheck(checkId);
    }

    @Override
    public void deleteCheckDetails(int checkId) {
        checkMapper.deleteCheckDetails(checkId);
    }

    @Override
    public StockNumberParam loadProductNumber(int productId, int locationId) {
        return checkMapper.loadProductNumber(productId,locationId);
    }

    @Override
    public void changeCheckDetail(int checkId, int productId, int locationId, int checkNumber) {
        checkMapper.changeCheckDetail(checkId,productId,locationId,checkNumber);
    }

    @Override
    public void changeCheckUpdateTime(int checkId) {
        checkMapper.changeCheckUpdateTime(checkId);
    }
}
