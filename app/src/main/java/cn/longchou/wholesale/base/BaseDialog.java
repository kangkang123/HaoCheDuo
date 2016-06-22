package cn.longchou.wholesale.base;

import cn.longchou.wholesale.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
/**
 * 
* @Description: 基类的对话框
*
* @author kangkang 
*
* @date 2015年12月21日 下午2:32:09 
*
 */
public abstract class BaseDialog extends AlertDialog implements android.view.View.OnClickListener {

	public BaseDialog(Context context) {
		super(context,R.style.BaseDialog);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initListener();
		initData();
	}
	
	//初始化view
	public abstract void initView();
	
	//初始化数据
	public abstract void initData();
	
	//初始化点击事件监听
	public abstract void initListener();
	
	//点击事件的处理
	public abstract void processClick(View v);

	//点击事件
	@Override
	public void onClick(View v) {
		
		processClick(v);
		
	}

}
