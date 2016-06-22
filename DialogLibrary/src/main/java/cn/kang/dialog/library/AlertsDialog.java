package cn.kang.dialog.library;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.kang.dialog.library.R;

public class AlertsDialog extends BaseDialog {

	private String title;
	private String message;
	private TextView tv_dialog_title;
	private TextView tv_dialog_message;
	private Button bt_dialog_confirm;
	private OnConfirmListener onConfirmListener;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	protected AlertsDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public static void showDialog(Context context, String title, String message, OnConfirmListener confirmListener){
		AlertsDialog dialog = new AlertsDialog(context);
		dialog.setTitle(title);
		dialog.setMessage(message);
		dialog.setConfirmListener(confirmListener);
		dialog.show();
	}
	
	@Override
	public void initView() {
		//设置对话框显示的布局文件
		setContentView(R.layout.dialog_alert);
		
		tv_dialog_title = (TextView) findViewById(R.id.tv_dialog_title);
		tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
		
		bt_dialog_confirm = (Button) findViewById(R.id.bt_dialog_confirm);

	}

	@Override
	public void initListener() {
		bt_dialog_confirm.setOnClickListener(this);

	}

	@Override
	public void initData() {
		tv_dialog_title.setText(title);
		tv_dialog_message.setText(message);

	}

	@Override
	public void processClick(View v) {
		int id = v.getId();
		 if (id == R.id.bt_dialog_confirm) {
			if(onConfirmListener != null){
				onConfirmListener.onConfirm();
			}
		}
		//对话框消失
		dismiss();
		
	}
	
	
	public void setConfirmListener(OnConfirmListener confirmListener) {
		this.onConfirmListener = confirmListener;
	}



	public interface OnConfirmListener{
		void onConfirm();
	}
}
