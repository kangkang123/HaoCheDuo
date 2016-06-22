package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.activity.TestReportActivity;
import cn.longchou.wholesale.dialog.ImageShowDialog;
import cn.longchou.wholesale.domain.TestReport.Items;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
* @Description: 检查报告
*
* @author kangkang
*
* @date 2016年2月3日 上午9:35:50 
*
 */
public class TestReportAdapter extends BaseAdapter {

	private Context context;
	private List<Items> list;
	private TextView mNumber;
	private TextView mContent;
	private TextView mTvResult;
	private ImageView mIvReslut;
	public TestReportAdapter(Context context, List<Items> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Items getItem(int position) {
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
			convertView=View.inflate(context, R.layout.item_list_test_report, null);
			holder.mNumber = (TextView) convertView.findViewById(R.id.car_detail_report_number);
			holder.mContent = (TextView) convertView.findViewById(R.id.tv_car_detail_report_content);
			holder.mTvResult = (TextView) convertView.findViewById(R.id.tv_car_detail_report_result);
			holder.mIvReslut = (ImageView) convertView.findViewById(R.id.iv_car_detail_report_result);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		Items item = getItem(position);
		int num=position+1;
		holder.mNumber.setText(num+"");
		holder.mContent.setText(item.key);
		if("正常".equals(item.value))
		{
			holder.mIvReslut.setVisibility(View.VISIBLE);
			holder.mTvResult.setVisibility(View.GONE);
		}else{
			holder.mIvReslut.setVisibility(View.GONE);
			holder.mTvResult.setVisibility(View.VISIBLE);
		}
		holder.mTvResult.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog();
				
			}
		});
		return convertView;
	}
	//点击时显示图片的内容
	protected void showDialog() {
		ImageShowDialog.showDialog(context);
		
	}

	class ViewHolder{
		private TextView mNumber;
		private TextView mContent;
		private TextView mTvResult;
		private ImageView mIvReslut;
	}

}
