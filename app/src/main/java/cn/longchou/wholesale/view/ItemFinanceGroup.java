package cn.longchou.wholesale.view;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.FinanceInfo;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 
* @Description: 金融的Group列表
*
* @author kangkang
*
* @date 2016年5月5日 下午5:10:26 
*
 */
public class ItemFinanceGroup extends RelativeLayout {

	private static TextView mNumber;
	private static ImageView mArrow;
	private boolean isOpen=false;
	private ListView mListView;
	private List<FinanceInfo> list;
	private MyAdapter adapter;

	public ItemFinanceGroup(Context context) {
		super(context);
		init();
	}

	public ItemFinanceGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ItemFinanceGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ItemFinanceGroup(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}
	
	public void setNumberText(String number)
	{
		mNumber.setText(number);
	}
	
	public void setArrow(boolean isOpen)
	{
		this.isOpen=isOpen;
		if(this.isOpen)
		{
			mArrow.setImageResource(R.drawable.arrow_finance_open);
			mListView.setVisibility(View.VISIBLE);
		}else{
			mArrow.setImageResource(R.drawable.arrow_finance_close);
			mListView.setVisibility(View.GONE);
		}
		adapter.notifyDataSetChanged();
	}
	
	
	public void setListViewData(List<FinanceInfo> list)
	{
		this.list=list;
		adapter = new MyAdapter();
		mListView.setAdapter(adapter);
	}
	
	public void init()
	{
		View view = View.inflate(getContext(), R.layout.item_finance_group, null);
		mNumber = (TextView) view.findViewById(R.id.tv_finance_number);
		mArrow = (ImageView) view.findViewById(R.id.iv_arrow);
		mListView = (ListView) view.findViewById(R.id.lv_finance);
		this.addView(view);
	}
	public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getContext(), R.layout.item_finace_child, null);
			TextView mNumber=(TextView) view.findViewById(R.id.tv_finance_child);
			TextView mContent=(TextView) view.findViewById(R.id.tv_finance_content);
			List<FinanceInfo> lists=list;
			FinanceInfo info = list.get(position);
			mNumber.setText(info.number);
			mContent.setText(info.context);
			return view;
		}
		
	}

}
