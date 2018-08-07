package com.alibaba.weex;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.weex.utils.MD5Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author liumeng
 *
 * 微信支付
 */
public class PayActivity extends Activity {
	private PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private String prepay_id = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prepay_id = getIntent().getStringExtra("prepay_id");
		req = new PayReq();
		msgApi.registerApp(MyConstant.WECHAT_APP_ID);
		genPayReq();
		sendPayReq();
	}
	
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(MyConstant.WEIXIN_API_KEY);
		String appSign = MD5Util.md516(sb.toString());
		return appSign;
	}
	

	private String genNonceStr() {
		Random random = new Random();
		return MD5Util.md516(String.valueOf(random.nextInt(10000)));
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	private void genPayReq() {
		req.appId = MyConstant.WECHAT_APP_ID;
		req.partnerId = MyConstant.WEIXIN_MCH_ID;
		req.prepayId = prepay_id;
		req.packageValue = "prepay_id="+prepay_id;
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		
		req.sign = genAppSign(signParams);
	}
	
	private void sendPayReq() {
		msgApi.registerApp(MyConstant.WECHAT_APP_ID);
		msgApi.sendReq(req);
		
		finish();
	}
}

