package cn.longchou.wholesale.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.IOUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class AboutActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mVersion;
	private TextView mDail;
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				UIUtils.showToastSafe("您需要更新了。");
				break;
			case 1:
				UIUtils.showToastSafe("您已经是最新版本");
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_about);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);

		mVersion = (TextView) findViewById(R.id.tv_version_name);
		mDail = (TextView) findViewById(R.id.tv_dail);
		
	}

	@Override
	public void initData() {
		mTitle.setText("关于");
		String versionName = getIntent().getStringExtra("versionName");
		mVersion.setText("版本号:V"+versionName);

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mDail.setOnClickListener(this);
		mVersion.setOnClickListener(this);

	}
	
	private void checkVersion() {
		new Thread(){
			@Override
			public void run() {
				try {
					String url=Constant.RequestVersionUpdate;
					HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
					conn.setReadTimeout(2000);
					conn.setConnectTimeout(2000);
					conn.setRequestMethod("GET");
					if(conn.getResponseCode()==200)
					{
						InputStream is=conn.getInputStream();
						String text=IOUtils.readFromStream(is);
	                  
						JSONObject jo=new JSONObject(text);
						int mVersionCode = jo.getInt("version");
						Message msg=handler.obtainMessage();
						if(getVersionCode()<mVersionCode)
						{
							msg.what=0;
							
						}else{
							msg.what=1;
						}
						
						handler.sendMessage(msg);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();

	}
	
	private int getVersionCode() {
		PackageManager pm=getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.tv_dail:
			Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4008596677"));
            startActivity(intent1);
			break;

		case R.id.tv_version_name:
			checkVersion();
			break;
		default:
			break;
		}

	}

}
