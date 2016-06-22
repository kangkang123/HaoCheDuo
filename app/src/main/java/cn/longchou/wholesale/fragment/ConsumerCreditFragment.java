package cn.longchou.wholesale.fragment;

import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.sqlite.CursorUtils.FindCacheSequence;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseFragment;
import cn.longchou.wholesale.domain.MyFinance;
import cn.longchou.wholesale.fragment.StockFinanceFragment.Task;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.DonutProgress;
/**
 * 
* @Description: 消费贷
*
* @author kangkang
*
* @date 2016年1月25日 下午3:49:15 
*
 */
public class ConsumerCreditFragment extends BaseFragment {

	private TextView mAvailMoney;
	private TextView mLineCredit;
	private TextView mConsumer;
	private TextView mRepayMoney;
	private TextView mStockLimit;
	private TextView mRepayLimit;
	private DonutProgress mProgress;
	
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
	private String toPay;
	private String totalLimit;
	
	@Override
	public View initView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = UIUtils.inflate(R.layout.fragment_consumer_credit);
		mAvailMoney = (TextView) view.findViewById(R.id.tv_consumer_credit_avail_money);
		mLineCredit = (TextView) view.findViewById(R.id.tv_consumer_credit_line_of_credit);
		
		mConsumer = (TextView) view.findViewById(R.id.tv_consumer_credit_money);
		mRepayMoney = (TextView) view.findViewById(R.id.tv_consumer_credit_repay_money);
		mStockLimit = (TextView) view.findViewById(R.id.tv_consumer_credit_stock_limit);
		mRepayLimit = (TextView) view.findViewById(R.id.tv_consumer_credit_repay_limit);
		
		mProgress = (DonutProgress) view.findViewById(R.id.pb_cunsumer);
		return view;
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
		String token = PreferUtils.getString(getActivity(), "token", null);
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
			}
		});
	}
	
	private void parseData(String result) {
		Gson gson=new Gson();
		MyFinance data = gson.fromJson(result, MyFinance.class);
		if(null!=data)
		{
			toPay = data.xiaoFeiDaiInfo.toPay;
			//可用额度
			mAvailMoney.setText(TextUtils.isEmpty(toPay) ? "0.00元" :toPay+"元");
			//授信额度
			mLineCredit.setText(TextUtils.isEmpty(data.xiaoFeiDaiInfo.totalLimit) ? "0.00元" : data.xiaoFeiDaiInfo.totalLimit+"元");
			totalLimit = data.xiaoFeiDaiInfo.totalLimit;
			//消费贷
			mConsumer.setText(TextUtils.isEmpty(data.xiaoFeiDaiInfo.totalLimit) ? "0.00元" : data.xiaoFeiDaiInfo.totalLimit+"元");
			//待还金额
			mRepayMoney.setText(TextUtils.isEmpty(toPay) ? "0.00元" : toPay+"元");
			//融资期限
			mStockLimit.setText(TextUtils.isEmpty(data.xiaoFeiDaiInfo.totalMonth) ? "-" :data.xiaoFeiDaiInfo.totalMonth+"月");
			//待还期数
			mRepayLimit.setText(TextUtils.isEmpty(data.xiaoFeiDaiInfo.remainMonth) ? "-" :data.xiaoFeiDaiInfo.remainMonth+"月");

			if(!TextUtils.isEmpty(toPay))
			{
//				mProgress.setMax((int) Float.parseFloat(data.xiaoFeiDaiInfo.totalLimit));
				int max=getHandlerProgress(toPay);
				mProgress.setMax(max);
			}
			mProgress.setUnfinishedStrokeWidth(22);
		}
		
	}
	
	protected void runTime() {
		
		if(!TextUtils.isEmpty(totalLimit))
		{
			int progress=getHandlerProgress(totalLimit);
			if(i<=progress)
			{
				mProgress.setProgress(i++);
			}
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
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		// TODO Auto-generated method stub
		super.onHiddenChanged(hidden);
		if(!hidden)
		{
			TimeBack();
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
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
