package cn.longchou.wholesale.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.kang.dialog.library.ConfirmDialog;
import cn.kang.dialog.library.ConfirmDialog.OnConfirmListener;
import cn.kang.dialog.library.FinishedDialog;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.utils.DataCleanManager;
import cn.longchou.wholesale.view.ItemMyInformation;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 
* @Description:设置界面 
*
* @author kangkang
*
* @date 2016年6月3日 下午4:58:10 
*
 */
public class SettingActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ItemMyInformation mClearCatche;
	private ItemMyInformation mGoodCommand;
	private ItemMyInformation mLinkUs;
	private ItemMyInformation mShareFriend;
	private ItemMyInformation mAbout;
	private ItemMyInformation mServerProtocl;
	private ItemMyInformation mPushSet;
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			FinishedDialog.dismissDialog();
		};
	};
	private String versionName;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_setting);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mClearCatche = (ItemMyInformation) findViewById(R.id.imi_clear_catch);
		mGoodCommand = (ItemMyInformation) findViewById(R.id.imi_give_good_command);
		mLinkUs = (ItemMyInformation) findViewById(R.id.imi_link_custom);
		mShareFriend = (ItemMyInformation) findViewById(R.id.imi_share_friend);
		mAbout = (ItemMyInformation) findViewById(R.id.imi_about);
		mServerProtocl = (ItemMyInformation) findViewById(R.id.imi_server_protocl);
		mPushSet = (ItemMyInformation) findViewById(R.id.imi_push_set);
		
	}

	@Override
	public void initData() {
		mTitle.setText("设置");
		
		try {
			String totalCacheSize = DataCleanManager.getTotalCacheSize(SettingActivity.this);
			mClearCatche.setInformation("清除本地缓存");
			mClearCatche.setArrowVisible(false);
			mClearCatche.setChooseVisible(true);
			mClearCatche.setChoose(totalCacheSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		//获取版本号
		versionName = getVersionName();
		
		mPushSet.setInformation("推送消息设置");
		mPushSet.setArrowVisible(true);
		mPushSet.setChooseVisible(false);
		
		mGoodCommand.setInformation("给我们好评");
		mGoodCommand.setArrowVisible(true);
		mGoodCommand.setChooseVisible(false);
		
		mLinkUs.setInformation("联系客服");
		mLinkUs.setArrowVisible(true);
		mLinkUs.setChooseVisible(false);
		mLinkUs.setArrowTextVisible(true);
		mLinkUs.setArrowText("400-849-6677");
		
		mShareFriend.setInformation("分享给好友");
		mShareFriend.setArrowVisible(true);
		mShareFriend.setChooseVisible(false);
		
		mAbout.setInformation("关于");
		mAbout.setArrowVisible(true);
		mAbout.setChooseVisible(false);
		mAbout.setArrowTextVisible(true);
		mAbout.setArrowText("V"+versionName);
		mAbout.setArrowTextColor(Color.rgb(51, 51, 51));
		
		mServerProtocl.setInformation("服务协议");
		mServerProtocl.setArrowVisible(true);
		mServerProtocl.setChooseVisible(false);

		
	}
	
	//获取版本号
	private String getVersionName() {
		PackageManager pm=getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		mClearCatche.setOnClickListener(this);
		mGoodCommand.setOnClickListener(this);
		mLinkUs.setOnClickListener(this);
		mShareFriend.setOnClickListener(this);
		mAbout.setOnClickListener(this);
		mServerProtocl.setOnClickListener(this);
		mPushSet.setOnClickListener(this);
		

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
			//清除缓存
		case R.id.imi_clear_catch:
			showClearCatchDialot();
			break;
			//给我们好评
		case R.id.imi_give_good_command:
			Uri uri = Uri.parse("market://details?id="+getPackageName());
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
			//联系客服
		case R.id.imi_link_custom:
			Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4008596677"));
            startActivity(intent1);
			break;
			//分享给好友
		case R.id.imi_share_friend:
			showShare();
			break;
			//关于
		case R.id.imi_about:
			Intent intentAbout=new Intent(SettingActivity.this,AboutActivity.class);
			intentAbout.putExtra("versionName", versionName);
			startActivity(intentAbout);
			break;
			//服务协议
		case R.id.imi_server_protocl:
			Intent intenProtocol=new Intent(SettingActivity.this,ProtocolActivity.class);
			intenProtocol.putExtra("register", "register");
			startActivity(intenProtocol);
			break;
			//消息推送设置
		case R.id.imi_push_set:
//			Intent intentSet =  new Intent();
//			intentSet.setAction("android.intent.action.MAIN");
//			intentSet.setClassName("com.android.settings", "com.android.settings.ManageApplications");
//		    startActivity(intentSet);
			Intent intentSet = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri1 = Uri.fromParts("package", getPackageName(), null);
			intentSet.setData(uri1);
			startActivity(intentSet);
			break;

		default:
			break;
		}

	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("分享");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText("我是分享文本");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
		oks.show(this);
	}

	//清除缓存对话框
	private void showClearCatchDialot() {
		
		ConfirmDialog.showDialog(SettingActivity.this, "提示", "确定清除缓存?", new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				DataCleanManager.clearAllCache(SettingActivity.this);
				String totalCacheSize;
				try {
					totalCacheSize = DataCleanManager.getTotalCacheSize(SettingActivity.this);
					mClearCatche.setChoose(totalCacheSize);
					FinishedDialog.showDialog(SettingActivity.this);
					handler.sendEmptyMessageDelayed(0, 2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});
	}

}
