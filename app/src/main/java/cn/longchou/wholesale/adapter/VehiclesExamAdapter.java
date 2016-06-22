package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MustNotice.IllegalDisposal;
import cn.longchou.wholesale.domain.MustNotice.ResidencePermit;
import cn.longchou.wholesale.domain.MustNotice.VehiclesExam;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 车辆年审的adapter
*
* @author kangkang
 * @param <T>
*
* @date 2016年1月21日 上午11:06:57 
*
 */
public class VehiclesExamAdapter extends BaseAdapter{

	private Context context;
	private List<VehiclesExam> list;
	
	public VehiclesExamAdapter(Context context,List<VehiclesExam> list) {
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public VehiclesExam getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_list_notice_fee, null);
			holder.mText1=(TextView) convertView.findViewById(R.id.tv_item_list_notice_fee_1);
			holder.mText2=(TextView) convertView.findViewById(R.id.tv_item_list_notice_fee_2);
			holder.mText3=(TextView) convertView.findViewById(R.id.tv_item_list_notice_fee_3);
			holder.mText4=(TextView) convertView.findViewById(R.id.tv_item_list_notice_fee_4);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		VehiclesExam item = list.get(position);
		holder.mText1.setText(item.qy);
		holder.mText2.setText(item.bd);
		holder.mText3.setText(item.sh);
		holder.mText4.setText(item.lb);
		
		return convertView;
	}
	
	class ViewHolder{
		TextView mText1;
		TextView mText2;
		TextView mText3;
		TextView mText4;
	}

}
