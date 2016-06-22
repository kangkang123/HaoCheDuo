package cn.longchou.wholesale.view;

import cn.longchou.wholesale.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
* @Description: 检测报告的条目的自定义
*
* @author kangkang
*
* @date 2016年1月20日 上午9:31:32 
*
 */
public class ItemCheckReport extends LinearLayout {

	private TextView number;
	private TextView content;
	private TextView tvResult;
	private ImageView ivResult;
	private ImageView mLine;
	private LinearLayout llReport;
	private static int choose;


	public ItemCheckReport(Context context) {
		super(context);
		initView();
	}

	public ItemCheckReport(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ItemCheckReport(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ItemCheckReport(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}
	
	//设置编号
	public void setNumber(String code) {
		number.setText(code);
	}
	
	private static OnCheckReportListener onCheckReportListener;
	
	public void setOnCheckReportListener(OnCheckReportListener onCheckReportListener){
		ItemCheckReport.onCheckReportListener=onCheckReportListener;
	}
	
	public interface OnCheckReportListener
	{
		void Check();
	}
	
	//设置检查的内容
	public void setContent(String checkCotent) {
		content.setText(checkCotent);
	}
	
	//设置图片结果是否可见
	public void setImageResultVisible(boolean isVisible) {
		ivResult.setVisibility(isVisible ? View.VISIBLE : View.GONE);

	}
	
	//设置文字内容是否可见
	public void setTextResultVisible(boolean isVisible)
	{
		tvResult.setVisibility(isVisible ? View.VISIBLE : View.GONE);
	}
	
	//返回结果的TextView对象
	public TextView getTextResult()
	{
		return tvResult;
	}
	
	//获取到Linelayout对象
	public LinearLayout getLineLayout()
	{
		return llReport;
	}
	
	//设置单元框的底部的线是否可见
	public void setLineVisible(boolean isVisible)
	{
		mLine.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
	}
	
	
	//初始化布局文件的信息
	private void initView() {
		View view=View.inflate(getContext(), R.layout.item_check_report, null);

		number = (TextView) view.findViewById(R.id.item_check_report_number);
		content = (TextView) view.findViewById(R.id.item_check_report_content);
		tvResult = (TextView) view.findViewById(R.id.item_check_report_result);
	    ivResult = (ImageView) view.findViewById(R.id.item_check_report_nomal);
	    mLine = (ImageView) view.findViewById(R.id.item_check_report_botton_line);
	    llReport = (LinearLayout) view.findViewById(R.id.ll_checl_report);
	    
	    tvResult.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(onCheckReportListener!=null)
				{
					onCheckReportListener.Check();
				}
				
			}
		});
	    
	    this.addView(view);
	}

}
