package cn.longchou.wholesale.fragment;

import java.util.List;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.meiqia.meiqiasdk.util.MQIntentBuilder;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.FeedBackActivity;
import cn.longchou.wholesale.activity.HelpCenterActivity;
import cn.longchou.wholesale.activity.HelpQuestionActivity;
import cn.longchou.wholesale.activity.QuestionDetailActivity;
import cn.longchou.wholesale.adapter.MyCenterAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.view.ItemHelpCenter;
/**
 * 
* @Description: 客服
*
* @author kangkang
*
* @date 2016年6月6日 上午10:45:19 
*
 */
public class CustomServerFragment extends BaseFragment {

	private ItemHelpCenter mQuestion;
	private ItemHelpCenter mBuyCar;
	private ItemHelpCenter mSellCar;
	private ItemHelpCenter mCertification;
	private ItemHelpCenter mKR;
	private ItemHelpCenter mIntruduction;
	private ImageView mBack;
	private TextView mTitle;
	private TextView mAdvice;
	private TextView mOnLine;
	
	private ListView mLvQuestion;
	List<String> list=null;
	private int ids[]={R.drawable.help_many_question};

	private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_cusom_server, null);
		mBack = (ImageView) view.findViewById(R.id.iv_my_news_back);
	    mTitle = (TextView) view.findViewById(R.id.tv_my_news_title);
		mQuestion = (ItemHelpCenter) view.findViewById(R.id.help_common_question);
	    mBuyCar = (ItemHelpCenter) view.findViewById(R.id.help_buycar_process);
	    mSellCar = (ItemHelpCenter) view.findViewById(R.id.help_apply_inventory);
	    mCertification = (ItemHelpCenter) view.findViewById(R.id.help_apply_certification);
	    mKR = (ItemHelpCenter) view.findViewById(R.id.help_apply_kr);
	    mIntruduction = (ItemHelpCenter) view.findViewById(R.id.help_car_intruduction);
	    
	    mLvQuestion = (ListView) view.findViewById(R.id.lv_question);
	    
	    //意见反馈
	    mAdvice = (TextView) view.findViewById(R.id.tv_custom_advice);
	    //在线咨询
	    mOnLine = (TextView) view.findViewById(R.id.tv_custom_online);
		return view;
	}

	@Override
	public void initData() {
		mTitle.setText("客服中心");
		list = Constant.getManyQuestion();
		MyCenterAdapter adapter=new MyCenterAdapter(getActivity(), list, ids, 1);
		mLvQuestion.setAdapter(adapter);
		
		mBack.setVisibility(View.GONE);
		//更改图标
		mQuestion.changeImage(R.drawable.help_question);
		mBuyCar.changeImage(R.drawable.help_buy_car);
		mSellCar.changeImage(R.drawable.help_sell_car);
		mKR.changeImage(R.drawable.help_apply_inventory);
		mCertification.changeImage(R.drawable.help_apply_certification);
		mIntruduction.changeImage(R.drawable.help_apply_intruduction);
		
		//更改文字
		mQuestion.changeText("常见问题");
		mBuyCar.changeText("买车流程");
		mSellCar.changeText("卖车流程");
		mKR.changeText("申请库存融资流程");
		mCertification.changeText("申请认证用户流程");
		mIntruduction.changeText("隆筹好车介绍");
		
		//去掉下划线
		mQuestion.setLineVisible(false);
		mSellCar.setLineVisible(false);
		mCertification.setLineVisible(false);
		mIntruduction.setLineVisible(false);

	}

	@Override
	public void initListener() {
		//各个条目设置点监听
		mQuestion.setOnClickListener(this);
		mBuyCar.setOnClickListener(this);
		mSellCar.setOnClickListener(this);
		mCertification.setOnClickListener(this);
		mIntruduction.setOnClickListener(this);
		mAdvice.setOnClickListener(this);
		mOnLine.setOnClickListener(this);
		
		mLvQuestion.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent=new Intent(getActivity(),QuestionDetailActivity.class);
				String string = list.get(position);
				intent.putExtra("title", string);
				intent.putExtra("help", "常见问题");
				intent.putExtra("position", position);
				startActivity(intent);
				
			}
		});

	}

	@Override
	public void processClick(View v) {
		Intent intent=new Intent(getActivity(),HelpQuestionActivity.class);
		switch (v.getId()) {
		case R.id.help_common_question:
			intent.putExtra("title", "常见问题");
			startActivity(intent);
			break;
		case R.id.help_buycar_process:
			intent.putExtra("title", "买车流程");
			startActivity(intent);
			break;
		case R.id.help_apply_inventory:
			intent.putExtra("title", "卖车流程");
			startActivity(intent);
			break;
		case R.id.help_apply_kr:
			intent.putExtra("title", "申请库存融资流程");
			startActivity(intent);
			break;
		case R.id.help_apply_certification:
			intent.putExtra("title", "申请认证用户流程");
			startActivity(intent);
			break;
		case R.id.help_car_intruduction:
			intent.putExtra("title", "隆筹好车介绍");
			startActivity(intent);
			break;
		case R.id.tv_custom_advice:
			Intent intent1=new Intent(getActivity(),FeedBackActivity.class);
			startActivity(intent1);
			break;
		case R.id.tv_custom_online:
			conversationWrapper();
			break;

		default:
			break;
		}
	}

	private void conversationWrapper() {
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
					WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
		} else {
			conversation();
		}
	}
	private void conversation() {
		Intent intent = new MQIntentBuilder(getActivity()).build();
		startActivity(intent);
	}

}
