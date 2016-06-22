package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MyScore.ScoreDetail;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 积分的adapter
*
* @author kangkang
*
* @date 2016年1月14日 下午6:09:41 
*
 */
public class IntegrateAdapter extends BaseAdapter{

	private Context context;
	private List<ScoreDetail> list;
	
	public IntegrateAdapter(Context context, List<ScoreDetail> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ScoreDetail getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_balance, null);
			holder.source=(TextView) convertView.findViewById(R.id.tv_item_balance_source);
			holder.time=(TextView) convertView.findViewById(R.id.tv_item_balance_time);
			holder.score=(TextView) convertView.findViewById(R.id.tv_item_balance_score);
			holder.mark=(TextView) convertView.findViewById(R.id.tv_item_balance_mark);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		ScoreDetail item = getItem(position);
		holder.source.setText(item.description);
		holder.score.setText(item.pointAmount);
		holder.time.setText(item.createTimeStr);
		if("支出".equals(item.typeName))
		{
			holder.mark.setText("-");
		}else if("收入".equals(item.typeName))
		{
			holder.mark.setText("+");
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView source;
		TextView time;
		TextView score;
		TextView mark;
		
	}

}
