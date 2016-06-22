package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.holder.BaseHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public abstract class MyBaseAdapter <T> extends BaseAdapter{

	public ListView mListView;
	public List<T> mDatas;
	private BaseHolder<T> holder;
	
	public MyBaseAdapter(ListView mListView,List<T> mDatas) {
		this.mListView=mListView;
		setData(mDatas);
	}
	
	public void setData(List<T> mDatas) {
		this.mDatas = mDatas;

	}

	public List<T> getData() {
		return mDatas;
	}

	@Override
	public int getCount() {
	
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null!=convertView)
		{
			holder= (BaseHolder<T>) convertView.getTag();
		}else{
			holder=getHolder();
		}
		holder.setData(mDatas.get(position));
		return holder.getRootView();
	}

	public abstract  BaseHolder getHolder();

}
