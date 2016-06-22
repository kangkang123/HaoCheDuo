package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MyNews;
import cn.longchou.wholesale.domain.MyNews.News;
import cn.longchou.wholesale.domain.PromotionActivity.Activitys;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
* @Description: 优惠活动adapter
*
* @author kangkang
*
* @date 2016年2月1日 下午4:24:25 
*
 */
public class PreferentialActivityAdapter extends BaseAdapter{

	private Context context;
	private List<Activitys> list;
	
	public PreferentialActivityAdapter(Context context,List<Activitys> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Activitys getItem(int position) {
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
		Activitys item = getItem(position);
		
		holder.round.setVisibility(View.GONE);
		holder.title.setText(item.activityTitle);
		holder.time.setText(item.activityDate);
		holder.content.setText(replaceString(item.activityContent));
		return convertView;
	}
	
	public static String replaceString(String content)
	{
		String replace = content.replace("<p>", "").replace("</p>", "").replace("<br />", "\n");
		System.out.println("replace:"+replace);
		return replace;
	}
	
	class ViewHolder{
		ImageView round;
		TextView title;
		ImageView mark;
		TextView time;
		TextView content;
	}

}
