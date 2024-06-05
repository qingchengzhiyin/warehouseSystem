package nwpu.group20.warehouse.constant;

public enum ResponseEnum {
    LOGIN_SUCCESS(200,"OK"),
    LOGIN_FAIL(500,"NO"),
    NO_LOGIN(501,"NO_LOGIN"),
    NO_AUTH(502,"NO_AUTH")
    ;
    private Integer code;
    private String msg;

    ResponseEnum() {
    }

    ResponseEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}