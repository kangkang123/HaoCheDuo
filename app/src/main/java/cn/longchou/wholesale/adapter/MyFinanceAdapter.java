package cn.longchou.wholesale.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.FinanceFirst;
/**
 * 
* @Description: 我的金融
*
* @author kangkang
*
* @date 2016年2月25日 下午4:30:52 
*
 */
public class MyFinanceAdapter extends BaseAdapter {

	
	
	private Context context;
	private List<FinanceFirst> list;
	public MyFinanceAdapter(Context context, List<FinanceFirst> list){
		this.context=context;
		this.list=list;
	}

	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public FinanceFirst getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_finance_first, null);
			holder.mPlan = (TextView) convertView.findViewById(R.id.tv_item_finance_title);
			holder.mImage = (ImageView) convertView.findViewById(R.id.iv_item_finance_plan);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		
		FinanceFirst data = getItem(position);
		holder.mPlan.setText(data.title);
		holder.mImage.setBackgroundResource(data.id);
		return convertView;
	}
	
	class ViewHolder{
		private TextView mPlan;
		private ImageView mImage;
	}

}
