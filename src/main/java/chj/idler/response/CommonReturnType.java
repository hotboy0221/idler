package chj.idler.response;

public class CommonReturnType {
    private Object data;
    private String status;

    public CommonReturnType(Object data, String status) {
        this.data = data;
        this.status = status;
    }
    public CommonReturnType(){

    }

    public static CommonReturnType create(Object data){
        return create(data,"success");
    }
    public static CommonReturnType create(Object data,String status){
        return new CommonReturnType(data,status);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
