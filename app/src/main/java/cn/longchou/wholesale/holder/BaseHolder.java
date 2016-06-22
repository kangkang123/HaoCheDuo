package cn.longchou.wholesale.holder;

import android.view.View;

public abstract class BaseHolder<T> {

	private View view;
	private T mData;

	public BaseHolder() {
		view = initView();
		view.setTag(this);
	}

	// 获取到view
	public View getRootView() {
		return view;
	}

	public abstract View initView();
	
	public void setData(T data)
	{
		this.mData=data;
		refreshView();
	}
	
	public abstract  void refreshView();

	public T getData()
	{
		return mData;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
