package cn.longchou.wholesale.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.HomePagerAdapter;
import cn.longchou.wholesale.adapter.LicensePlateAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.AttentionAddDelete;
import cn.longchou.wholesale.domain.CarVisitCount;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.MaintenanceReports;
import cn.longchou.wholesale.domain.VehicleDetails;
import cn.longchou.wholesale.domain.VehicleDetails.CarPreview;
import cn.longchou.wholesale.domain.VehicleDetails.Certifications;
import cn.longchou.wholesale.domain.VehicleDetails.CheckReports;
import cn.longchou.wholesale.domain.VehicleDetails.MustKnow;
import cn.longchou.wholesale.domain.VehicleDetails.Primarys;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.SystemUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.ListViewForScrollView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 
 * @Description: 车辆详情
 * 
 * @author kangkang
 * 
 * @date 2016年1月16日 下午1:49:11
 * 
 */
public class VehicleDetailActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mParamsMore;
	private TextView mNoticeMore;
	private TextView mCarDescrip;
	private TextView mOldMoney;
	private TextView mCheckReport;
	private TextView mMachine;
	private TextView mBones;
	private ListViewForScrollView mLvLicense;
	private ViewPager mViewPager;
	private int items[] = { R.drawable.header_image1, R.drawable.header_image2,
			R.drawable.header_image3 };
	private TextView mTvCarId;
	private LinearLayout mLLCarId;
	private TextView mPager;
	private TextView mAttention;
	private TextView mJoinCar;
	private TextView mBudgetNow;
	
	//模拟是否关注
	private boolean isAttention;
	//是否加入购物车
	private boolean isCartCar;
	private TextView mCarInColor;
	private TextView mCarColor;
	private TextView mUseNature;
	private TextView mCarIdentification;
	private TextView mRecordDate;
	private TextView mReleaseDate;
	private TextView mEmissionStand;
	private TextView mAnnual;
	private TextView mCompulsory;
	private TextView mOnTime;
	private TextView mDrivingDistance;
	private TextView mEmmisionStand;
	private TextView mCarLocation;
	private RatingBar mRBExterior;
	private RatingBar mRBTrim;
	private CheckReports checkReports;
	private TextView mManufacturer;
	private TextView mLevel;
	private TextView mBodyWork;
	private TextView mMotor;
	private List<MustKnow> listKnow;
	private LinearLayout mLLMustKnow;
	private TextView mKnow;
	private TextView mMaterial;
	private TextView mMoney;
	private TextView mMoney_Wan;
	private ImageView mMark;
	private TextView mEarnest;
	private Cars cars;
	private VehicleDetails data;
	private List<String> carImags;
	private ScrollView mScrollView;
	private ImageView mScrollUp;
	private TextView mEarnestText;
	LinearLayout mLMaintenance;
	private TextView mDail;
	private TextView mCoupon;
	private ImageView mShare;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mScrollView.fullScroll(ScrollView.FOCUS_UP);
				mScrollView.scrollTo(10, 10);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void initView() {
		setContentView(R.layout.activity_cartcar_detail);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);

		mScrollView = (ScrollView) findViewById(R.id.sv_car_detail);
		
		// 基本参数查看更多
		mParamsMore = (TextView) findViewById(R.id.tv_car_detail_params_more);

		// 办证须知查看更多
		mNoticeMore = (TextView) findViewById(R.id.tv_car_detail_notice_more);

		mCarDescrip = (TextView) findViewById(R.id.tv_car_detail_descripe);

		// 花掉的价格
		mOldMoney = (TextView) findViewById(R.id.tv_car_detail_old_money);

		// 检测报告查看更多
		mCheckReport = (TextView) findViewById(R.id.tv_car_detail_check_report);

		// 机械及电器的查看更多
		mMachine = (TextView) findViewById(R.id.tv_car_detail_machine_more);

		// 骨骼的查看更多
		mBones = (TextView) findViewById(R.id.tv_car_detail_bone_more);

		// 办证须知的内容
		mLvLicense = (ListViewForScrollView) findViewById(R.id.lv_license_plate);

		// 顶部的图片
		mViewPager = (ViewPager) findViewById(R.id.vp_car_detail);

		// 车的id
		mTvCarId = (TextView) findViewById(R.id.tv_car_detail_car_id);

		// 车id的线性布局
		mLLCarId = (LinearLayout) findViewById(R.id.ll_car_id);

		// 显示当前的图片的第几张
		mPager = (TextView) findViewById(R.id.tv_car_detail_car_number);

		// 关注
		mAttention = (TextView) findViewById(R.id.tv_car_detail_attention);
		// 加入购物车
		mJoinCar = (TextView) findViewById(R.id.tv_car_detail_join_cart);
		// 立即订购
		mBudgetNow = (TextView) findViewById(R.id.tv_car_detail_budget_now);
		
		
		/**证件及手续报告*/
		
		//内饰颜色
		mCarInColor = (TextView) findViewById(R.id.tv_car_detail_car_in_color);
		//外观颜色
		mCarColor = (TextView) findViewById(R.id.tv_car_detail_car_color);
		//使用性质
		mUseNature = (TextView) findViewById(R.id.tv_car_detail_use_nature);
		//车辆识别号
		mCarIdentification = (TextView) findViewById(R.id.tv_car_detail_car_identification);
		//登记日期
		mRecordDate = (TextView) findViewById(R.id.tv_car_detail_record_date);
		//出厂日期
		mReleaseDate = (TextView) findViewById(R.id.tv_car_detail_release_date);
		//排放标准
		mEmissionStand = (TextView) findViewById(R.id.tv_car_detail_emission_standards);
		//年审有效期
		mAnnual = (TextView) findViewById(R.id.tv_car_detail_annual_verification);
		//交强险
		mCompulsory = (TextView) findViewById(R.id.tv_car_detail_compulsory);
		
 
		/**车辆的四个描述*/
		
		//上牌时间
		mOnTime = (TextView) findViewById(R.id.tv_car_detail_on_time);
		//行驶里程
		mDrivingDistance = (TextView) findViewById(R.id.tv_car_detail_driving_distance);
		//排放标准
		mEmmisionStand = (TextView) findViewById(R.id.tv_car_detail_emmision_stand);
		//车辆所在地
		mCarLocation = (TextView) findViewById(R.id.car_detail_car_location);
		
		/**检查报告*/
		
		//外观
		mRBExterior = (RatingBar) findViewById(R.id.rb_car_detail_report_exterior);
		//内饰
		mRBTrim = (RatingBar) findViewById(R.id.rb_car_detail_report_trim);
		
		/**基本参数*/
		
		mManufacturer = (TextView) findViewById(R.id.tv_car_detail_params_manufacturer);
		mLevel = (TextView) findViewById(R.id.tv_car_detail_params_level);
		mBodyWork = (TextView) findViewById(R.id.tv_car_detail_params_bodywork);
		mMotor = (TextView) findViewById(R.id.tv_car_detail_params_motor);

		
		/**办证须知*/
		//办证须知其他的线性布局
		mLLMustKnow = (LinearLayout) findViewById(R.id.ll_notice_car_plate_others);
		//办证须知其他的具体内容
		mKnow = (TextView) findViewById(R.id.tv_notice_car_plate_others);
		//办证材料
		mMaterial = (TextView) findViewById(R.id.tv_car_detail_must_know_material);
		
		//车辆的金钱
		mMoney = (TextView) findViewById(R.id.tv_car_detail_money);
		mMoney_Wan = (TextView) findViewById(R.id.tv_car_detail_moeny_wan);
		
		//车辆的优惠活动
		mMark = (ImageView) findViewById(R.id.iv_car_detail_mark);
		
		//定金
		mEarnest = (TextView) findViewById(R.id.tv_car_detail_earnest);
		
		//定金文字
	    mEarnestText = (TextView) findViewById(R.id.tv_car_detail_earnest_text);
		
		mScrollUp = (ImageView) findViewById(R.id.iv_car_detail_scrool_up);
		
		mDail = (TextView) findViewById(R.id.tv_dail_1);

		//可使用优惠卷
		mCoupon = (TextView) findViewById(R.id.tv_car_detail_coupon);


		mLMaintenance = (LinearLayout) findViewById(R.id.ll_car_detail_maintenance);

		mShare= (ImageView) findViewById(R.id.iv_my_title_login);
	}

	@Override
	public void initData() {
		mTitle.setText("车辆详情");
		//给文字甚至删除线
		mOldMoney.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		cars = (Cars) getIntent().getExtras().getSerializable("cars");

//		mCoupon.setText("可以使用优惠卷");
		mCoupon.setText(cars.subTitle+"");
		mShare.setVisibility(View.VISIBLE);
		
		//初始化关注的状态
		setAttention();
		
		//初始化购物车的状态
		setCartCat();
		
		mTvCarId.setText("ID:"+cars.carID);
		
		mCarDescrip.setText(cars.carName);
		boolean isCertified = PreferUtils.getBoolean(getApplicationContext(), "isCertified", false);
		if(isCertified)
		{
			if("已下架".equals(cars.carPrice))
			{
				mMoney.setTextColor(Color.GRAY);
				mMoney.setTextSize(12);
				mMoney_Wan.setVisibility(View.GONE);
			}else{
				
				mMoney_Wan.setVisibility(View.VISIBLE);
			}
			//如果认证显示车的价格
			mMoney.setText(cars.carPrice+"");
			//如果认证显示定金
			mEarnestText.setVisibility(View.VISIBLE);
			mEarnest.setText(cars.carSubscription+"元");
		}else{
			//如果没有认证提示认证后可查看
			mMoney.setText("认证用户可查看价格");
			mMoney.setTextSize(14);
			mMoney_Wan.setVisibility(View.GONE);
			
			//如果没有认证不显示定金
			mEarnestText.setVisibility(View.INVISIBLE);
			
		}
		
		mMark.setImageResource(R.drawable.miao);
		
		//车辆访问次数
		getSerVerCarVisitData();

		getServerData(cars);
		
		mScrollView.fullScroll(ScrollView.FOCUS_UP);
		handler.sendEmptyMessage(1);

		getCarMaintenanceData();
	}

	private void getCarMaintenanceData() {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMaintenance+cars.carID;
		http.send(HttpMethod.POST, url, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Gson gson=new Gson();
				MaintenanceReports json=gson.fromJson(responseInfo.result,MaintenanceReports.class);
				List<MaintenanceReports.MaintenanceReport> maintenanceReports = json.maintenanceReports;
				if(maintenanceReports!=null)
				{
					for(int i=0;i<maintenanceReports.size();i++)
					{
						ImageView image=new ImageView(VehicleDetailActivity.this);
						Glide.with(VehicleDetailActivity.this).load(maintenanceReports.get(i).imgUrl).into(image);
						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT);
						params.bottomMargin=20;
						image.setLayoutParams(params);
						mLMaintenance.addView(image);
					}
				}

			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	//车辆访问次数
	private void getSerVerCarVisitData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestVerticalVisit;
		RequestParams params=new RequestParams();
		if(!TextUtils.isEmpty(token))
		{
			params.addBodyParameter("Token", token);
		}
		params.addBodyParameter("Car_whole_id", cars.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
			    Gson gson=new Gson();
			    CarVisitCount data = gson.fromJson(result, CarVisitCount.class);
			    if(data!=null)
			    {
			    	if(data.success)
			    	{
//			    		UIUtils.showToastSafe(result);
			    	}
			    }
			}
		});
	}

	//初始化购物车的状态
	private void setCartCat() {
		isCartCar=cars.inCart;
		
	}

	//初始化关注的状态
	private void setAttention() {
		isAttention=cars.isFollow;
		
		if(!isAttention){
			mAttention.setText("关注");
			Drawable drawable= getResources().getDrawable(R.drawable.car_detail_attention_no);  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
			mAttention.setCompoundDrawables(null,drawable,null,null);
		}else{
			mAttention.setText("取消关注");
			Drawable drawable= getResources().getDrawable(R.drawable.car_detail_attention);  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
			mAttention.setCompoundDrawables(null,drawable,null,null);
		}

		
	}

	private void getServerData(Cars cars) {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestCarDetail;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("carID", cars.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
//				System.out.println("result:"+result);
				parseData(result);
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		data = gson.fromJson(result, VehicleDetails.class);
		if(null!=data)
		{
			carImags = data.carImags;
			//设置首页的图片
			setImageData();
			Certifications certifications = data.certifications;
			CarPreview carPreview = data.carPreview;
			checkReports = data.checkReports;
			Primarys primarys = data.primarys;
			//基本参数
			setBaseParams(primarys);
			//设置办证须知的数据
			setCertification(certifications);
			//车辆的四个内容
			setForData(carPreview);
			//检查报告
			setCheckReport(checkReports);
			listKnow = VehicleDetails.getInstance().getMustKnows(data.mustKnows);
			//办证须知
			setMastKnow();
			mOldMoney.setText("新车价"+data.originalPrice+"万元");
			
			mScrollView.fullScroll(ScrollView.FOCUS_UP);
		}
		mScrollView.scrollTo(10, 10);
		handler.sendEmptyMessage(1);
		
	}

	private void setImageData() {
		
		
		// 设置图片的来源
		setImageSource();
		
		// 设置图片的百分比
		setViewPagerPercent();

		// 设置车号位置
//		setCarIdPosition();

		mPager.setText("1/"+carImags.size());
		
	}

	//办证须知
	private void setMastKnow() {
	    if(VehicleDetails.getInstance().mOthers!=null)
		{
			mLLMustKnow.setVisibility(View.VISIBLE);
			mKnow.setText(VehicleDetails.getInstance().mOthers);
		}else{
			mLLMustKnow.setVisibility(View.GONE);
		}
		
		if(VehicleDetails.getInstance().mMaterial!=null)
		{
			mMaterial.setText(VehicleDetails.getInstance().mMaterial);
		}
		//遍历list集合去掉其他和办证材料的值
		
		if (listKnow != null || listKnow.size() > 0) {
			LicensePlateAdapter adapter = new LicensePlateAdapter(
					getApplicationContext(), listKnow);
			mLvLicense.setAdapter(adapter);
		}
		
	}

	//基本参数
	private void setBaseParams(Primarys data) {
		mManufacturer.setText(data.厂商);
		mLevel.setText(data.级别);
		mBodyWork.setText(data.车身结构);
		mMotor.setText(data.发动机);
	}

	//检查报告
	private void setCheckReport(CheckReports data) {
		mRBExterior.setRating(data.外观);
		mRBTrim.setRating(data.内饰);
		
	}

	//车辆的四个内容
	private void setForData(CarPreview data) {
		mOnTime.setText(data.上牌时间);;
		mDrivingDistance.setText(data.行驶里程);
		mEmmisionStand.setText(data.排放标准);
		mCarLocation.setText(data.车辆所在地);
		
	}

	//设置办证须知的数据
	private void setCertification(Certifications data) {
		mUseNature.setText(data.使用性质);
		mRecordDate.setText(data.登记日期);
		mReleaseDate.setText(data.出厂日期);
		mEmissionStand.setText(data.排放标准);
		mAnnual.setText(data.年审有效期);
		mCompulsory.setText(data.交强险);
		mCarInColor.setText(data.内饰颜色);
		mCarColor.setText(data.外观颜色);
		mCarIdentification.setText(data.VIN码);
	}

	// 设置车号的位置
	private void setCarIdPosition() {
		RelativeLayout.LayoutParams params = (LayoutParams) mLLCarId
				.getLayoutParams();
		params.topMargin = mViewPager.getLayoutParams().height*9/10;
		mLLCarId.setLayoutParams(params);

	}

	private void setImageSource() {
		List<ImageView> images = new ArrayList<ImageView>();
		for (int i = 0; i < carImags.size(); i++) {
			ImageView image = new ImageView(getApplicationContext());
//			int height = mViewPager.getLayoutParams().height;
//			int width = mViewPager.getLayoutParams().width;
//			image.setMinimumWidth(width);
//			image.setMinimumHeight(height);
//			image.setBackgroundResource(items[i]);
			Glide.with(this).load(carImags.get(i)).placeholder(R.drawable.car_detail_default).into(image);
			images.add(image);
			
		}

		HomePagerAdapter adapter = new HomePagerAdapter(VehicleDetailActivity.this, images,carImags,"image");
		mViewPager.setAdapter(adapter);

	}
	

	// 设置viewpager的轮播图的百分比
	private void setViewPagerPercent() {
		// 窗口高度
		int screenHeight = SystemUtils.getScreenWidth(getApplicationContext()) * 2 / 3;
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mViewPager
				.getLayoutParams();
		params.width = SystemUtils.getScreenWidth(getApplicationContext());
		params.height = screenHeight;
		mViewPager.setLayoutParams(params);
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		mScrollUp.setOnClickListener(this);

		// 基本参数查看更多
		mParamsMore.setOnClickListener(this);

		// 办证须知查看更多
		mNoticeMore.setOnClickListener(this);

		// 检测报告
		mCheckReport.setOnClickListener(this);

		// 机械
		mMachine.setOnClickListener(this);
		
		mDail.setOnClickListener(this);

		// 骨架
		mBones.setOnClickListener(this);
		
		mShare.setOnClickListener(this);

		mAttention.setOnClickListener(this);
		mJoinCar.setOnClickListener(this);
		mBudgetNow.setOnClickListener(this);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				mPager.setText(position + 1 + "/"+carImags.size());
//				image.setOnTouchListener(new TouchListener(carImags.get(position)));

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

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

		case R.id.tv_car_detail_params_more:
			Intent intentParams = new Intent(VehicleDetailActivity.this,
					BaseParamsDetailActivity.class);
			Bundle bundleParams=new Bundle();
			bundleParams.putSerializable("cars", cars);
			intentParams.putExtras(bundleParams);
			startActivity(intentParams);
			break;

		case R.id.tv_car_detail_notice_more:
			Intent intentNotice = new Intent(VehicleDetailActivity.this,
					NoticeDetailActivity.class);
			Bundle bundleNotice=new Bundle();
			bundleNotice.putSerializable("cars", cars);
			intentNotice.putExtras(bundleNotice);
			startActivity(intentNotice);
			break;

		case R.id.tv_car_detail_check_report:
		case R.id.tv_car_detail_machine_more:

			Intent intentCheck = new Intent(VehicleDetailActivity.this,
					TestReportActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("cars", cars);
			intentCheck.putExtras(bundle);
			startActivity(intentCheck);
			break;
		case R.id.tv_car_detail_bone_more:
			//如果有损伤就跳转
			if (checkReports.机械及电器损伤) {
				Intent intentBone = new Intent(VehicleDetailActivity.this,
						TestReportActivity.class);
				Bundle bundle1=new Bundle();
				bundle1.putSerializable("cars", cars);
				intentBone.putExtras(bundle1);
				startActivity(intentBone);
			} //如果没有损伤就显示无，不跳转
			else {
				// 如果没有损伤的情况下。
				mMachine.setText("无");
				mMachine.setTextColor(Color.rgb(51, 51, 51));
			}
			break;
		case R.id.tv_car_detail_attention:
			boolean isLogin = PreferUtils.getBoolean(getApplicationContext(), "isLogin", false);
			if(isLogin)
			{
				showAttention();
			}else{
				Intent intent=new Intent(VehicleDetailActivity.this,LoginActivity.class);
				startActivity(intent);
			}
			
			break;
		case R.id.tv_car_detail_join_cart:

			boolean isLogin1 = PreferUtils.getBoolean(getApplicationContext(), "isLogin", false);
			if(isLogin1)
			{
				showCartCar();
			}else{
				Intent intent=new Intent(VehicleDetailActivity.this,LoginActivity.class);
				startActivity(intent);
			}
			
			break;
		case R.id.tv_car_detail_budget_now:
			boolean isCertified = PreferUtils.getBoolean(getApplicationContext(), "isCertified", false);
			if(isCertified)
			{
				showBudget();
			}else{
				UIUtils.showToastSafe("您没认证不能订购!");
			}
			
			break;
		case R.id.iv_car_detail_scrool_up:
			mScrollView.fullScroll(ScrollView.FOCUS_UP);
			break;
			//分享
		case R.id.iv_my_title_login:
			showShare();
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
		oks.setText("我是分享文本，啦啦啦~");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
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


	//关注的操作
	private void showAttention() {
		//如果关注了,点击的时候让取消关注
		if(isAttention)
		{
			isAttention=false;
			mAttention.setText("关注");
			Drawable drawable= getResources().getDrawable(R.drawable.car_detail_attention_no);  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
			mAttention.setCompoundDrawables(null,drawable,null,null);
			
			//如果关注则取消关注
			deleteAttention();
			
		}//如果没有关注，点击的时候让关注
		else{
			isAttention=true;
			mAttention.setText("取消关注");
			Drawable drawable= getResources().getDrawable(R.drawable.car_detail_attention);  
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());  
			mAttention.setCompoundDrawables(null,drawable,null,null);
			
			//如果没有关注则添加到关注
			addAttention();
		}
	}
	
	//添加到关注
	protected void addAttention() {

		String token = PreferUtils.getString(this, "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestAddFavorite;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", cars.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				AttentionAddDelete data = gson.fromJson(result, AttentionAddDelete.class);
				if(TextUtils.isEmpty(data.errorReason) && data.success)
				{
					UIUtils.showToastSafe("添加关注成功");
					
				}
				
			}
		});
		
	}


	//取消关注
	private void deleteAttention() {

		String token = PreferUtils.getString(this, "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestRemoveFavorite;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", cars.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				AttentionAddDelete data = gson.fromJson(result, AttentionAddDelete.class);
				if(TextUtils.isEmpty(data.errorReason) && data.success)
				{
					UIUtils.showToastSafe("取消关注成功");
					
				}
				
			}
		});
		
	}
	
	//加入购物车的操作
	private void showCartCar() {
		if(isCartCar)
		{
			UIUtils.showToastSafe("已经加入购物车");
		}else{
			isCartCar=true;
			addCartData();
		}
	}
	
	//添加到购物车的接口
	protected void addCartData() {
		String token = PreferUtils.getString(this, "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestAddToCart;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		params.addBodyParameter("carID", cars.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				CartAddDelete data = gson.fromJson(result, CartAddDelete.class);
				if(TextUtils.isEmpty(data.errorMsg))
				{
					UIUtils.showToastSafe("添加购物车成功");
//					CarsManager.getInstance().joinCartCar(cars);
					int TotalCars = PreferUtils.getInt(getApplicationContext(), "totalCars", 0);
					MainActivity.getBadgeViewText().show();
					int cars=TotalCars+1;
					MainActivity.getBadgeViewText().setText(cars+"");
					PreferUtils.putInt(getApplicationContext(), "totalCars", cars);
				}else{
					UIUtils.showToastSafe(data.errorMsg);
				}
			}
		});
		
	}
	
	//立即订购的操作
	private void showBudget() {
		Intent intent =new Intent(this,BudgetConfirmActivity.class);
		Bundle bundle=new Bundle();
		bundle.putSerializable("cars", cars);
		intent.putExtras(bundle);
		startActivity(intent);

	}

}
