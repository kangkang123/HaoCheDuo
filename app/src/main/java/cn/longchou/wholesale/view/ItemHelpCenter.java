package cn.longchou.wholesale.view;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.utils.UIUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ItemHelpCenter extends RelativeLayout {

	ImageView image;
	TextView content;
	ImageView line;
	private ImageView arrow;
	
	public ItemHelpCenter(Context context) {
		super(context);
		initView();
	}

	public ItemHelpCenter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ItemHelpCenter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public ItemHelpCenter(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}
	
	//更改图片
	public void changeImage(int id)
	{
		image.setImageResource(id);
	}
	
	//更改文字
	public void changeText(String text)
	{
		content.setText(text);
	}
	
	//设置图片是否显示
	public void setLineVisible(boolean isVisible)
	{
		line.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
	}
	
	//设置箭头是否可见
	public void setArrowVisible(boolean isVisible)
	{
		arrow.setVisibility(isVisible ? View.VISIBLE : View.INVISIBLE);
	}
	
	public void setTextSize(int size)
	{
		content.setText(size);
	}
	
	
	private void initView() {
		View view = View.inflate(getContext(), R.layout.item_list_my, null);

		image=(ImageView) view.findViewById(R.id.iv_item_my);
		content=(TextView) view.findViewById(R.id.tv_item_my);
		line=(ImageView) view.findViewById(R.id.iv_my_line);
		
		arrow = (ImageView) view.findViewById(R.id.iv_item_arrow);
		content.setTextSize(12);
		this.addView(view);
	}

}
