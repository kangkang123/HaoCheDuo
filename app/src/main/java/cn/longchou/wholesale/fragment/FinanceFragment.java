package cn.longchou.wholesale.fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.BatchFinancingPlanActivity;
import cn.longchou.wholesale.activity.CarLoanPlanActivity;
import cn.longchou.wholesale.activity.ConsumerLoanPlanActivity;
import cn.longchou.wholesale.activity.FinancialPlanActivity;
import cn.longchou.wholesale.activity.LoginActivity;
import cn.longchou.wholesale.activity.MyFinanceActivity;
import cn.longchou.wholesale.adapter.MyFinanceAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.FinanceBanner;
import cn.longchou.wholesale.domain.FinanceFirst;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.ListViewForScrollView;

public class FinanceFragment extends BaseFragment {

	private LinearLayout mLLMore;
	private ListView mLvFinance;
	private ImageView mBanner;

	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_finance, null);
		
		View header = View.inflate(getActivity(), R.layout.item_finance_fragment_title, null);
		
		mLLMore = (LinearLayout) header.findViewById(R.id.ll_my_finance_more);
		
		mBanner = (ImageView) header.findViewById(R.id.iv_finance_display);
		
		mLvFinance = (ListView) view.findViewById(R.id.lv_finance_display);
		
		mLvFinance.addHeaderView(header);
		
		return view;
	}

	@Override
	public void initData() {
		MyFinanceAdapter adapter=new MyFinanceAdapter(getActivity(), FinanceFirst.getFinanceFirst());
		mLvFinance.setAdapter(adapter);

		//获取金融banner图片
		getImageData();
	}

	//获取金融banner图片
	private void getImageData() {
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.POST, Constant.RequestFinaceHome, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				FinanceBanner json = gson.fromJson(result, FinanceBanner.class);
				Glide.with(getActivity()).load(json.imgUrl).placeholder(R.drawable.finance_banner).into(mBanner);
			}
		});
		
	}

	@Override
	public void initListener() {
		mLLMore.setOnClickListener(this);
		
		mLvFinance.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==1){
					//批售融资方案
					Intent stockIntent=new Intent(getActivity(),BatchFinancingPlanActivity.class);
					startActivity(stockIntent);
				}
				else if(position==2){
					//收车贷方案
					Intent carLoanIntent=new Intent(getActivity(),CarLoanPlanActivity.class);
					startActivity(carLoanIntent);
					}
				else if(position==3)
				{
					//消费贷方案
					Intent consumeIntent=new Intent(getActivity(),ConsumerLoanPlanActivity.class);
					startActivity(consumeIntent);
				}
				else if(position==4)
				{
					//我要理财方案
					Intent financeIntent=new Intent(getActivity(),FinancialPlanActivity.class);
					startActivity(financeIntent);
				}
			}
		});

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.ll_my_finance_more:
			boolean isLogin = PreferUtils.getBoolean(getActivity(), "isLogin", false);
			if(!isLogin)
			{
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				UIUtils.startActivity(intent);
			}else{
				Intent intent=new Intent(getActivity(),MyFinanceActivity.class);
				UIUtils.startActivity(intent);
			}
			break;

		default:
			break;
		}

	}

}
