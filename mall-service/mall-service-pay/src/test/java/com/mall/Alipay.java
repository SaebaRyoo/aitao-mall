package com.mall;


import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayOpenOperationOpenbizmockBizQueryModel;
import com.alipay.api.request.AlipayOpenOperationOpenbizmockBizQueryRequest;
import com.alipay.api.request.AlipayOpenPublicTemplateMessageIndustryModifyRequest;
import com.alipay.api.response.AlipayOpenOperationOpenbizmockBizQueryResponse;
import com.alipay.api.response.AlipayOpenPublicTemplateMessageIndustryModifyResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class Alipay {
    //private static final String APP_PUBLIC_KEY = "app_public.key";

    //appid
    private static final String APP_ID = "2021000121642104";

    //商户应用私钥 pkcs8格式
    private static final String APP_PRIVATE_KEY = "app_private.key";

    //支付宝公钥，不是商户应用公钥
    private static final String ALIPAY_PUBLIC_KEY = "ali_public.key";

    //支付宝网关
    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";

    //签名方式
    private static final String SIGN_TYPE = "RSA2";

    //字符编码格式
    private static final String CHARSET = "utf-8";
    private static final String FORMAT = "json";


    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/notify_url.jsp";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://localhost:8080/alipay.trade.page.pay-JAVA-UTF-8/return_url.jsp";


    @Test
    public void goPay() {

        try {
            // 1. 创建AlipayClient实例
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, getKey(APP_PRIVATE_KEY), FORMAT, CHARSET, getKey(ALIPAY_PUBLIC_KEY), SIGN_TYPE);
            //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.open.public.template.message.industry.modify
            AlipayOpenPublicTemplateMessageIndustryModifyRequest request = new AlipayOpenPublicTemplateMessageIndustryModifyRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数
            //此次只是参数展示，未进行字符串转义，实际情况下请转义
            request.setBizContent("  {" +
                    "    \"primary_industry_name\":\"IT科技/IT软件与服务\"," +
                    "    \"primary_industry_code\":\"10001/20102\"," +
                    "    \"secondary_industry_code\":\"10001/20102\"," +
                    "    \"secondary_industry_name\":\"IT科技/IT软件与服务\"" +
                    " }");
            AlipayOpenPublicTemplateMessageIndustryModifyResponse response = alipayClient.execute(request);
            if (response.isSuccess()) {
                System.out.println("调用成功。");
            } else {
                System.out.println("调用失败，原因：" + response.getMsg() + "，" + response.getSubMsg());
            }
        } catch (Exception e) {
            System.out.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    private String getKey(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return null;
        }
    }

    private AlipayOpenOperationOpenbizmockBizQueryRequest getRequest() {
        // 初始化Request，并填充Model属性。实际调用时请替换为您想要使用的API对应的Request对象。
        AlipayOpenOperationOpenbizmockBizQueryRequest request = new AlipayOpenOperationOpenbizmockBizQueryRequest();
        AlipayOpenOperationOpenbizmockBizQueryModel model = new AlipayOpenOperationOpenbizmockBizQueryModel();
        model.setBizNo("test");
        request.setBizModel(model);
        return request;
    }
}
