package cn.longchou.wholesale.activity;

import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.StockFinanceAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.FinanceInfo;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.MyFinance;
import cn.longchou.wholesale.domain.StockFinance;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.view.ExpandableListViewForScrollView;
import cn.longchou.wholesale.view.ItemFinanceGroup;
import cn.longchou.wholesale.view.ListViewForScrollView;
/**
 * 
* @Description: 我的金融中的各种方案
*
* @author kangkang
*
* @date 2016年3月8日 下午2:01:07 
*
 */
public class FinancePlanActivcity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mName;
	private TextView mPhone;
	private TextView mPlan;
	private ListViewForScrollView mListView;
	private TextView mDetail;
	//金融的类型
	private String type;
	private String id;
	private String plan;
	private LinearLayout mLine;
	private List<List<FinanceInfo>> list;
	private StockFinanceAdapter adapter;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_stock_finance);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mName = (TextView) findViewById(R.id.tv_stock_finace_name);
		mPhone = (TextView) findViewById(R.id.tv_stock_finance_phone);
		mPlan = (TextView) findViewById(R.id.tv_stock_finance_plan);
		
		mListView = (ListViewForScrollView) findViewById(R.id.lv_stock_finance_content);
		mLine = (LinearLayout) findViewById(R.id.ll_finace);

		//拨打电话号码
		mDetail = (TextView) findViewById(R.id.tv_detail);
	}

	@Override
	public void initData() {
        
		plan = getIntent().getStringExtra("plan");
		
		if("库存融资".equals(plan))
		{
			mTitle.setText("我的库存融资");
			type = "kucun";
		}
		else if("收车贷".equals(plan))
		{
			mTitle.setText("我的收车贷");
			type="shouche";
		}
		else if("消费贷".equals(plan))
		{
			mTitle.setText("消费贷");
			type="xiaofei";
		}
		
		String myInfo = Constant.myInfo;
		Gson gson=new Gson();
		LoginValidate data = gson.fromJson(myInfo,LoginValidate.class);
		if(null!=data)
		{
			if(data.name!=null)
			{
				mName.setText(data.name);
			}else{
				mName.setText("待认证");
			}
			mPhone.setText(data.phoneNumber);
		}
		
		mPlan.setText(plan);
		
		
		getServerData1();
//		String res="{'financeNos': ['444','SC201604050001-01'],'lists': [{'applyTime': '2016年03月29日','financeNo': '444','totalMonth': '12个月','payStatus': '还款中','rzAmount': '2,000.00','dhAmount': '1,000.00','yhAmount': '1,000.00'},{'applyTime': '2016年04月05日','financeNo': 'SC201604050001-01','totalMonth': '12个月','payStatus': '还款中','rzAmount': '5,500.00','dhAmount': '5,000.00','yhAmount': '500.00'}]}";
//      String res="{'financeNos':['C05130011510KR54','C05130011510KR55','C05130011510KR56'],'lists':[{'applyTime':'2016年04月10日','financeNo':'C05130011510KR54','totalMonth':'0个月','payStatus':'还款中','rzAmount':'600,000.00','dhAmount':'0.00','yhAmount':'0.00'},{'applyTime':'2015年05月12日','financeNo':'C05130011510KR55','totalMonth':'3个月','payStatus':'已还款','rzAmount':'900,000.00','dhAmount':'0.00','yhAmount':'929,700.00'},{'applyTime':'2015年07月22日','financeNo':'C05130011510KR56','totalMonth':'2个月','payStatus':'已还款','rzAmount':'600,000.00','dhAmount':'0.00','yhAmount':'619,800.00'}]}";
	}
	
	private void getServerData1() {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestmyFinance;
		RequestParams params=new RequestParams();
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		params.addBodyParameter("Token", token);
		System.out.println(token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resuInfo) {
			
				String result=resuInfo.result;
				Constant.myFinance=result;
				parseData(result);
			}
		});
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		MyFinance data = gson.fromJson(result, MyFinance.class);
		if(null!=data)
		{
			if("库存融资".equals(plan))
			{
				mTitle.setText("我的库存融资");
				id=data.kunCunDaiInfo.kunCunDaiId;
			}
			else if("收车贷".equals(plan))
			{
				mTitle.setText("我的收车贷");
				id=data.shouCheDaiInfo.shouCheDaiId;
			}
			else if("消费贷".equals(plan))
			{
				mTitle.setText("消费贷");
				id=data.xiaoFeiDaiInfo.id;
			}
		}
		if(null!=id)
		{
			getServerData();
			
		}
		
	}
	

	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url = Constant.RequestFinanceDetail;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		params.addBodyParameter("type", type);
		params.addBodyParameter("id", id);
		System.out.println(token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				FinanceInfo.clearData();
				String result=resultInfo.result;
				Gson jso=new Gson();
				FinanceInfo json = jso.fromJson(result, FinanceInfo.class);
				FinanceInfo.setIsOpenData(json.lists.size());
				list = json.getFinanceChild(type);
				if(null!=json)
				{
					adapter = new StockFinanceAdapter(getApplicationContext(), json.financeNos,list);
					mListView.setAdapter(adapter);
				}
				
			}
		});
		
	}


	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mDetail.setOnClickListener(this);
		mPlan.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.tv_detail:
			Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "4008596677"));
            startActivity(intent1);
			break;
		case R.id.tv_stock_finance_plan:
			if("库存融资".equals(plan))
			{
				Intent stockIntent=new Intent(getApplicationContext(),BatchFinancingPlanActivity.class);
				startActivity(stockIntent);
			}
			else if("收车贷".equals(plan))
			{
				Intent carLoanIntent=new Intent(getApplicationContext(),CarLoanPlanActivity.class);
				startActivity(carLoanIntent);
			}
			else if("消费贷".equals(plan))
			{
				Intent consumeIntent=new Intent(getApplicationContext(),ConsumerLoanPlanActivity.class);
				startActivity(consumeIntent);
			}
			break;

		default:
			break;
		}

	}

}
