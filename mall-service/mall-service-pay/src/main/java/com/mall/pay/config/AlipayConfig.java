package com.mall.pay.config;

import org.springframework.beans.factory.annotation.Value;

public class AlipayConfig {

	// APPID
	@Value("${alipay.app_id}")
	public static String app_id;//appid

	//商户秘钥
	@Value("${alipay.merchant_private_key}")
	public static String merchant_private_key;

	//支付宝公钥
	@Value("${alipay.alipay_public_key}")
	public static String alipay_public_key;

	//支付状态异步通知
	@Value("${alipay.notify_url}")
	public static String notify_url;

	//支付状态同步通知
	@Value("${alipay.return_url}")
	public static String return_url;

	//签名方式
	@Value("${alipay.sign_type}")
	public static String sign_type;

	@Value("${alipay.charset}")
	public static String charset;

	// 支付宝开发环境网关, 生产环境去掉dev
	@Value("${alipay.gatewayUrl}")
	public static String gatewayUrl;

	@Value("${alipay.format}")
	public static String format;
}
