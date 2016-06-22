package cn.kang.dialog.library;

import android.content.Context;
import android.view.View;
/**
 * 
* @Description: 加载完成的对话框
*
* @author kangkang
*
* @date 2016年6月3日 下午9:21:17 
*
 */
public class FinishedDialog extends BaseDialog {

	private static FinishedDialog dialog;

	protected FinishedDialog(Context context) {
		super(context);
		
	}
	
	public static void showDialog(Context context){
		dialog = new FinishedDialog(context);
		dialog.show();
	}
	
	public static void dismissDialog()
	{
		dialog.dismiss();
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_finished);

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
