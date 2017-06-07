package com.rupeng.util;

//表示ajax请求的json格式的想要
public class AjaxResult {

    private String status; //表示响应状态，规定"success"表示成功，"error"表示失败
    private Object data; //表示响应信息，既可以是提示信息，也可以是客户端需要的数据

    public AjaxResult() {

    }

    public AjaxResult(String status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static AjaxResult errorInstance(Object data) {
        return new AjaxResult("error", data);
    }

    public static AjaxResult successInstance(Object data) {
        return new AjaxResult("success", data);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
