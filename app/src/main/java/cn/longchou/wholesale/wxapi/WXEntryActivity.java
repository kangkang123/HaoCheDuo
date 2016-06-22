package cn.longchou.wholesale.wxapi;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.global.Constant;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private IWXAPI api;
































	@Override
	protected void onStart() {
		Log.i("227", "wxentryactivity");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID,false);
		api.registerApp(Constant.APP_ID);
		api.handleIntent(getIntent(), this);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		api.handleIntent(intent, this);
	}

	// 微信发送的请求将回调到onReq方法
	@Override
	public void onReq(BaseReq req) {
		Toast.makeText(this, "openid = " + req.openId, Toast.LENGTH_SHORT)
				.show();
		Log.i("226", "wxentryactivity_onreq");
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			// goToGetMsg();
			Log.i("225", "获得消息");
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			// goToShowMsg((ShowMessageFromWX.Req) req);
			Log.i("225", "展示消息");

			break;
		case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
			Log.i("225", R.string.launch_from_wx + "");
			Toast.makeText(this, R.string.launch_from_wx, Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}
	}

	// 发送到微信请求的响应结果将回调到onResp方法
	@Override
	public void onResp(BaseResp resp) {
		// Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT)
		// .show();
		Log.i("226", "wxentryactivity_onresp");
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code,
					Toast.LENGTH_SHORT).show();
		}

		int result = 0;

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = R.string.errcode_cancel;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

}
