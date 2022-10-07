package com.mall.shipping.pojo;

public class KData {
    //平台订单ID
    String orderId;
    //订单状态，0：'下单成功'；1：'已接单'；2：'收件中'；9：'用户主动取消'；10：'已取件'；11：'揽货失败'；12：'已退回'；13：'已签收'；14：'异常签收'；99：'订单已取消'
    String status;
    //快递员姓名
    String courierName;
    //快递员电话
    String courierMobile;
    //重量
    String weight;
    //运费
    String freight;

    public KData() {
    }

    public KData(String orderId, String status, String courierName, String courierMobile, String weight, String freight) {
        this.orderId = orderId;
        this.status = status;
        this.courierName = courierName;
        this.courierMobile = courierMobile;
        this.weight = weight;
        this.freight = freight;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCourierName() {
        return courierName;
    }

    public void setCourierName(String courierName) {
        this.courierName = courierName;
    }

    public String getCourierMobile() {
        return courierMobile;
    }

    public void setCourierMobile(String courierMobile) {
        this.courierMobile = courierMobile;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }
}
