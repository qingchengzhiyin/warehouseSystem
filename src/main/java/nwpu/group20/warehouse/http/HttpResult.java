package nwpu.group20.warehouse.http;

import lombok.Data;
import lombok.NoArgsConstructor;
import nwpu.group20.warehouse.constant.ResponseEnum;


@Data
@NoArgsConstructor
public class HttpResult<T> {
    private Integer code;
    private String msg;
    private T data;

    public HttpResult<T> ok(T t){
        return new HttpResult(ResponseEnum.LOGIN_SUCCESS.getCode(), t);
    }

    public static final HttpResult<Void> LOGIN_SUCCESS=
            new HttpResult(ResponseEnum.LOGIN_SUCCESS.getCode(),
                    ResponseEnum.LOGIN_SUCCESS.getMsg());

    public static final HttpResult<Void> LOGIN_FAIL=
            new HttpResult(ResponseEnum.LOGIN_FAIL.getCode(),
                    ResponseEnum.LOGIN_FAIL.getMsg());

    public static final HttpResult<Void> NO_LOGIN=
            new HttpResult(ResponseEnum.NO_LOGIN.getCode(),
                    ResponseEnum.NO_LOGIN.getMsg());

    public static final HttpResult<Void> NO_AUTH=
            new HttpResult(ResponseEnum.NO_AUTH.getCode(),
                    ResponseEnum.NO_AUTH.getMsg());

    public HttpResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }
    public HttpResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public HttpResult(Integer code, String msg,T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}