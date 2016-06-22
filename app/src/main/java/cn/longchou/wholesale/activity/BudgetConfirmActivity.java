package cn.longchou.wholesale.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.crypto.spec.IvParameterSpec;

import org.apache.http.params.HttpParams;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;
import cn.kang.dialog.library.InputDialog;
import cn.kang.dialog.library.InputDialog.OnInputDialogListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.BudgetConfirmAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.dialog.ConfirmListDialog;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.OrderPayState;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.ListViewForScrollView;
import cn.longchou.wholesale.view.MySwitch;
import cn.longchou.wholesale.view.MySwitch.OnCheckedChangeListener;
/**
 * 
* @Description: 提交购物车订单的支付确认界面,此处开始生成订单
*
* @author kangkang
*
* @date 2016年1月21日 下午3:13:09 
*
 */
public class BudgetConfirmActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mCarMany;
	private TextView mPayMoney;
	private TextView mFinish;
	
	private ListView mLvBudgetConfirm;
	
	private static final int RESULT_ORDER=1;
	
	
	private float TotalMoney;
	
	
	private Cars cars;
	private String orderNumber;
	
	private StringBuffer sb=new StringBuffer();
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_buget_confirm);
		
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		//选中车辆
		mCarMany = (TextView) findViewById(R.id.tv_budget_confirm_car_many);
		//应付金额
		mPayMoney = (TextView) findViewById(R.id.tv_budget_confirm_should_money);
		//提交订单
		mFinish = (TextView) findViewById(R.id.tv_bugdet_confirm_commit);
		
		//选的的内容
		mLvBudgetConfirm = (ListView) findViewById(R.id.lv_budget_confirm);

	}

	@Override
	public void initData() {
		//用于保存选中的车辆信息
		List<CarsInfo> list=new ArrayList<CarsInfo>();
		Bundle bundle = getIntent().getExtras();
		if(null!=bundle)
		{
			cars = (Cars) bundle.getSerializable("cars");
		}
		
		mTitle.setText("确认订单");
		
		if(null!=cars)
		{
			list.clear();
			CarsInfo clone = CarsInfo.clone(cars);
			list.add(clone);
		}else{
			list.clear();
			List<CarsInfo> cartCarList = CarsManager.getInstance().getCartCarList();
			for(int i=0;i<cartCarList.size();i++)
			{
				CarsInfo carsInfo = cartCarList.get(i);
				if(carsInfo.isSelect)
				{
					list.add(carsInfo);
				}
			}
		}
		
		//选中车辆
		mCarMany.setText("共"+list.size()+"辆车");
		for(int i=0;i<list.size();i++)
		{
			CarsInfo carsInfo = list.get(i);
			float money = carsInfo.carSubscription;
			TotalMoney+=money;
			if(i!=list.size()-1)
			{
				sb.append(carsInfo.carID+",");
			}else{
				sb.append(carsInfo.carID);
			}
		}
		mPayMoney.setText(TotalMoney+"0元");
		
		if(list!=null)
		{
			if(cars!=null)
			{
				//从订单详情进来的
				BudgetConfirmAdapter adapter=new BudgetConfirmAdapter(this,list,-1);
				mLvBudgetConfirm.setAdapter(adapter);
			}else{
				//从购物车进来的
				BudgetConfirmAdapter adapter=new BudgetConfirmAdapter(this,list,0);
				mLvBudgetConfirm.setAdapter(adapter);
			}
		}
		
	}
	
	@Override
	public void initListener() {
        mBack.setOnClickListener(this);
        mFinish.setOnClickListener(this);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			setResult(3);
			finish();
            break;
		
		case R.id.tv_bugdet_confirm_commit:
			orderNumber = generateOrders();
			UIUtils.showToastSafe("订单生成中...");
			submitOrder();
			break;
		default:
			break;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			setResult(3);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//生成订单
	private void submitOrder() {
        String ids=sb.toString();
//        UIUtils.showToastSafe(ids);
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestAddOrders;
		RequestParams params=new RequestParams();
		params.addBodyParameter("orderNo", orderNumber);
		params.addBodyParameter("Token", token);
		params.addBodyParameter("totalMoney", TotalMoney+"");
		params.addBodyParameter("carIds", ids);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				Gson gson=new Gson();
				OrderPayState data = gson.fromJson(result, OrderPayState.class);
				if(null!=data)
				{
					if(data.success)
					{
						Intent intent=new Intent(BudgetConfirmActivity.this,PayMoneyActivity.class);
						intent.putExtra("money", TotalMoney);
						intent.putExtra("order", orderNumber);
						startActivityForResult(intent, 1);
					}else{
						UIUtils.showToastSafe(data.errorMsg);
					}
				}
				
				
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_ORDER:
			setResult(1);
			finish();
			break;

		default:
			break;
		}
	}
	
	//生成订单编号
	private String generateOrders()
	{
        java.util.Date current=new java.util.Date();
        java.text.SimpleDateFormat sdf=new java.text.SimpleDateFormat("yyyyMMddhhmmss"); 
        String c=sdf.format(current)+generateRandom();
        return c;
	
	}
	//生成随机数
	private String generateRandom()
	{
		StringBuilder str=new StringBuilder();
		Random random=new Random();
		for(int i=0;i<8;i++)
		{
			int nextInt = random.nextInt(10);
			str.append(nextInt);
		}
		return str.toString();
	}
}
