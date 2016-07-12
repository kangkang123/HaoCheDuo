package cn.longchou.wholesale;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.activity.MainActivity;
import cn.longchou.wholesale.base.BaseApplication;
import cn.longchou.wholesale.domain.CityLocation;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.IOUtils;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.daimajia.numberprogressbar.OnProgressBarListener;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.uilimageloader.UILImageLoader;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.umeng.common.message.UmengMessageDeviceConfig;
import com.umeng.message.ALIAS_TYPE;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.tag.TagManager;

public class SplashActivity extends Activity implements OnProgressBarListener{

	private RelativeLayout mRl;
	private ProgressBar mPb;
	private TextView mTv;

	private String mVersionName;
	private int mVersionCode;
	private String mDes;
	private String mUrl;
	private NumberProgressBar bnp;
	
	public static final int CODE_UPDATE_APK=1;
	public static final int CODE_ENTER_HOME=2;
	public static final int CODE_URL_ERROR=3;
	public static final int CODE_IO_ERROR=4;
	public static final int CODE_JSON_ERROR=5;

	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//声明mLocationOption对象
	public AMapLocationClientOption mLocationOption = null;
	//声明定位回调监听器
	public AMapLocationListener mLocationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation amapLocation) {

			if (amapLocation != null) {
				if (amapLocation.getErrorCode() == 0) {
					//定位成功回调信息，设置相关消息
					amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
					amapLocation.getLatitude();//获取纬度
					amapLocation.getLongitude();//获取经度
					amapLocation.getAccuracy();//获取精度信息
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date date = new Date(amapLocation.getTime());
					df.format(date);//定位时间
					amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
					amapLocation.getCountry();//国家信息
					amapLocation.getProvince();//省信息
					amapLocation.getCity();//城市信息
					amapLocation.getDistrict();//城区信息
					amapLocation.getStreet();//街道信息
					amapLocation.getStreetNum();//街道门牌号信息
					amapLocation.getCityCode();//城市编码
					amapLocation.getAdCode();//地区编码
					amapLocation.getAoiName();//获取当前定位点的AOI信息
//					UIUtils.showToastSafe("city:"+amapLocation.getCity());
					PreferUtils.putString(getApplicationContext(),"city",amapLocation.getCity());
					Constant.cityLocation=amapLocation.getCity();
				} else {
					//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
					Log.e("AmapError", "location Error, ErrCode:"
							+ amapLocation.getErrorCode() + ", errInfo:"
							+ amapLocation.getErrorInfo());
				}
			}
		}
	};

	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_APK:
				showUpdateDialog();
				break;
			case CODE_ENTER_HOME:
				enterMain();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
				enterMain();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "数据解析异常", Toast.LENGTH_SHORT).show();
				enterMain();
				break;
			case CODE_IO_ERROR:
				Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
				enterMain();
				break;

			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mRl = (RelativeLayout) findViewById(R.id.rl_splash);
		mPb = (ProgressBar) findViewById(R.id.pb_splash);
		mTv = (TextView) findViewById(R.id.tv_splash);
		
		bnp = (NumberProgressBar)findViewById(R.id.pb_splash_number);
		bnp.setOnProgressBarListener(this);
		
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
		mPushAgent.onAppStart();
		mPushAgent.enable(mRegisterCallback);
		String device_token = UmengRegistrar.getRegistrationId(this);
		String phone = PreferUtils.getString(getApplicationContext(), "phone", null);
//		mPushAgent.getTagManager().add(phone);
//		new AddTagTask(phone+"").execute();
		mPushAgent.setAlias(phone, ALIAS_TYPE.KAIXIN);
		
		System.out.println("device_token"+device_token);



//这里以ACCESS_COARSE_LOCATION为例
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			//申请WRITE_EXTERNAL_STORAGE权限
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
					11);//自定义的code
		}
		//定位
		Location();
		//更新检查
		checkVersion();
//		enterMain();
		//设置闪屏渐变
        AlphaAnimation aa=new AlphaAnimation(0.4f, 1);
        aa.setDuration(2000);
        mRl.startAnimation(aa);
		        
		ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		sa.setDuration(1000);
		sa.setFillAfter(true);
		
		AnimationSet as=new AnimationSet(false);
		as.addAnimation(sa);
		as.addAnimation(aa);
		mRl.startAnimation(as);
		
		getData();
//		copyDb("assert.pdf");
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
	}
	
	class AddTagTask extends AsyncTask<Void, Void, String>{

		String tagString;
		String[] tags;
		public AddTagTask(String tag) {
			// TODO Auto-generated constructor stub
			tagString = tag;
			tags = tagString.split(",");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				TagManager.Result result = mPushAgent.getTagManager().add(tags);
				return result.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Fail";
		}
		
		@Override
		protected void onPostExecute(String result) {
			UIUtils.showToastSafe(result);
		}
	}
	
public IUmengRegisterCallback mRegisterCallback = new IUmengRegisterCallback() {
		
		@Override
		public void onRegistered(String registrationId) {
			// TODO Auto-generated method stub
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					updateStatus();
				}
			});
		}
	};
private PushAgent mPushAgent;
	
	private void updateStatus() {
		String pkgName = getApplicationContext().getPackageName();
		String info = String.format("enabled:%s\nisRegistered:%s\nDeviceToken:%s\n" +
				"SdkVersion:%s\nAppVersionCode:%s\nAppVersionName:%s",
				mPushAgent.isEnabled(), mPushAgent.isRegistered(),
				mPushAgent.getRegistrationId(), MsgConstant.SDK_VERSION,
				UmengMessageDeviceConfig.getAppVersionCode(this), UmengMessageDeviceConfig.getAppVersionName(this));
	}
	
    
    private void getData() {
		new Thread(){
			public void run() {
				//获取品牌数据
				getHotBrandData();
				//获取城市数据
				getCityLocationData();
			};
		}.start();
		
	}

	public void Location(){

//初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
		mLocationClient.setLocationListener(mLocationListener);

		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);

		if(mLocationOption.isOnceLocationLatest()){
			mLocationOption.setOnceLocationLatest(true);
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
//如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
		}

//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
//启动定位
		mLocationClient.startLocation();
	}

	private void checkVersion() {
		new Thread(){
			public void run() {
				String url=Constant.RequestVersionUpdate;
				Message msg=handler.obtainMessage();
				long preTime=System.currentTimeMillis();
				try {
					HttpURLConnection conn=(HttpURLConnection)new URL(url).openConnection();
					conn.setReadTimeout(2000);
					conn.setConnectTimeout(2000);
					conn.setRequestMethod("GET");
					if(conn.getResponseCode()==200)
					{
						InputStream is=conn.getInputStream();
						String text=IOUtils.readFromStream(is);
                      
						JSONObject jo=new JSONObject(text);
//						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("version");
//						mDes = jo.getString("des");
//						mUrl = jo.getString("url");
						mUrl="http://www.apk.anzhi.com/data2/apk/201605/31/401720c280d5e6e063ca1a4dde9b1a55_38349700.apk";
						if(getVersionCode()<mVersionCode)
						{
							msg.what=CODE_UPDATE_APK;
						}else{
							msg.what=CODE_ENTER_HOME;
						}
						
					}
				} catch (MalformedURLException e) {
					msg.what=CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					msg.what=CODE_IO_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					msg.what=CODE_JSON_ERROR;
					e.printStackTrace();
				}finally{
					try {
						long curTime=System.currentTimeMillis();
						long useTime=preTime-curTime;
						if(2000>useTime)
						{
							Thread.sleep(2000-useTime);
						}
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.sendMessage(msg);
				}
			}
			
		}.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stopLocation();
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
	
	private void showUpdateDialog() {
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("版本号");
		builder.setMessage("这是最新的版本,欢迎下载！");
		builder.setPositiveButton("立即下载", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoadApk();
				
			}
		});
		
		builder.setNegativeButton("以后再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterMain();
				
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				enterMain();
			}
		});
		builder.show();

	}
	private void downLoadApk() {

		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			mTv.setVisibility(View.VISIBLE);
			bnp.setVisibility(View.VISIBLE);
			mPb.setVisibility(View.INVISIBLE);
			HttpUtils http=new HttpUtils();
			http.download(mUrl, "sdcard/longchoudai.apk", new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(responseInfo.result),
							"application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
				
				@Override
				public void onFailure(HttpException error, String msg) {
					Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
					System.out.println(msg);
				}
				
				@Override
				public void onLoading(long total, long current, boolean isUploading) {
					super.onLoading(total, current, isUploading);
					
					int precent=(int) (100*current/total);
					mTv.setText("下载进度"+precent+"%");
					bnp.setProgress(precent);
				}
			});
		}else{
			Toast.makeText(SplashActivity.this, "sd卡不可用",Toast.LENGTH_SHORT).show();
			enterMain();
		}
		
	}
	
    private void enterMain() {
		Intent intent=new Intent(SplashActivity.this,MainActivity.class);
		startActivity(intent);
		finish();

	}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	enterMain();
    }

	@Override
	public void onProgressChange(int current, int max) {
		// TODO Auto-generated method stub
		if(current == max) {
//            Toast.makeText(getApplicationContext(), getString(R.string.finish), Toast.LENGTH_SHORT).show();
            bnp.setProgress(0);
        }
	}
	
	//获取热门品牌的内容
	public void getHotBrandData(){
		HttpUtils http=new HttpUtils();
		String url=Constant.AvailableBrand;
		http.send(HttpMethod.GET, url, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(!TextUtils.isEmpty(responseInfo.result))
				{
					PreferUtils.putString(getApplicationContext(), "availableBrand", responseInfo.result);
				}
			}
		});
	}
	
	//获取城市的数据
	private void getCityLocationData()
	{
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestCityLocation;
		RequestParams params=new RequestParams();
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resuInfo) {
				if(!TextUtils.isEmpty(resuInfo.result))
				{
					PreferUtils.putString(getApplicationContext(), "cityLocation", resuInfo.result);
				}
				paraseCityData();
			}
		});
	}
	private void paraseCityData() {
		String result = PreferUtils.getString(getApplicationContext(), "cityLocation", null);
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			CityLocation data = gson.fromJson(result, CityLocation.class);
			CityLocation.setCityLocation(data);
			CityLocation.getFirstLocation(data);
		}
	}
}
