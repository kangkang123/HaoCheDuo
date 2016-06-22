package cn.longchou.wholesale.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
/**
 * 
* @Description: Fragment的基类
*
* @author kangkang 
*
* @date 2015年12月21日 下午2:39:32 
*
 */

public abstract   class BaseFragment extends Fragment implements OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return initView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initData();
		initListener();
	}
	
	 //初始化view
	public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
	//初始化数据
	public abstract void initData();
	
	//初始化点击事件监听
	public abstract void initListener();
	
	//点击事件的处理
	public abstract void processClick(View v);
	
	@Override
	public void onClick(View v) {
		processClick(v);
	}
}
