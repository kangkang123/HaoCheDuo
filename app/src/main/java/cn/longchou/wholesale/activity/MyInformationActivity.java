package cn.longchou.wholesale.activity;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
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
		
		String result = PreferUtils.getString(getApplicationContext(), "myInfo", null);
		if(!TextUtils.isEmpty(result))
		{
			Gson gson=new Gson();
			LoginValidate data = gson.fromJson(result,LoginValidate.class);
			if(data!=null)
			{
				mLoginName.setChoose(data.phoneNumber);
				
				if(data.isCertified)
				{
					mConfirm.setText("已经验证");
					
				}else{
					mConfirm.setText("未验证");
				}
				
				if(!data.isCertified)
				{
					mName.setChoose("待认证");
				}else{
					mName.setChoose(data.name);
				}
				
				if(!data.isCertified)
				{
					mCarId.setChoose("待认证");
				}else{
					mCarId.setChoose(data.idNo);
				}
			}
		}
		//设置登录名
		mLoginName.setInformation("登录名");
		mLoginName.setArrowVisible(true);
		mLoginName.setChooseVisible(false);
		mLoginName.setArrowText("保密");
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
				startActivity(intentChangeName);
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

}
