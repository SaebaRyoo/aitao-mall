package com.mall.shipping.pojo;


public class KResult {
    boolean result;
    String returnCode;
    String message;

    public KResult() {
    }

    public KResult(boolean result, String returnCode, String message) {
        this.result = result;
        this.returnCode = returnCode;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
