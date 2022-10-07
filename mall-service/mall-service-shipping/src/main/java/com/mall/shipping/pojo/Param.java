package com.mall.shipping.pojo;

public class Param {
    //快递公司的编码，一律用小写字母，见《快递公司编码》
    String kuaidicom;
    //快递单号，单号的最大长度是32个字符
    String kuaidinum;
    //状态码
    String status;
    //状态描述
    String message;
    //订单内容
    KData data;

    public Param() {
    }

    public Param(String kuaidicom, String kuaidinum, String status, String message, KData data) {
        this.kuaidicom = kuaidicom;
        this.kuaidinum = kuaidinum;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getKuaidicom() {
        return kuaidicom;
    }

    public void setKuaidicom(String kuaidicom) {
        this.kuaidicom = kuaidicom;
    }

    public String getKuaidinum() {
        return kuaidinum;
    }

    public void setKuaidinum(String kuaidinum) {
        this.kuaidinum = kuaidinum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public KData getData() {
        return data;
    }

    public void setData(KData data) {
        this.data = data;
    }
}
