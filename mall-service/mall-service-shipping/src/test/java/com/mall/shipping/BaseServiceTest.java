package com.mall.shipping;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kuaidi100.sdk.api.*;
import com.kuaidi100.sdk.cloud.CloudBase;
import com.kuaidi100.sdk.contant.*;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.*;
import com.kuaidi100.sdk.request.cloud.COrderCancelReq;
import com.kuaidi100.sdk.request.cloud.COrderQueryReq;
import com.kuaidi100.sdk.request.cloud.COrderReq;
import com.kuaidi100.sdk.response.QueryTrackMapResp;
import com.kuaidi100.sdk.utils.SignUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BaseServiceTest {

    private String key = "key";
    private String customer = "customer";
    private String secret = "secret";
    private String siid = "siid";
    private String userid = "32164379287";
    private String tid = "aitao";
    private String secret_key = "n3WwJZIL1ErAnru9vh";
    private String secret_secret = "62db2c2e33ba48dbbb4ed8bbbcc4186b";
    /**
     * 查询物流轨迹
     */
    @Test
    public void testQueryTrack() throws Exception{

        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(CompanyConstant.YT);
        queryTrackParam.setNum("YT9383342193097");
        queryTrackParam.setPhone("17725390266");
        String param = new Gson().toJson(queryTrackParam);

        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(customer);
        queryTrackReq.setSign(SignUtils.querySign(param ,key,customer));

        IBaseClient baseClient = new QueryTrack();
        System.out.println(baseClient.execute(queryTrackReq));
    }

    /**
     * 快递信息地图轨迹
     */
    @Test
    public void testQueryMapView() throws Exception{

        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(CompanyConstant.YD);
        queryTrackParam.setNum("4311159956248");
        queryTrackParam.setPhone("17725390266");
        queryTrackParam.setFrom("河北保定市");
        queryTrackParam.setTo("湖南岳阳市");
        queryTrackParam.setResultv2("1");
        String param = new Gson().toJson(queryTrackParam);

        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(customer);
        queryTrackReq.setSign(SignUtils.querySign(param ,key,customer));

        IBaseClient baseClient = new QueryTrackMap();
        HttpResult result = baseClient.execute(queryTrackReq);

        QueryTrackMapResp queryTrackMapResp = new Gson().fromJson(result.getBody(),QueryTrackMapResp.class);
        System.out.println(queryTrackMapResp);
    }


    /**
     * 发送短信
     */
    @Test
    public void testSendSms() throws Exception{
        SendSmsReq sendSmsReq = new SendSmsReq();
        sendSmsReq.setCallback("http://www.baidu.com");
        Map<String,String> content = new HashMap<String, String>();
        content.put("username","测试用户");
        sendSmsReq.setContent(new Gson().toJson(content));
        sendSmsReq.setPhone("15994708912");
        sendSmsReq.setSeller("贵司名称");
        sendSmsReq.setUserid(userid);
        sendSmsReq.setTid(tid);
        sendSmsReq.setSign(SignUtils.smsSign(key,userid));

        IBaseClient sendSms = new SendSms();
        System.out.println(sendSms.execute(sendSmsReq));
    }



    /**
     * C端查询运力
     */
    @Test
    public void testCOrderQuery() throws Exception {
        COrderQueryReq cOrderQueryReq = new COrderQueryReq();
        //cOrderQueryReq.setAddress("广东省深圳市南山区华强南");
        //寄件人地址
        cOrderQueryReq.setSendManPrintAddr("浙江省杭州市西湖区中大银座");
        //收件人地址
        cOrderQueryReq.setRecManPrintAddr("广东省深圳市深圳市南山区科技南十二路2号金蝶软件园");
        cOrderQueryReq.setSecret_key(secret_key);
        cOrderQueryReq.setSecret_code(CloudApiCodeConstant.ORDER_QUERY_EXPRESS);
        cOrderQueryReq.setSecret_sign(SignUtils.cloudSign(secret_key, secret_secret));

        IBaseClient cloudBase = new CloudBase();
        System.out.println(cloudBase.execute(cOrderQueryReq));
    }

    /**
     * c端寄件
     */
    @Test
    public void testCOrder() throws Exception {
        COrderReq cOrderReq = new COrderReq();
        // 快递公司的编码，一律用小写字母，见《快递公司编码》
        cOrderReq.setCom(CompanyConstant.JD);
        //收件人姓名
        cOrderReq.setSendManName("小红");
        //收件人的手机号，手机号和电话号二者其一必填
        cOrderReq.setSendManMobile("15966666666");
        //收件人所在完整地址，如广东深圳市深圳市南山区科技南十二路2号金蝶软件园
        cOrderReq.setSendManPrintAddr("广东深圳市南山区金蝶软件园");
        //寄件人姓名
        cOrderReq.setRecManName("小明");
        //寄件人的手机号，手机号和电话号二者其一必填
        cOrderReq.setRecManMobile("15966666666");
        //寄件人所在的完整地址，如广东深圳市深圳市南山区科技南十二路2号金蝶软件园B10
        cOrderReq.setRecManPrintAddr("广东深圳市福田区华强南");
        //callBackUrl订单信息回调
        cOrderReq.setCallBackUrl("http://www.baidu.com");
        //物品名称,例：文件
        cOrderReq.setCargo("文件");
        //支付方式，SHIPPER: 寄方付（默认），CONSIGNEE: 到付
        cOrderReq.setPayment("SHIPPER");
        //备注
        cOrderReq.setRemark("测试下单，待会取消");
        //物品总重量KG，不需带单位，例：1.5
        cOrderReq.setWeight("1");
        //签名用随机字符串
        cOrderReq.setSalt("123456");
        cOrderReq.setSecret_key(secret_key);
        cOrderReq.setSecret_code(CloudApiCodeConstant.ORDER);
        cOrderReq.setSecret_sign(SignUtils.cloudSign(secret_key,secret_secret));

        IBaseClient cloudBase = new CloudBase();
        System.out.println(cloudBase.execute(cOrderReq));
    }

    /**
     * 需要企业认证
     * @throws Exception
     */
    @Test
    public void testCheckOrderPro() throws Exception {
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        //查询的快递公司的编码， 一律用小写字母 下载编码表格
        queryTrackParam.setCom(CompanyConstant.JD);
        //查询的快递单号， 单号的最小长度6个字符，最大长度32个字符
        queryTrackParam.setNum("JDX010538105326");
        //收、寄件人的电话号码（手机和固定电话均可，只能填写一个，顺丰速运和丰网速运必填，其他快递公司选填。如座机号码有分机号，分机号无需传入。）
        queryTrackParam.setPhone("15966666666");
        //出发地城市
        queryTrackParam.setFrom("广东深圳市南山区金蝶软件园");
        //目的地城市，到达目的地后会加大监控频率
        queryTrackParam.setTo("广东深圳市福田区华强南");
        //添加此字段表示开通行政区域解析功能。空：关闭（默认），1：开通行政区域解析功能以及物流轨迹增加物流状态名称 4: 开通行政解析功能以及物流轨迹增加物流高级状态名称、状态值并且返回出发、目的及当前城市信息
        queryTrackParam.setResultv2("");
        //返回格式：0：json格式（默认），1：xml，2：html，3：text
        queryTrackParam.setShow("0");
        //返回结果排序:desc降序（默认）,asc 升序
        queryTrackParam.setOrder("desc");
        QueryTrackReq queryTrackReq = new QueryTrackReq();
        queryTrackReq.setCustomer(customer);
        queryTrackReq.setSign(SignUtils.querySign(JSON.toJSONString(queryTrackParam), secret_key, customer));
        queryTrackReq.setParam(JSON.toJSONString(queryTrackParam));

        //queryTrackReq
        QueryTrack queryTrack = new QueryTrack();
        System.out.println(queryTrack.execute(queryTrackReq));;
    }

    /**
     * c端取消寄件
     */
    @Test
    public void testCOrderCancel() throws Exception {
        COrderCancelReq cOrderCancelReq = new COrderCancelReq();
        cOrderCancelReq.setTaskId("4ACF6D2B08A38E8AA01E1B9272E62455");
        cOrderCancelReq.setOrderId("25483336");
        cOrderCancelReq.setCancelMsg("测试单");
        cOrderCancelReq.setSecret_key(secret_key);
        cOrderCancelReq.setSecret_code(CloudApiCodeConstant.ORDER_CANCEL);
        cOrderCancelReq.setSecret_sign(SignUtils.cloudSign(secret_key,secret_secret));

        IBaseClient cloudBase = new CloudBase();
        System.out.println(cloudBase.execute(cOrderCancelReq));
    }


}
