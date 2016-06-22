package cn.longchou.wholesale.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.FinanceInfo;
import cn.longchou.wholesale.domain.StockFinance;
import cn.longchou.wholesale.domain.FinanceInfo.MapInfo;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.ItemFinanceGroup;

public class StockFinanceAdapter extends BaseAdapter {

	private Context context;
	private List<String> financeNos;
	private List<List<FinanceInfo>> lists;
	private int pos=-1;
	private int index=0;
	
	public StockFinanceAdapter(Context context, List<String> financeNos,List<List<FinanceInfo>> lists) {
		this.context=context;
		this.financeNos=financeNos;
		this.lists=lists;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return financeNos.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return financeNos.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_finance_group, null);
			holder.mNumber = (TextView) convertView.findViewById(R.id.tv_finance_number);
			holder.mArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
			holder.mListView = (ListView) convertView.findViewById(R.id.lv_finance);
			holder.mRl=(RelativeLayout) convertView.findViewById(R.id.rl_finance);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		holder.mNumber.setText(financeNos.get(position));
		holder.mArrow.setImageResource(R.drawable.arrow_finance_close);
		MyAdapter myAdapter = new MyAdapter(lists.get(position));
		holder.mListView.setAdapter(myAdapter);
		
		List<Boolean> isOpenData = FinanceInfo.getIsOpenData();
		holder.mListView.setVisibility(View.GONE);
		holder.mArrow.setImageResource(R.drawable.arrow_finance_close);
		
		holder.mRl.setTag(position);
		
		if(holder.mRl.getTag()!=null)
		{
			int pos=(int) holder.mRl.getTag();
			if(isOpenData.get(pos))
			{
				holder.mListView.setVisibility(View.VISIBLE);
				holder.mArrow.setImageResource(R.drawable.arrow_finance_open);
			}
		}
		
		holder.mRl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				List<Boolean> isOpenData = FinanceInfo.getIsOpenData();
				
				int position=(int) v.getTag();
				
				
				for(int i=0;i<isOpenData.size();i++)
				{
					if(position==i)
					{
						if(isOpenData.get(i))
						{
							FinanceInfo.setIsOpen(false, i);
						}else{
							FinanceInfo.setIsOpen(true, i);
						}
					}else{
						FinanceInfo.setIsOpen(false, i);
					}
				}
				
				notifyDataSetChanged();
			}
		});
		
		
		return convertView;
	}

	class ViewHolder{
		private TextView mNumber;
		private ImageView mArrow;
		private ListView mListView;
		private RelativeLayout mRl;
	}
	public class MyAdapter extends BaseAdapter{
		private List<FinanceInfo> list;
		public MyAdapter(List<FinanceInfo> list) {
			this.list=list;
		}

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
			View view = View.inflate(context, R.layout.item_finace_child, null);
			TextView mNumber=(TextView) view.findViewById(R.id.tv_finance_child);
			TextView mContent=(TextView) view.findViewById(R.id.tv_finance_content);
			FinanceInfo info = list.get(position);
			mNumber.setText(info.number);
			mContent.setText(info.context);
			return view;
		}
		
	}


}
