package nwpu.group20.warehouse.service;

import nwpu.group20.warehouse.param.CheckAddParam;
import nwpu.group20.warehouse.param.StockNumberParam;
import nwpu.group20.warehouse.vo.CheckDetailsVo;
import nwpu.group20.warehouse.vo.CheckVo;

import java.util.List;

public interface CheckService {
    List<CheckVo> loadAllChecks();
    List<CheckDetailsVo> loadCheckDetailById(int checkId);
    void insertCheck(CheckAddParam checkAddParam);
    void insertCheckDetail(CheckDetailsVo checkDetailsVo);
    void deleteCheck(int checkId);
    void deleteCheckDetails(int checkId);
    StockNumberParam loadProductNumber(int productId, int locationId);
    void changeCheckDetail(int checkId,int productId,int locationId,int checkNumber);
    void changeCheckUpdateTime(int checkId);
}
