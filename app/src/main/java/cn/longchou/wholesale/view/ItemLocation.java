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
* @Description: 自定义定位的ListView的条目
*
* @author kangkang 
*
* @date 2015年12月28日 下午2:23:44 
*
 */
public class ItemLocation extends RelativeLayout {

	private TextView city;
	private ImageView ivIcon;
	public ItemLocation(Context context) {
		super(context);
		initView();
	}

	public ItemLocation(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ItemLocation(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ItemLocation(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}
	
	//设置确定的图标
	public void setLocationConfirm()
	{
		ivIcon.setImageResource(R.drawable.location_confirm);
	}
	//设置定位的箭头
	public void setLocationArrow()
	{
		ivIcon.setImageResource(R.drawable.location_city);
	}
	//设置当前定位的城市
	public void setLocationCity(String cityNow)
	{
		city.setText(cityNow);
	}
	//设置当前没有任何图标
	public void setLocationIconNo()
	{
		ivIcon.setVisibility(View.INVISIBLE);
	}
	public void initView()
	{
		View view=View.inflate(getContext(), R.layout.item_location_view, null);
		city = (TextView) view.findViewById(R.id.tv_city);
		ivIcon = (ImageView) view.findViewById(R.id.iv_location_arrow);
		this.addView(view);
	}

}
