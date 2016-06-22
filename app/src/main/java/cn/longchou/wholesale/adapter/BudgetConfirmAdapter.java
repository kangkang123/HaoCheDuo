package cn.longchou.wholesale.adapter;

import java.util.List;

import com.bumptech.glide.Glide;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.dialog.ConfirmListDialog;
import cn.longchou.wholesale.dialog.ConfirmListDialog.OnListDialogLietener;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.FinancialPlan;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BudgetConfirmAdapter extends BaseAdapter {

	private Context context;
	//获取金融方案
	List<FinancialPlan> financialPlan = FinancialPlan.getFinancialPlan();
	
	private List<CarsInfo> list;
	
	//判断是否已经选择了
	private String ChooseContent=financialPlan.get(0).plan;
	private CarsInfo item;
	private int i;
	public BudgetConfirmAdapter(Context context, List<CarsInfo> list, int i) {
		this.context=context;
		this.list=list;
		this.i=i;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CarsInfo getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if(convertView==null)
		{
			holder=new ViewHolder();
			convertView=View.inflate(context, R.layout.item_list_budget_confirm, null);
			holder.mChoose=(ImageView) convertView.findViewById(R.id.iv_check);
			holder.mDescript = (TextView) convertView.findViewById(R.id.tv_budget_confirm_car_descripe);
			holder.mTime = (TextView) convertView.findViewById(R.id.tv_budget_confirm_car_time);
			holder.mMoney = (TextView) convertView.findViewById(R.id.tv_budget_confirm_car_money);
			holder.mCarMoneyMark=(TextView) convertView.findViewById(R.id.tv_budget_confirm_car_money_mark);
			holder.mEarnest = (TextView) convertView.findViewById(R.id.tv_budget_confirm_car_earnest);
			holder.mEarnestText=(TextView) convertView.findViewById(R.id.tv_budget_confirm_car_earnest_text);
			holder.mLLplan = (LinearLayout) convertView.findViewById(R.id.ll_budget_confirm_financing_plan);
			holder.mPlan = (TextView) convertView.findViewById(R.id.tv_budget_confirm_financing_plan);
			holder.mDisplay = (ImageView) convertView.findViewById(R.id.iv_budget_confirm_car_image);
		    convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.mChoose.setVisibility(View.GONE);
	    item = getItem(position);
		holder.mDescript.setText(item.carName);
		holder.mTime.setText(item.carDesc);
		holder.mPlan.setText(item.financePlan);
		
		boolean isCertified = PreferUtils.getBoolean(context, "isCertified", false);
		if(isCertified)
		{
			//认证用户可产看价格
			holder.mMoney.setText(item.carPrice+"");
			holder.mCarMoneyMark.setVisibility(View.VISIBLE);
			//用户认证后显示定金
			int price=(int) item.carSubscription;
			holder.mEarnest.setText(price+"元");
			holder.mEarnestText.setVisibility(View.VISIBLE);
			
			//只有认证才会选中
			holder.mChoose.setImageResource(R.drawable.finance_plan_choose);
		}else{
			
			//用户没有认证提示认证后可查看价格
			holder.mMoney.setText("认证用户可查看价格");
			holder.mCarMoneyMark.setVisibility(View.INVISIBLE);
			holder.mMoney.setTextSize(8);
			//用户没有认证定金不显示
			holder.mEarnestText.setVisibility(View.INVISIBLE);
			
			//没有认证就不选中
			holder.mChoose.setImageResource(R.drawable.finance_plan_default);
		}
		
		Glide.with(context).load(item.carImgURL).placeholder(R.drawable.displaycar).into(holder.mDisplay);
		final TextView text=holder.mPlan;
		holder.mLLplan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int p=(int) v.getTag();
				if(i!=-1)
				{
					//从购物车进来的
					CarsInfo carsInfo = CarsManager.getInstance().getCartCarList().get(p);
					showDialog(p,text,carsInfo);
				}else{
					//从订单详情进来的
					CarsInfo carsInfo = list.get(0);
					showDialog(-1,text,carsInfo);
				}
				
			}
		});
		
		holder.mLLplan.setTag(position);
		return convertView;
	}
	//显示弹出的融资方案
	protected void showDialog(final int p, final TextView text,  final CarsInfo carsInfo) {
		ConfirmListDialog.showListDialog(context, "请选择融资方案", financialPlan, p, carsInfo, new OnListDialogLietener() {
			
			private ImageView choose;
			private String plan2;
			private String mPlan;
			private boolean isChoose;
			List<FinancialPlan> plans=financialPlan;
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id,FinancePlanAdapter myAdapter) {
				choose =(ImageView) view.findViewById(R.id.iv_confirm_list);
				
				mPlan = financialPlan.get(position).plan;
				if(p!=-1)
				{
					CarsManager.getInstance().setCarPlanSelect(carsInfo, mPlan);
					
				}else{
					//订单详情
					for(int i=0;i<financialPlan.size();i++)
					{
						if(i==position)
						{
							FinancialPlan plan = financialPlan.get(position);
							plan.isChoose=true;
							plan2 = plan.plan;
						}else{
							FinancialPlan plan = financialPlan.get(i);
							plan.isChoose=false;
						}
					}
					
				}
				myAdapter.notifyDataSetChanged();
				
			}
			
			@Override
			public void onConfirm() {
				if(p!=-1)
				{
					if(!TextUtils.isEmpty(mPlan))
					{
						text.setText(mPlan);
						CarsManager.getInstance().setCarPlanSelect(carsInfo, mPlan);
					}
				}else{
					text.setText(plan2);
				}
				
			}
			
			@Override
			public void onCancel() {
				//如果是从列表页面进来
				if(p!=-1)
				{
					text.setText(carsInfo.financePlan);
					CarsManager.getInstance().setCarPlanSelect(carsInfo, carsInfo.financePlan);
				}else{
//					//如果是从订单详情页面进来
					for(int i=0;i<financialPlan.size();i++)
					{
						FinancialPlan plan = financialPlan.get(i);
						//当点击了对话框的选项时，mPlan不为空，当没有点击对话框的选项mPlan为空
						if(!TextUtils.isEmpty(mPlan))
						{
							//遍历所有的选项，当等于点击中的选项时让选中状态变为false
							if(mPlan.equals(plan.plan))
							{
								plan.isChoose=false;
							}else{
								//没有选中的变为true，并且重新设置值
								plan.isChoose=true;
								text.setText(plan.plan);
							}
						}
						
					}
				}
			}
		});
		
	}
	class ViewHolder{
		private TextView mDescript;
		private TextView mTime;
		private TextView mMoney;
		private TextView mCarMoneyMark;
		private TextView mEarnest;
		private TextView mEarnestText;
		private LinearLayout mLLplan;
		private TextView mPlan;
		private ImageView mDisplay;
		private ImageView mChoose;
	}

}
