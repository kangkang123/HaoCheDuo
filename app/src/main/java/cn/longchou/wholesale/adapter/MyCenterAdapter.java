package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 
* @Description: 我的界面的adapter
*
* @author kangkang 
*
* @date 2015年12月30日 下午8:30:23 
*
 */
public class MyCenterAdapter extends BaseAdapter {

	
	private Context context;
	private List<String> list;
	private int[] ids;
	private int id;
	
	public MyCenterAdapter(Context context,List<String> list,int[] ids,int id) {
		this.context=context;
		this.list=list;
		this.ids=ids;
		this.id=id;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int position) {
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
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_list_my, null);
			holder.image=(ImageView) convertView.findViewById(R.id.iv_item_my);
			holder.content=(TextView) convertView.findViewById(R.id.tv_item_my);
			holder.line=(ImageView) convertView.findViewById(R.id.iv_my_line);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
	    if((list.size()-1)==position)
	    {
	    	holder.line.setVisibility(View.INVISIBLE);
	    }else if(position==0)
	    {
	    	holder.line.setVisibility(View.VISIBLE);
	    }
	    //如果id是1表示只有一个图标
	    if(id==1)
	    {
	    	holder.image.setImageResource(ids[0]);
	    }else if(id==2)//如果id是2表示图标可以更换
	    {
	    	holder.image.setImageResource(ids[position]);
	    	
	    }
	    
	    holder.content.setTextSize(12);
		holder.content.setText(getItem(position));
		return convertView;
	}
	
	class ViewHolder
	{
		ImageView image;
		TextView content;
		ImageView line;
	}

}
