package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyNews.News;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyNewsAdapter extends BaseAdapter{

	private Context context;
	private List<News> list;
	
	public MyNewsAdapter(Context context,List<News> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public News getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_my_news, null);
			holder.round=(ImageView) convertView.findViewById(R.id.iv_my_news_round);
			holder.title=(TextView) convertView.findViewById(R.id.tv_my_news_detail_title);
			holder.time=(TextView) convertView.findViewById(R.id.tv_my_news_time);
			holder.content=(TextView) convertView.findViewById(R.id.iv_my_news_content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		News item = getItem(position);
		if(item.isRead)
		{
			holder.round.setVisibility(View.GONE);
		}
		holder.title.setText(item.msgTitle);
		holder.time.setText(item.msgDate);
		holder.content.setText(item.msgContent);
		return convertView;
	}
	
	class ViewHolder{
		ImageView round;
		TextView title;
		ImageView mark;
		TextView time;
		TextView content;
	}

}
