package cn.longchou.wholesale.view;

import cn.longchou.wholesale.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 
* @Description: 个人信息的自定义条目
*
* @author kangkang 
*
* @date 2016年1月4日 下午6:13:41 
*
 */
public class ItemMyInformation extends RelativeLayout{

	private TextView myBuy;
	private TextView myChoose;
	private ImageView mLine;
	private ImageView mArrow;
	private TextView mArrowText;
	
	public ItemMyInformation(Context context) {
		super(context);
		initView();
	}

	public ItemMyInformation(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	public ItemMyInformation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ItemMyInformation(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	
	//设置个人信息的相关内容
	public void setInformation(String information)
	{
		myBuy.setText(information);
	}
	
	//设置个人信息后面的内容
	public void setChoose(String choose)
	{
		myChoose.setText(choose);
	}
	
	//设置箭头是否显示
	public void setArrowVisible(boolean isArrow)
	{
		mArrow.setVisibility(isArrow ? View.VISIBLE:View.INVISIBLE);
	}
	
	//设置个人信息后面的文字是否显示
	public void setChooseVisible(boolean isChoose)
	{
		myChoose.setVisibility(isChoose ? View.VISIBLE:View.INVISIBLE);
	}
	
	//设置右边箭头前面的文字是否显示
	public void setArrowTextVisible(boolean isArrowText)
	{
		mArrowText.setVisibility(isArrowText ? View.VISIBLE:View.INVISIBLE);
	}
	
	//设置右边箭头的文字的颜色
	public void setArrowTextColor(int color)
	{
		mArrowText.setTextColor(color);
	}
	
	//设置右边箭头前面的文字的内容
	public void setArrowText(String arrowText)
	{
		mArrowText.setText(arrowText);
	}

	public void initView()
	{
		View view=View.inflate(getContext(), R.layout.view_my_information, null);
		myBuy = (TextView) view.findViewById(R.id.tv_view_my_information_buy);
		myChoose = (TextView) view.findViewById(R.id.tv_view_my_information_loaction);
		mArrow = (ImageView) view.findViewById(R.id.iv_view_my_information_arrow);
		mLine = (ImageView) view.findViewById(R.id.iv_view_my_information_line);
		mArrowText = (TextView) view.findViewById(R.id.tv_view_setting_before);
		this.addView(view);
	}

}
