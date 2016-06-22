package cn.kang.dialog.library;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ConfirmListDialog extends BaseDialog {

	private String title;
	private List<FinancialPlan> items;
	private TextView tv_listdialog_title;
	private ListView lv_listdialog;
	
	//列表
	private OnListDialogLietener onListDialogLietener;
	
	private Button bt_dialog_cancel;
	private Button bt_dialog_confirm;
	
	private Context context;
	protected ConfirmListDialog(Context context, String title, List<FinancialPlan> items, OnListDialogLietener onListDialogLietener) {
		super(context);
		this.context = context;
		this.title = title;
		this.items = items;
		this.onListDialogLietener = onListDialogLietener;
	}

	public static  void showListDialog(Context context, String title, List<FinancialPlan> items, OnListDialogLietener onListDialogLietener){
		ConfirmListDialog dialog = new ConfirmListDialog(context, title, items, onListDialogLietener);
		dialog.show();
	}
	
	
	@Override
	public void initView() {
		setContentView(R.layout.confirm_list);
		tv_listdialog_title = (TextView) findViewById(R.id.tv_confirm_list_title);
		lv_listdialog = (ListView) findViewById(R.id.lv_confirm_list);
		
		bt_dialog_cancel = (Button) findViewById(R.id.bt_confirm_list_cancel);
		bt_dialog_confirm = (Button) findViewById(R.id.bt_confrim_list_confirm);
	}

	@Override
	public void initListener() {
		
		//确定取消按钮
		bt_dialog_cancel.setOnClickListener(this);
		bt_dialog_confirm.setOnClickListener(this);
		
		//给lv_listdialog设置条目侦听
		lv_listdialog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(onListDialogLietener != null){
					onListDialogLietener.onItemClick(parent, view, position, id);
				}
				dismiss();
			}
		});

	}

	@Override
	public void initData() {
		tv_listdialog_title.setText(title);
		lv_listdialog.setAdapter(new MyAdapter());

	}

	@Override
	public void processClick(View v) {
		int id = v.getId();
		if (id == R.id.bt_dialog_cancel) {
			if(onListDialogLietener != null){
				onListDialogLietener.onCancel();
			}
		} else if (id == R.id.bt_dialog_confirm) {
			if(onListDialogLietener != null){
				onListDialogLietener.onConfirm();
			}
		}
		//对话框消失
		dismiss();

	}
	

	//列表接口
	public interface OnListDialogLietener{
		void onItemClick(AdapterView<?> parent, View view, int position, long id);
		
		//确定取消
		void onCancel();
		void onConfirm();
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(context, R.layout.item_confirm_list_dialog, null);
			
			ImageView choose=(ImageView) view.findViewById(R.id.iv_confirm_list);
			TextView plan=(TextView) view.findViewById(R.id.tv_plan);
			TextView planContent=(TextView) view.findViewById(R.id.tv_plan_content);
			
			FinancialPlan finalce= (FinancialPlan) items.get(position);
			plan.setText(finalce.plan);
			planContent.setText(finalce.content);
			if(finalce.isChoose)
			{
				choose.setImageResource(R.drawable.location_confirm);
			}else{
				choose.setImageResource(R.drawable.checkedno);
			}
			
			return view;
		}
		
	}
}
