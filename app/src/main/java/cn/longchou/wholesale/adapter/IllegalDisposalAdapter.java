package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.MustNotice.IllegalDisposal;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 违章处理的adapter
*
* @author kangkang
 * @param <T>
*
* @date 2016年1月21日 上午11:06:57 
*
 */
public class IllegalDisposalAdapter extends BaseAdapter{

	private Context context;
	private List<IllegalDisposal> list;
	private int object;
	
	public IllegalDisposalAdapter(Context context,List<IllegalDisposal> list) {
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		
		return list.size();
	}

	@Override
	public IllegalDisposal getItem(int position) {
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
		IllegalDisposal item = list.get(position);
		holder.mText1.setText(item.qy);
		holder.mText2.setText(item.wzbj);
		holder.mText3.setText(item.kfcl);
		holder.mText4.setText(item.fwf);
		
		return convertView;
	}
	
	class ViewHolder{
		TextView mText1;
		TextView mText2;
		TextView mText3;
		TextView mText4;
	}

}
