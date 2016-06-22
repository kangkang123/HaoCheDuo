package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.CarBasicParameters;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BaseParamsAdapter extends BaseAdapter {

	private Context context;
	private List<CarBasicParameters> list;
	
	public BaseParamsAdapter(Context context,List<CarBasicParameters> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CarBasicParameters getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_params_details, null);
			holder.mName = (TextView) convertView.findViewById(R.id.tv_item_params_detail_name);
			holder.mContent = (TextView) convertView.findViewById(R.id.tv_item_params_detail_content);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		CarBasicParameters item = list.get(position);
		holder.mName.setText(item.key);
		holder.mContent.setText(item.value);
		return convertView;
	}
	
	class ViewHolder{
		private TextView mName;
		private TextView mContent;
	}

}
