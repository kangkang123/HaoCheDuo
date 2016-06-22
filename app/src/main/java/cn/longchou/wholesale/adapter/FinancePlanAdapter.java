package cn.longchou.wholesale.adapter;

import java.util.List;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.FinancialPlan;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FinancePlanAdapter extends BaseAdapter {

	private Context context;
	private List<FinancialPlan> items;
	private int p;
	private CarsInfo carsInfo;

	public FinancePlanAdapter(Context context, List<FinancialPlan> items, int p, CarsInfo carsInfo) {
		this.context = context;
		this.items = items;
		this.p=p;
		this.carsInfo=carsInfo;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public FinancialPlan getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.item_confirm_list_dialog,
				null);

		ImageView choose = (ImageView) view.findViewById(R.id.iv_confirm_list);
		TextView plan = (TextView) view.findViewById(R.id.tv_plan);
		TextView planContent = (TextView) view
				.findViewById(R.id.tv_plan_content);

		FinancialPlan finalce = getItem(position);
		plan.setText(finalce.plan);
		if(p!=-1)
		{
			CarsInfo info = CarsManager.getInstance().getCartCarMap().get(carsInfo.carID);
			if(info.financePlan.equals(finalce.plan))
			{
				choose.setImageResource(R.drawable.finance_plan_choose);
			}else{
				choose.setImageResource(R.drawable.finance_plan_default);
			}
		}else{
			
			FinancialPlan financialPlan = items.get(position);
			if(financialPlan.isChoose)
			{
				choose.setImageResource(R.drawable.finance_plan_choose);
			}else{
				choose.setImageResource(R.drawable.finance_plan_default);
			}
		}

		return view;
	}

}
