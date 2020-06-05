package chj.idler.response;


public enum EmBusinessError implements CommonError {
    //通用错误类型10001
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    UNKNOWN_ERROR(10002,"未知错误"),
    URL_ERROR(10003,"url不合法"),
    //20000开头为用户信息相关错误定义
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"用户名或密码不正确"),
    USER_NOT_LOGIN(20003,"用户未登陆"),
    REGISTRATION_EXPIRE(20004,"注册已过期，请重新注册"),

    //30000为业务错误
    URL_NOT_SUPPORT(30001,"暂不支持此网址"),
    VIDEO_IS_SUB(30002,"视频已订阅"),
    VIDEO_NOT_SUB(30003,"未订阅此视频"),


    ;

    EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }


    private int errCode;
    private String errMsg;


    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
