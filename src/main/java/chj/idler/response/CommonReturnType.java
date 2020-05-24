package chj.idler.response;

public class CommonReturnType {
    private Object data;
    private Boolean succeed;

    public CommonReturnType(Object data, Boolean succeed) {
        this.data = data;
        this.succeed = succeed;
    }
    public CommonReturnType(){

    }

    public static CommonReturnType create(Object data){
        return create(data,true);
    }
    public static CommonReturnType create(Object data,Boolean succeed){
        return new CommonReturnType(data,succeed);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    @Override
    public String toString() {
        return "CommonReturnType{" +
                "data=" + data +
                ", succeed='" + succeed + '\'' +
                '}';
    }
}
