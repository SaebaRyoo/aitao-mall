package com.mall.utils;

public class AlipayConfig {

	public static String app_id = "ccc";//appid
	
	public static String merchant_private_key = "xxxx";// 商户秘钥

	public static String alipay_public_key = "xxx";// 支付宝公钥
	
	public static String notify_url = "http://localhost:7001/alipay/alipayNotifyNotice.action";
	
	public static String return_url = "http://localhost:7001/alipay/alipayReturnNotice.action";
	
	public static String sign_type = "RSA2";
	
	public static String charset = "utf-8";
	
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";// 支付宝开发环境网关, 生产环境去掉dev

	public static String format = "json";
}
