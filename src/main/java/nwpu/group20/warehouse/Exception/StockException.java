package nwpu.group20.warehouse.Exception;

import lombok.Data;

@Data
public class StockException extends RuntimeException{
    public StockException(String message){
        super(message);
    }
}
