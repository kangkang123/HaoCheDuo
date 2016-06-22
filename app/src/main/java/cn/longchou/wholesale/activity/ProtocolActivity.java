package cn.longchou.wholesale.activity;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProtocolActivity extends BaseActivity {

	private TextView mTvTitle;
	private ImageView mIvBack;
	private WebView mWebView;

	@Override
	public void initView() {

		setContentView(R.layout.activity_protocol);
		mTvTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mIvBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mWebView = (WebView) findViewById(R.id.wv_protocol);
		
	}

	@Override
	public void initData() {

		
		WebSettings mSetting=mWebView.getSettings();
		
		String register = getIntent().getStringExtra("register");
		String redir = getIntent().getStringExtra("redir");
		if(!TextUtils.isEmpty(register))
		{
			mTvTitle.setText("注册协议");
			mWebView.loadUrl(Constant.RequestRigisterProtocol);
		}else{
			mTvTitle.setText("隆筹质保");
			mWebView.loadUrl(Constant.RequestZhiBao);
		}
		if(!TextUtils.isEmpty(redir))
		{
			mTvTitle.setText("优惠活动");
			mWebView.loadUrl(redir);
		}
	}

	@Override
	public void initListener() {

		mIvBack.setOnClickListener(this);
	}

	@Override
	public void processClick(View v) {

		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;

		default:
			break;
		}
	}

}
