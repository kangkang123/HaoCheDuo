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
* @Description: 热门搜索的adapter
*
* @author kangkang 
*
* @date 2015年12月30日 下午1:18:46 
*
 */
public class HotSearchAdapter extends BaseAdapter {

	private List<HotAction> list;
	private Context context;
	
	public HotSearchAdapter(Context context,List<HotAction> list) {
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public HotAction getItem(int position) {
		
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
		HotAction item = getItem(position);
		if(item.actionName.equals(Constant.screenAvtivity))
		{
			Drawable drawable = context.getResources().getDrawable(R.drawable.shape_screen_item_choose);
			holder.mTvHot.setBackgroundDrawable(drawable);
			holder.mTvHot.setTextColor(Color.rgb(237, 108, 1));
		}else{
			Drawable drawable = context.getResources().getDrawable(R.drawable.shape_hot_item);
			holder.mTvHot.setBackgroundDrawable(drawable);
			holder.mTvHot.setTextColor(Color.rgb(128, 128, 128));
		}
		holder.mTvHot.setText(getItem(position).actionName);
		return convertView;
	}
	
	class ViewHolder{
		TextView mTvHot;
	}

}
