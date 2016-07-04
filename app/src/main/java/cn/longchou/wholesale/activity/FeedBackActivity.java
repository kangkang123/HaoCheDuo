package cn.longchou.wholesale.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.meiqia.meiqiasdk.widget.MQRecorderKeyboardLayout;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.PopWindowAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.LoginValidate;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

/**
 * 
 * @Description: 意见反馈的界面
 * 
 * @author kangkang
 * 
 * @date 2016年6月6日 下午12:36:53
 * 
 */
public class FeedBackActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private RelativeLayout mRelative;
	private EditText mAdvice;
	private EditText mPhone;
	private TextView mSubmit;
	List<String> mList=new ArrayList<>();
	public boolean isOpen=false;
	PopupWindow popupWindow;
	private ImageView mArrow;
	private PopWindowAdapter adapter;
	private List<String> list=new ArrayList<>();
	private ListView mLvPop;

	@Override
	public void initView() {
		setContentView(R.layout.activity_feed_back);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mRelative = (RelativeLayout) findViewById(R.id.rl_feed_back);
		mAdvice = (EditText) findViewById(R.id.et_feed_back_advice);
		mPhone = (EditText) findViewById(R.id.et_feed_bakc_phone);
		mSubmit = (TextView) findViewById(R.id.tv_feed_back_submit);
		mArrow = (ImageView) findViewById(R.id.iv_arrow_pop);


	}

	@Override
	public void initData() {
		mTitle.setText("意见反馈");
		String phone = PreferUtils.getString(getApplicationContext(), "phone",null);
		if(!TextUtils.isEmpty(phone))
		{
			mPhone.setText(phone);
		}


		list.add("系统卡顿1");
		list.add("系统卡顿2");
		list.add("系统卡顿3");
		list.add("系统卡顿4");
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mSubmit.setOnClickListener(this);
		mRelative.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.tv_feed_back_submit:

			if(TextUtils.isEmpty(mAdvice.getText().toString().trim())){
				UIUtils.showToastSafe("请输入反馈内容");
			}else{
				feedBack(mAdvice.getText().toString().trim());
			}


			break;
		case R.id.rl_feed_back:
			if(isOpen){
				isOpen=false;
				mArrow.setImageResource(R.drawable.arrow_down);
//				popupWindow.dismiss();
			}else{
				isOpen=true;
				mArrow.setImageResource(R.drawable.arrow_up_down);
				showPopWindow();
			}
			break;
		default:
			break;
		}
	}

	private void showPopWindow(){
		View view=View.inflate(FeedBackActivity.this,R.layout.item_pop_feedback,null);
		popupWindow=new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.showAsDropDown(mRelative);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		mLvPop= (ListView) view.findViewById(R.id.lv_popwindow);

		adapter=new PopWindowAdapter(list,getApplicationContext(),mList);
		mLvPop.setAdapter(adapter);

		mLvPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String choose=list.get(position);
				if(mList.contains(choose)){
					mList.remove(choose);
				}else{
					mList.add(choose);
				}

				adapter=new PopWindowAdapter(list,getApplicationContext(),mList);
				mLvPop.setAdapter(adapter);

				if(mList.size()==0)
				{
					mAdvice.setText("");
				}else{
					StringBuffer sb=new StringBuffer();
					for(int i=0;i<mList.size();i++)
					{
						if(i<mList.size()-1)
						{
							sb.append(mList.get(i)+",");
						}else{
							sb.append(mList.get(i));
						}
					}
					mAdvice.setText(sb.toString());

				}
			}
		});
	}

	public void feedBack(String result){
		String phone = PreferUtils.getString(getApplicationContext(), "phone",null);
		HttpUtils http=new HttpUtils();
		String url= Constant.RequestFeedBack;
		RequestParams params=new RequestParams();
		params.addBodyParameter("content",result);
		params.addBodyParameter("phone",phone);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				Gson gson=new Gson();
				LoginValidate loginValidate = gson.fromJson(result, LoginValidate.class);
				if(TextUtils.isEmpty(loginValidate.errorMsg)){
					UIUtils.showToastSafe("反馈成功");
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

}
