package com.alibaba.weex.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.weex.MyConstant;
import com.taobao.weex.bridge.JSCallback;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liumeng
 * 微信支付回调
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	public static JSCallback myCallback = null;
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, MyConstant.WECHAT_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			switch (resp.errCode) {
			case 0:// 成功
				//ret
				//   {"status":"success","msg":"支付成功"}
				//status:成功success,失败:error
				//msg:信息描述
				if(myCallback!=null){
					Map<String, Object> map = new HashMap<>();
					map.put("status", "success");
					map.put("msg", "支付成功");
					myCallback.invoke(map);
				}
				break;
			case -1:// 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
				if(myCallback!=null){
					Map<String, Object> map = new HashMap<>();
					map.put("status", "error");
					map.put("msg", "可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等");
					myCallback.invoke(map);
				}
				break;
			case -2:// 用户取消——无需处理。发生场景：用户不支付了，点击取消，返回APP。
				if(myCallback!=null){
					Map<String, Object> map = new HashMap<>();
					map.put("status", "error");
					map.put("msg", "用户取消");
					myCallback.invoke(map);
				}
				break;
			default:
				break;
			}
			finish();
		}
	}

}
