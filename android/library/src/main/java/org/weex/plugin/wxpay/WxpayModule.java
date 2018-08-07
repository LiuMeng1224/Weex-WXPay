package org.weex.plugin.wxpay;


import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by Administrator on 2017/10/27.
 * @author liumeng
 * 微信支付功能集成
 */

public class WxpayModule extends WXModule {

    //appid 微信申请的appid
    @JSMethod(uiThread = true)
    public void registerAPP(String appid) {



    }

    @JSMethod(uiThread = true)
    public void WXPay(String msg) {

    }
}
