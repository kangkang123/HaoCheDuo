package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.usage.UsageEvents.Event;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyHotBrandAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
/**
 * 
* @Description: 性别选择
*
* @author kangkang
*
* @date 2016年6月6日 下午5:01:44 
*
 */
public class SexChooseActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ListView mListView;
	private List<String> list;
	private MyHotBrandAdapter adapter;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_sex_choose);

		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mListView = (ListView) findViewById(R.id.lv_sex_choose);
	}

	@Override
	public void initData() {
		mTitle.setText("修改性别");
		list = new ArrayList<String>();
		list.add("男");
		list.add("女");
		list.add("保密");
		adapter = new MyHotBrandAdapter(getApplicationContext(), list, "sexChoose");
		mListView.setAdapter(adapter);

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Constant.sexChoose = list.get(position);
				adapter.notifyDataSetChanged();
			}
		});

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			setResult(1);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			setResult(1);
			finish();
            break;

		default:
			break;
		}

	}

}
