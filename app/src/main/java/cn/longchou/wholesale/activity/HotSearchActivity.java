package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.HotBrandAdapter;
import cn.longchou.wholesale.adapter.HotSearchAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.HotSearch;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class HotSearchActivity extends BaseActivity {

	private ImageView mBack;
	private EditText mEtSearch;
	private TextView mCancel;
	private GridView mGridView;
	private ImageView mImage;
	
	private List<String> list;
	private HotBrandAdapter adapter;

	@Override
	public void initView() {
		setContentView(R.layout.activity_hot_search);

		mBack = (ImageView) findViewById(R.id.title_search_back);
		mEtSearch = (EditText) findViewById(R.id.et_search_hot);
		mCancel = (TextView) findViewById(R.id.tv_search_cancel);

		mGridView = (GridView) findViewById(R.id.gv_search);

		mImage = (ImageView) findViewById(R.id.image_search_ad);

	}

	@Override
	public void initData() {
		
		setViewPagerPercent();
		getServerData();
		if(!TextUtils.isEmpty(Constant.searchSelect))
		{
			mEtSearch.setText(Constant.searchSelect);
			mEtSearch.setSelection(Constant.searchSelect.length());
		}
	}

	private void getServerData() {
		String token = PreferUtils.getString(getApplicationContext(), "token", null);
		HttpUtils http=new HttpUtils();
		String url=Constant.HotSearch;
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token", token);
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				Gson gson=new Gson();
				HotSearch json = gson.fromJson(result, HotSearch.class);
				list = json.hotBrand;
				Glide.with(getApplicationContext()).load(json.adPic).into(mImage);
				if(list.size()>0 && list!=null)
				{
					adapter = new HotBrandAdapter(getApplicationContext(),list);
					mGridView.setAdapter(adapter);
					
				}
				
			}
		});
		
	}

	// 设置viewpager的轮播图的百分比
	private void setViewPagerPercent() {
		// 窗口高度
		int px2dp = (int) UIUtils.px2dip(100);
		int screenHeight = (getScreenWidth() - px2dp) * 2 / 3;
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mImage
				.getLayoutParams();
		params.width = getScreenWidth() - px2dp;
		params.height = screenHeight;
		mImage.setLayoutParams(params);
	}

	// 获取屏幕的高度
	private int getScreenWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		// 取得窗口属性
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		// 窗口的宽度
		int screenWidth = dm.widthPixels;
		return screenWidth;
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mCancel.setOnClickListener(this);

		// 给GridView设置点击事件
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String string = list.get(position);
				mEtSearch.setText(string);
//				Constant.filterAction=string;
				Constant.searchSelect=string;
				setResult(3);
				finish();
//				adapter.notifyDataSetChanged();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			Constant.searchSelect="";
			setResult(3);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.title_search_back:
			Constant.searchSelect="";
			setResult(3);
			finish();
			break;
		case R.id.tv_search_cancel:
			String select = mEtSearch.getText().toString().trim();
			if(!TextUtils.isEmpty(select))
			{
				Constant.searchSelect=select;
				setResult(3);
				finish();
			}else{
				UIUtils.showToastSafe("搜索内容不能为空");
			}
			break;

		default:
			break;
		}
	}
}
