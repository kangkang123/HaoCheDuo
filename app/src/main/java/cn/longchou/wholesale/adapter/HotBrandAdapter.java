package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.HotActivity.HotAction;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 热门品牌adapter
*
* @author kangkang 
*
* @date 2015年12月30日 下午1:18:46 
*
 */
public class HotBrandAdapter extends BaseAdapter {

	private List<String> list;
	private Context context;
	
	//设置状态
	private boolean b;
	//保存选中的条目的下标
	private int index;
	
	public HotBrandAdapter(Context context,List<String> list) {
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
		
		return list.get(position);
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
			convertView=View.inflate(context, R.layout.item_hot_search, null);
			holder.mTvHot=(TextView) convertView.findViewById(R.id.tv_item_hot);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		for (int i = 0; i < list.size(); i++) {
			//当点击条目的时候，设置指定位置的条目的对号为不可见
			if(index==i&&b==true)
			{
				Drawable drawable = context.getResources().getDrawable(R.drawable.shape_hot_item);
				holder.mTvHot.setBackgroundDrawable(drawable);
				holder.mTvHot.setTextColor(Color.rgb(128, 128, 128));
			}
		}
		holder.mTvHot.setText(getItem(position));
		return convertView;
	}
	
	//设置当点击天目的时候调用
	public void selectSingle(int index, boolean b){
		this.index=index;
		this.b=b;
		notifyDataSetChanged();
	}
	
	
	class ViewHolder{
		TextView mTvHot;
	}

}
