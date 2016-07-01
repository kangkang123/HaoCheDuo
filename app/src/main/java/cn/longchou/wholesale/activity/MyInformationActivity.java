package cn.longchou.wholesale.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.Text;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.HeadUpLoad;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.SaveMyInfo;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.IOUtils;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.utils.UploadUtil;
import cn.longchou.wholesale.view.ItemMyInformation;
import cn.longchou.wholesale.view.SelectPicPopupWindow;

public class MyInformationActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ItemMyInformation mLoginName;
	private ItemMyInformation mName;
	private ItemMyInformation mCarId;
	private ItemMyInformation mPassword;
	private ItemMyInformation mPhone;
	private TextView mConfirm;
	private ItemMyInformation mAddressManage;
	private ItemMyInformation mCarCertaion;
	private ItemMyInformation mSex;
	private RelativeLayout mRLHead;
	
	//自定义的弹出框类
	SelectPicPopupWindow menuWindow;
	
	// 图库获取图片
	private static final int REQUEST_CODE_PICK_IMAGE = 2;
	// 相机获取图片
	private static final int REQUEST_CODE_CAPTURE_CAMEIA = 3;
	//修改登录名
	private static final int REQUEST_CODE_CHANGE_NAME=4;

	// 创建一个以当前系统时间为名称的文件，防止重复
	private File tempFile = new File(Environment.getExternalStorageDirectory(),
			getPhotoFileName());

	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
		return sdf.format(date) + ".png";
	}
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_my_information);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mConfirm = (TextView) findViewById(R.id.tv_my_information);
		
		mLoginName = (ItemMyInformation) findViewById(R.id.im_login_name);
		mName = (ItemMyInformation) findViewById(R.id.im_name);
		mCarId = (ItemMyInformation) findViewById(R.id.im_card_id);
		mPassword = (ItemMyInformation) findViewById(R.id.im_change_password);
		mPhone = (ItemMyInformation) findViewById(R.id.im_change_phone);
		
		//地址管理
		mAddressManage = (ItemMyInformation) findViewById(R.id.im_address_manage);
		//车商认证
		mCarCertaion = (ItemMyInformation) findViewById(R.id.im_car_certain);
		//性别
		mSex = (ItemMyInformation) findViewById(R.id.im_sex);
		//头像
		mRLHead = (RelativeLayout) findViewById(R.id.rl_head);
		mHead = (ImageView) findViewById(R.id.iv_my_head_login);
	}

	@Override
	public void initData() {
		mTitle.setText("个人信息");
		
		mCarCertaion.setInformation("车商认证");
		mCarCertaion.setArrowVisible(true);
		mCarCertaion.setArrowText("立即认证");
		mCarCertaion.setArrowTextVisible(true);
		mCarCertaion.setChooseVisible(false);
		mCarCertaion.setArrowTextColor(Color.rgb(51, 51, 51));
		
		mAddressManage.setInformation("地址管理");
		mAddressManage.setArrowVisible(true);
		mAddressManage.setChooseVisible(false);
		
		mSex.setInformation("性别");
		mSex.setArrowVisible(true);
		mSex.setChooseVisible(false);
		mSex.setArrowText("保密");
		mSex.setArrowTextVisible(true);
		mSex.setArrowTextColor(Color.rgb(214, 214, 214));

		getServerData();
		
		String result = PreferUtils.getString(getApplicationContext(), "myInfo", null);

		//设置登录名
		mLoginName.setInformation("登录名");
		mLoginName.setArrowVisible(true);
		mLoginName.setChooseVisible(false);

		mLoginName.setArrowTextVisible(true);
		mLoginName.setArrowTextColor(Color.rgb(214, 214, 214));
		
		//设置姓名
		mName.setInformation("姓名");
		
		//设置身份证
		mCarId.setInformation("身份证号");
		
		//更改密码
		mPassword.setInformation("更改密码");
		mPassword.setChooseVisible(false);
		mPassword.setArrowVisible(true);
		
		//更改手机号码
		mPhone.setInformation("更改手机号码");
		mPhone.setChooseVisible(false);
		mPhone.setArrowVisible(true);

	}

	private void getServerData() {
		String token=PreferUtils.getString(getApplicationContext(),"token",null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestGetMyInfo;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token",token);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				paraseData(result);
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	//解析个人信息的内容
	private void paraseData(String result) {
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			SaveMyInfo data = gson.fromJson(result,SaveMyInfo.class);
			if(data!=null)
			{
//				mLoginName.setChoose(data.phoneNumber);
				if(TextUtils.isEmpty(data.nickName)){
					String phone=PreferUtils.getString(getApplicationContext(),"phone",null);
					mLoginName.setArrowText(phone);

				}else{
					mLoginName.setArrowText(data.nickName);
					Constant.personName=data.nickName;
				}
				if(!TextUtils.isEmpty(data.sex)){
					mSex.setArrowText(data.sex);
					Constant.sexChoose=data.sex;
				}
				if(!TextUtils.isEmpty(data.imgUrl)){
					Glide.with(MyInformationActivity.this).load(data.imgUrl).placeholder(R.drawable.me).into(mHead);
				}
			}
		}
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mPassword.setOnClickListener(this);
		mPhone.setOnClickListener(this);
		
		mAddressManage.setOnClickListener(this);
		mCarCertaion.setOnClickListener(this);
		mSex.setOnClickListener(this);
		mRLHead.setOnClickListener(this);
		mLoginName.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		case R.id.im_change_password:
			Intent intent=new Intent(MyInformationActivity.this,ChangePasswordActivity.class);
			startActivity(intent);
			break;
		case R.id.im_change_phone:
			Intent intent1=new Intent(MyInformationActivity.this,VerificationCodeActivity.class);
			startActivity(intent1);
			break;
			//地址管理
		case R.id.im_address_manage:
			Intent intentAddress=new Intent(MyInformationActivity.this,AddressManagerActivity.class);
			startActivity(intentAddress);
			break;
			//车商认证
		case R.id.im_car_certain:
			break;
			//性别
		case R.id.im_sex:
			Intent intentSex=new Intent(MyInformationActivity.this,SexChooseActivity.class);
			startActivityForResult(intentSex, 1);
			break;
			//头像
		case R.id.rl_head:
			//实例化SelectPicPopupWindow
			menuWindow = new SelectPicPopupWindow(MyInformationActivity.this, itemsOnClick);
			//显示窗口
			menuWindow.showAtLocation(findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
			break;
			case R.id.im_login_name:
		        Intent intentChangeName=new Intent(MyInformationActivity.this,ChangeNameActivity.class);
				startActivityForResult(intentChangeName,REQUEST_CODE_CHANGE_NAME);
				break;
		default:
			break;
		}

	}
	
	 //为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){
		public void onClick(View v) {
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.btn_take_photo:
				takePhoto();
				break;
			case R.id.btn_pick_photo:	
				pickPhoto();
				break;
			case R.id.btn_cancel:
				
				break;
			default:
				break;
			}
		}
    };
	private ImageView mHead;
    
    private void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		Intent getImageByCamera = new Intent(
				"android.media.action.IMAGE_CAPTURE");
		startActivityForResult(getImageByCamera,
				REQUEST_CODE_CAPTURE_CAMEIA);
	}

    private void pickPhoto() {
		// 本地相册
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		    case 1:
			     mSex.setArrowText(Constant.sexChoose);
				 saveMyInfo("sex",Constant.sexChoose);
			break;
			case REQUEST_CODE_CHANGE_NAME:
                if(!TextUtils.isEmpty(Constant.personName)){
					mLoginName.setArrowText(Constant.personName);
				}
				break;

		default:
			break;
		}
		if(null!=data)
		{
			if (requestCode == REQUEST_CODE_PICK_IMAGE)
			{
				Uri uri = data.getData();
				try {
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
					mHead.setImageBitmap(bitmap);
					uploadFile(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA )
			{
				Uri uri = data.getData();
				if(uri == null)
				{
					Bundle bundle = data.getExtras();
					if (bundle != null)
					{
						Bitmap  photo = (Bitmap) bundle.get("data"); //get bitmap

						mHead.setImageBitmap(photo);
						uploadFile(photo);

					} else {
						UIUtils.showToastSafe("error");
						return;
					}
				}
				else{
					//to do find the path of pic by uri
				}
			}
		}
	}

	//修改个人信息
	private void saveMyInfo(String key,String value){
		String token=PreferUtils.getString(getApplicationContext(),"token",null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestSaveMyInfo;
		RequestParams params=new RequestParams();
		params.addBodyParameter(key,value);
		params.addBodyParameter("Token",token);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				Gson gson=new Gson();
				SaveMyInfo json = gson.fromJson(result, SaveMyInfo.class);
				UIUtils.showToastSafe(json.msg);
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	//图片上传
	private void uploadFile(final Bitmap bitmap)
	{
		new Thread(){
			@Override
			public void run() {
				try {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
					FileOutputStream fis = new FileOutputStream(tempFile);
					fis.write(baos.toByteArray());
					fis.flush();
					String token=PreferUtils.getString(getApplicationContext(),"token",null);
					String url=Constant.RequestHead;
					Map<String, String> params = new HashMap<String,String>();
					params.put("Token", token);

					Map<String, File> files = new HashMap<String,File>();
					files.put("uploadifys", tempFile);
					String request = UploadUtil.post(url, params, files);
					Gson gson=new Gson();
					HeadUpLoad json = gson.fromJson(request, HeadUpLoad.class);
					if(json.urls.size()>0||json.urls!=null)
					{
						String imageUrl=json.urls.get(0);
						saveMyInfo("imgUrl",imageUrl);
//						UIUtils.showToastSafe("头像上传成功");
					}else{
						UIUtils.showToastSafe("头像上传失败");
					}
					System.out.print(request);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
