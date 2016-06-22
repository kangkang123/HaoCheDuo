package cn.longchou.wholesale.activity;

import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.IllegalDisposalAdapter;
import cn.longchou.wholesale.adapter.LicensePlateAdapter;
import cn.longchou.wholesale.adapter.CompensationFeeAdapter;
import cn.longchou.wholesale.adapter.ResidencePermitAdapter;
import cn.longchou.wholesale.adapter.VehicleLicenseFeeAdapter;
import cn.longchou.wholesale.adapter.VehiclesExamAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LicensePlate;
import cn.longchou.wholesale.domain.MustNotice;
import cn.longchou.wholesale.domain.MustNotice.CompensationFee;
import cn.longchou.wholesale.domain.MustNotice.IllegalDisposal;
import cn.longchou.wholesale.domain.MustNotice.LicenseFee;
import cn.longchou.wholesale.domain.MustNotice.ResidencePermit;
import cn.longchou.wholesale.domain.MustNotice.VehiclesExam;
import cn.longchou.wholesale.domain.VehicleDetails.MustKnow;
import cn.longchou.wholesale.domain.VehicleDetails;
import cn.longchou.wholesale.domain.VehicleLicenseFee;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.view.ListViewForScrollView;
/**
 * 
* @Description: 办证须知界面
*
* @author kangkang
*
* @date 2016年1月18日 上午11:18:16 
*
 */
public class NoticeDetailActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mNoticeTitle;
	private TextView mNoticeMore;
	
	private ListViewForScrollView mLvLicense;
	private ListViewForScrollView mVehiclefee;
	private ListViewForScrollView mIlleageDiapal;
	private ListViewForScrollView mResidence;
	private ListViewForScrollView mFillPersmit;
	private ListViewForScrollView mAnnualVerfication;
	private LinearLayout mPlateOthers;
	private TextView mTvPlateOthers;
	
	private TextView mMaterial;
	
	private LinearLayout mAnnualOthers;
	private TextView mTvAnnualOthers;
	
	private String plateOthers="我是车牌的其他情况";
	
	private String annualOthers="我是车牌的其他情况haha";
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_notice_detail);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mNoticeTitle = (TextView) findViewById(R.id.tv_car_detail_notice_title);
		mNoticeMore = (TextView) findViewById(R.id.tv_car_detail_notice_more);
		
		//办证须知的内容
		mLvLicense = (ListViewForScrollView) findViewById(R.id.lv_license_plate);
		
		//车辆办证费
		mVehiclefee = (ListViewForScrollView) findViewById(R.id.lv_vehicle_license_fee);
		
		//违章处理
		mIlleageDiapal = (ListViewForScrollView) findViewById(R.id.lv_illeage_dispal);
		
		//居住证、暂住证
		mResidence = (ListViewForScrollView) findViewById(R.id.lv_residence_permit);
		
		//补证费用
		mFillPersmit = (ListViewForScrollView) findViewById(R.id.lv_fill_permit_money);
		
		//车辆年审
		mAnnualVerfication = (ListViewForScrollView) findViewById(R.id.lv_annual_verfication);
		
		
		//车牌号码的其他的布局
		mPlateOthers = (LinearLayout) findViewById(R.id.ll_notice_car_plate_others);
		
		//车牌号码其他的显示的内容
		mTvPlateOthers = (TextView) findViewById(R.id.tv_notice_car_plate_others);
		
		//车辆年审其他的布局
		mAnnualOthers = (LinearLayout) findViewById(R.id.ll_notice_annual_verfication_others);
		
		//车辆年审其他的显示的内容
		mTvAnnualOthers = (TextView) findViewById(R.id.tv_notice_annual_verfication_others);
		
		//办证材料
		mMaterial = (TextView) findViewById(R.id.tv_car_detail_must_know_material);
		
	}

	@Override
	public void initData() {
		mTitle.setText("办证须知");
		mNoticeMore.setVisibility(View.INVISIBLE);
		
		Cars cars=(Cars) getIntent().getExtras().getSerializable("cars");
		
		//给补证须知设置值
		setMastKnow();
		getServerData(cars);
	}

	//办证须知设置值
	private void setMastKnow() {
		List<MustKnow> listKnow = VehicleDetails.getInstance().getMust();

	    if(VehicleDetails.getInstance().mOthers!=null)
		{
	    	mPlateOthers.setVisibility(View.VISIBLE);
	    	mTvPlateOthers.setText(VehicleDetails.getInstance().mOthers);
		}else{
			mPlateOthers.setVisibility(View.GONE);
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

	private void getServerData(Cars cars) {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMust;
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
				parseData(result);
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		MustNotice data = gson.fromJson(result, MustNotice.class);
		//补证费
		List<CompensationFee> bzf = data.bzf;
		setCompensationFee(bzf);
		
		//车辆办证费
		List<LicenseFee> clbzf = data.clbzf;
		setLicenseFee(clbzf);
		
		//违章处理
		List<IllegalDisposal> wzcl = data.wzcl;
		setIllegalDisposal(wzcl);
		
		//居住证
	    List<ResidencePermit> jzz = data.jzz;
	    setResidencePermit(jzz);
	    
	    //车辆年审
	    List<VehiclesExam> clns = data.clns;
	    setVehiclesExam(clns);
		
	    mNoticeTitle.setText(data.shortCarPlate);
	    
	    //如果其他的内容为空则不显示其他的布局否则显示其他的内容-车辆年审
	    String other = data.other;
  		if(other==null)
  		{
  			mAnnualOthers.setVisibility(View.GONE);
  		}else{
  			mAnnualOthers.setVisibility(View.VISIBLE);
  			mTvAnnualOthers.setText(other);
  		}
	}

	//车辆年审
	private void setVehiclesExam(List<VehiclesExam> clns) {
		if(clns!=null && clns.size()>0)
		{
			VehiclesExamAdapter adapter=new VehiclesExamAdapter(getApplicationContext(), clns);
			mAnnualVerfication.setAdapter(adapter);
		}
		
	}

	//居住证
	private void setResidencePermit(List<ResidencePermit> jzz) {
		if(jzz!=null && jzz.size()>0)
		{
			ResidencePermitAdapter adapter=new ResidencePermitAdapter(getApplicationContext(), jzz);
			mResidence.setAdapter(adapter);
		}
		
	}

	//违章处理
	private void setIllegalDisposal(List<IllegalDisposal> wzcl) {
		if(wzcl!=null && wzcl.size()>0)
		{
			IllegalDisposalAdapter adapter=new IllegalDisposalAdapter(getApplicationContext(), wzcl);
			mIlleageDiapal.setAdapter(adapter);
		}
		
	}

	//车辆办证费
	private void setLicenseFee(List<LicenseFee> clbzf) {
		if(clbzf!=null && clbzf.size()>0)
		{
			VehicleLicenseFeeAdapter adapter=new VehicleLicenseFeeAdapter(getApplicationContext(), clbzf);
			mVehiclefee.setAdapter(adapter);
		}
		
	}

	//补证费
	private void setCompensationFee(List<CompensationFee> bzf) {
		if(bzf!=null && bzf.size()>0)
		{
			CompensationFeeAdapter adapter=new CompensationFeeAdapter(getApplicationContext(), bzf);
			mFillPersmit.setAdapter(adapter);
		}
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);

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
