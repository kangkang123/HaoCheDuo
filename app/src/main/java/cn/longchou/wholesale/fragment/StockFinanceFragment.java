package cn.longchou.wholesale.fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyNewsFragmentAdapter;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.MyFinance;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.DonutProgress;
import cn.longchou.wholesale.view.RoundProgressBar;
/**
 * 
* @Description: 库存融资和收车贷的Fragment
*
* @author kangkang
*
* @date 2016年1月25日 下午2:32:07 
*
 */
public class StockFinanceFragment extends BaseFragment {

	private TextView mTime;
	private TextView mStockMoney;
	private TextView mStockRepayMoney;
	private TextView mCarLoan;
	private TextView mLoanRepayMoney;
	private TextView mAvailMoney;
	private TextView mLineCredit;
//	private DonutProgress mProgress;
	private RoundProgressBar mProgress;

    private int i;
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				runTime();
				break;

			default:
				break;
			}
		};
	};
	private String trustavailable;
	private String trustTotal;
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = UIUtils.inflate(R.layout.fragment_stock_finance);
		mTime = (TextView) view.findViewById(R.id.tv_stock_finance_time);
		mStockMoney = (TextView) view.findViewById(R.id.tv_stock_finance_money);
		mStockRepayMoney = (TextView) view.findViewById(R.id.tv_stock_finance_repay_money);
		mCarLoan = (TextView) view.findViewById(R.id.tv_car_loan_money);
		mLoanRepayMoney = (TextView) view.findViewById(R.id.tv_car_loan_repay_money);
		mAvailMoney = (TextView) view.findViewById(R.id.tv_stock_finance_avail_money);
		mLineCredit = (TextView) view.findViewById(R.id.tv_stock_finace_line_of_credit);
//		mProgress = (DonutProgress) view.findViewById(R.id.pb_stock);
		mProgress = (RoundProgressBar) view.findViewById(R.id.pb_progress);
		return view;
	}

	protected void runTime() {
		
		if(!TextUtils.isEmpty(trustavailable))
		{
			String total = trustTotal.replace(",","");
			String avail=trustavailable.replace(",","");
			float left=Float.parseFloat(total)-Float.parseFloat(avail);
			int progress=getHandlerProgress(left+"");
//			int progress=getHandlerProgress(trustavailable);
//			if(i<=Float.parseFloat(trustavailable))
//			UIUtils.showToastSafe(progress+"");
			if(i<=progress)
			{
				mProgress.setProgress(i++);
			}
		}
		
		
	}

	@Override
	public void initData() {
		
//		String result = Constant.myFinance;
//		//只用当另一个Fragment中获取到数据时直接解析，如果没有获取数据则获取数据
//		if(TextUtils.isEmpty(result))
//		{
			getServerData();
//		}else{
//			
//			parseData(result);
//		}
		
		TimeBack();
		
	}
	
	private void getServerData() {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestmyFinance;
		RequestParams params=new RequestParams();
		final String token = PreferUtils.getString(getActivity(), "token", null);
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resuInfo) {
			
				String result=resuInfo.result;
				Constant.myFinance=result;
				parseData(result);
				System.out.println("token"+token);
				System.out.println(result);
			}
		});
	}
	
	private void parseData(String result) {
		Gson gson=new Gson();
		MyFinance data = gson.fromJson(result, MyFinance.class);
		if(null!=data)
		{
			//授信到期日
			mTime.setText(TextUtils.isEmpty(data.trustInfo.limitDate) ? "无" : (data.trustInfo.limitDate+""));
			//库存融资
			mStockMoney.setText(TextUtils.isEmpty(data.kunCunDaiInfo.kunCunDai) ? "0.00元" : data.kunCunDaiInfo.kunCunDai+"元");
			//库存融资待还金额
			mStockRepayMoney.setText(TextUtils.isEmpty(data.kunCunDaiInfo.kunCunDaiHuan) ? "0.00元" :data.kunCunDaiInfo.kunCunDaiHuan+"元");
			//收车贷
			mCarLoan.setText(TextUtils.isEmpty(data.shouCheDaiInfo.shouCheDai) ? "0.00元" :data.shouCheDaiInfo.shouCheDai+"元");
			//收车贷待还金额
			mLoanRepayMoney.setText(TextUtils.isEmpty(data.shouCheDaiInfo.shouCheDaiHuan) ? "0.00元" :data.shouCheDaiInfo.shouCheDaiHuan+"元");
			
			trustavailable = data.trustInfo.available;
			
			//可用额度
			mAvailMoney.setText(TextUtils.isEmpty(trustavailable) ? "0.00元" :trustavailable+"元");
			
			
			trustTotal = data.trustInfo.trustTotal;
			
			//授信额度
			mLineCredit.setText(TextUtils.isEmpty(data.trustInfo.trustTotal) ? "0.00元" :data.trustInfo.trustTotal+"元");
			if(!TextUtils.isEmpty(data.trustInfo.trustTotal))
			{
				int max=getHandlerProgress(data.trustInfo.trustTotal.replace(",", ""));
//				mProgress.setMax((int) Float.parseFloat(data.trustInfo.trustTotal));
//				UIUtils.showToastSafe(max+"");
				mProgress.setMax(max);
			}
//			mProgress.setUnfinishedStrokeWidth(22);
		}
		
	}
	
	private int getHandlerProgress(String progress)
	{
		int end = progress.indexOf(".");
		int len=0;
		int value=0;
		if(end>2)
		{
			len=end-2;
			value=(int) ((int) Float.parseFloat(progress)/(Math.pow(10, len)));
			return value;
		}else{
			return (int) Float.parseFloat(progress);
		}
		
	}

	private void TimeBack() {
		Timer timer = new Timer();
		timer.schedule(new Task(), 100, 100);
	}
	class Task extends TimerTask {
		@Override
		public void run() {
			handler.sendEmptyMessage(1);
		}

	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			TimeBack();
		}
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
