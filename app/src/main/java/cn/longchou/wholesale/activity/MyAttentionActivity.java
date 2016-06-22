package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mingle.widget.LoadingView;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.CartCarAdapter;
import cn.longchou.wholesale.adapter.MyAttentionAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.AttentionAddDelete;
import cn.longchou.wholesale.domain.CartAddDelete;
import cn.longchou.wholesale.domain.HomePage;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.ItemSelect;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
/**
 * 
* @Description: 我的关注的页面
*
* @author kangkang 
*
* @date 2016年1月4日 下午4:53:47 
*
 */
public class MyAttentionActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private SwipeMenuListView mLvAttention;
	private List<Cars> list;
	private MyAttentionAdapter adapter;
	private RelativeLayout mRlAttentionNo;
	private LoadingView loading;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_my_attention);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mLvAttention = (SwipeMenuListView) findViewById(R.id.lv_my_attention);
		
		mRlAttentionNo = (RelativeLayout) findViewById(R.id.rl_attention_no);
		loading = (LoadingView) findViewById(R.id.loadView);
		
	}

	@Override
	public void initData() {
		mTitle.setText("我的关注");
		
		getServerData();
		
		
		
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem carItem = new SwipeMenuItem(
						getApplicationContext());
				carItem.setBackground(new ColorDrawable(Color.rgb(237, 109,
						0)));
				carItem.setWidth(dp2px(60));
				carItem.setTitle("求购");
				carItem.setTitleSize(12);
				carItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(carItem);
				
				SwipeMenuItem buyItem = new SwipeMenuItem(
						getApplicationContext());
				buyItem.setBackground(new ColorDrawable(Color.rgb(255,
						181, 13)));
				buyItem.setTitleSize(12);
				buyItem.setWidth(dp2px(60));
				buyItem.setTitle("购物车");
				buyItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(buyItem);
				
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(255,
						52, 13)));
				deleteItem.setTitleSize(12);
				deleteItem.setWidth(dp2px(60));
				deleteItem.setTitle("删除");
				deleteItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(deleteItem);
			}
		};
		
		// set creator
		mLvAttention.setMenuCreator(creator);

		// step 2. listener item click event
		mLvAttention.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				Cars cars = list.get(position);
				switch (index) {
				case 0:
					Intent intent=new Intent(MyAttentionActivity.this,MyBuyActivity.class);
					startActivity(intent);
					break;
					
				//加入购物车
				case 1:
					
					//如果没有在购物车中则添加到购物车
					addCartCar(cars);
					
					break;
				case 2:
					deleteAttention(cars,position);
					
					break;
				
				}
			}
		});
	}
	
	//加入购物车
	protected void addCartCar(final Cars cars) {
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
					if(TotalCars+1!=0)
					{
						MainActivity.getBadgeViewText().show();
						MainActivity.getBadgeViewText().setText(TotalCars+1+"");
						PreferUtils.putInt(getApplicationContext(), "totalCars", TotalCars+1);
					}
				}else{
					UIUtils.showToastSafe(data.errorMsg);
				}
			}
		});
		
	}

	protected void deleteAttention(Cars cars, final int position) {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestRemoveFavorite;
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
					UIUtils.showToastSafe("取消关注成功");
					list.remove(position);
					
					
					//取消关注的时候调用。判断当前的列表是否为空
					if(list.size()>0)
					{
						mLvAttention.setAdapter(adapter);
						mRlAttentionNo.setVisibility(View.INVISIBLE);
					}else{
						mLvAttention.setVisibility(View.INVISIBLE);
						mRlAttentionNo.setVisibility(View.VISIBLE);
					}
					
					adapter.notifyDataSetChanged();
				}
				
			}
		});
		
	}

	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestMyFavorite;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result = resultInfo.result;
				paraseData(result);
				
			}
		});
		
	}

	protected void paraseData(String result) {
		Gson gson=new Gson();
		HomePage data = gson.fromJson(result, HomePage.class);
		loading.setVisibility(View.GONE);
		list = data.cars;
		
		List<Cars> listNow=new ArrayList<HomePage.Cars>();
		for(int i=list.size()-1;i>=0;i--)
		{
			listNow.add(list.get(i));
		}
		list.clear();
		list.addAll(listNow);
		
		if(list.size()>0)
		{
			adapter = new MyAttentionAdapter(getApplicationContext(), list);
			
			mLvAttention.setAdapter(adapter);
			mRlAttentionNo.setVisibility(View.INVISIBLE);
		}else{
			mLvAttention.setVisibility(View.INVISIBLE);
			mRlAttentionNo.setVisibility(View.VISIBLE);
		}
		
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		mLvAttention.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Cars cars = list.get(position);
				if("已下架".equals(cars.carPrice+""))
				{
					UIUtils.showToastSafe("车辆已下架不能查看详情!");
				}else{
					Intent intent=new Intent(getApplicationContext(),VehicleDetailActivity.class);
					Bundle bundle=new Bundle();
					bundle.putSerializable("cars", cars);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});

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
