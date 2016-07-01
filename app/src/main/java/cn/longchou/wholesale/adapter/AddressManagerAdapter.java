package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.AddressList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 
* @Description: 地址管理的adapter
*
* @author kangkang
*
* @date 2016年6月4日 下午4:39:44 
*
 */
public class AddressManagerAdapter extends BaseAdapter {

	private Context context;
	private List<AddressList.UserAddressBean> list;
	public AddressManagerAdapter(Context context,List<AddressList.UserAddressBean> list) {
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public AddressList.UserAddressBean getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_address_manage_list, null);
			holder.Name=(TextView) convertView.findViewById(R.id.tv_address_name);
			holder.Phone=(TextView) convertView.findViewById(R.id.tv_address_phone);
			holder.Address=(TextView) convertView.findViewById(R.id.tv_address_address);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		AddressList.UserAddressBean item = list.get(position);
		holder.Name.setText(item.userName);
		holder.Phone.setText(item.phone);
		String address=item.province+item.city+item.county+item.address;
		holder.Address.setText(address);
		return convertView;
	}
	
	class ViewHolder{
		TextView Name;
		TextView Phone;
		TextView Address;
		
	}

}
