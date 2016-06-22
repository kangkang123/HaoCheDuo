package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MachineElectrical;
import cn.longchou.wholesale.domain.TestReport.Mechanical;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 机械及电器的adapter
*
* @author kangkang
*
* @date 2016年1月20日 下午3:50:38 
*
 */
public class MachineAdapter extends BaseAdapter {

	private Context context;
	private List<Mechanical> list;
	public MachineAdapter(Context context, List<Mechanical> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Mechanical getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_machine_electrical, null);
			holder.mPosition=(TextView) convertView.findViewById(R.id.tv_machine_electrical_position);
			holder.mHurt=(TextView) convertView.findViewById(R.id.tv_machine_electrical_hurt);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Mechanical item = getItem(position);
		holder.mPosition.setText(item.key);
		holder.mHurt.setText(item.value);
		return convertView;
	}

	class ViewHolder{
		TextView mPosition;
		TextView mHurt;
	}

}
