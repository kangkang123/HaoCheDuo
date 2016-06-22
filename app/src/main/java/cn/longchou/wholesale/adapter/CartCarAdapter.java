package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.bumptech.glide.Glide;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.dialog.ConfirmListDialog;
import cn.longchou.wholesale.dialog.ConfirmListDialog.OnListDialogLietener;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.FinancialPlan;
import cn.longchou.wholesale.domain.ItemSelect;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;

/**
 * 
 * @Description: 车市首页listview的填充内容
 * 
 * @author kangkang
 * 
 * @date 2015年12月22日 下午11:18:10
 * 
 */
public class CartCarAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private List<Integer> ids;
	private ImageView mCarImage;
	private TextView mCarInfo;
	private TextView mCarTime;
	private TextView mCarMoney;
	private ImageView mIvLevel;
	private ImageView mCartCar;
	private CallbackCartCar mCallback;

	
	//获取金融方案
	List<FinancialPlan> financialPlan = FinancialPlan.getFinancialPlan();

	private int id;
	private CheckBox mCheck;
	
	//判断是否已经选择了
	private String ChooseContent=financialPlan.get(0).plan;
	
	//选中的融资
	private int ChooseId=0;
	

	//用于填充的list集合
	private List<CarsInfo> cartCarList;
	
	//用于表示来源于那个Fragment
	private String fragment;
	private CarsInfo info;

	/**
	 * 自定义接口，用于回调按钮点击事件到Activity
	 */
	public interface CallbackCartCar {
		public void click(View v);
	}


	public CartCarAdapter(Context context,List<CarsInfo> cartCarList, CheckBox mCheck, String cartCar,CallbackCartCar callback) {
		this.context=context;
		this.cartCarList=cartCarList;
		this.mCheck=mCheck;
		this.fragment=cartCar;
		this.mCallback=callback;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cartCarList.size();
	}

	@Override
	public CarsInfo getItem(int position) {
		// TODO Auto-generated method stub
		return cartCarList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View
					.inflate(context, R.layout.item_list_market, null);
			holder.mCarImage = (ImageView) convertView
					.findViewById(R.id.iv_car_image);
			holder.mChekced = (ImageView) convertView
					.findViewById(R.id.iv_check);
			holder.mCarInfo = (TextView) convertView
					.findViewById(R.id.tv_car_info);
			holder.mCarTime = (TextView) convertView
					.findViewById(R.id.tv_car_time);
			holder.mCarMoney = (TextView) convertView
					.findViewById(R.id.tv_car_money);
			holder.mIvLevel = (ImageView) convertView
					.findViewById(R.id.iv_level);
			holder.mCartCar = (ImageView) convertView
					.findViewById(R.id.iv_cartcar);
			holder.mEarnest=(TextView) convertView.findViewById(R.id.tv_car_earnest);
			holder.Line = convertView.findViewById(R.id.view_line);
			holder.llPlan=(LinearLayout) convertView.findViewById(R.id.ll_financing_plan);
			holder.mplan=(TextView) convertView.findViewById(R.id.tv_financing_plan);
			holder.llEarnest=(LinearLayout) convertView.findViewById(R.id.ll_car_earnest);
			holder.llMarket=(LinearLayout) convertView.findViewById(R.id.ll_car_market);
			holder.mCarMoneyMark=(TextView) convertView.findViewById(R.id.tv_car_money_tv);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (fragment.equals("carcar")) {
			holder.mChekced.setVisibility(View.VISIBLE);
			holder.llPlan.setVisibility(View.VISIBLE);

			//如果是购物车页面则显示定金,不显示购物车
			holder.llMarket.setVisibility(View.GONE);
			holder.llEarnest.setVisibility(View.VISIBLE);
		}
		if (fragment.equals("market")) {
			holder.mChekced.setVisibility(View.GONE);
			holder.llPlan.setVisibility(View.GONE);
			
			//如果是车市页面显示购物车、不显示订单
			holder.llMarket.setVisibility(View.VISIBLE);
			holder.llEarnest.setVisibility(View.GONE);
		}
		info = getItem(position);
		holder.mCarInfo.setText(info.carName);
		holder.mCarTime.setText(info.carDesc);
		holder.mplan.setText(info.financePlan);
		
		boolean isCertified = PreferUtils.getBoolean(context, "isCertified", false);
		if(isCertified)
		{
			//认证用户可产看价格
			holder.mCarMoney.setText(info.carPrice);
			
			if("已下架".equals(info.carPrice))
			{
				holder.mCarMoney.setTextColor(Color.GRAY);
				holder.mCarMoney.setTextSize(12);
				holder.mCarMoneyMark.setVisibility(View.INVISIBLE);
			}else{
				holder.mCarMoneyMark.setVisibility(View.VISIBLE);
			}
			//用户认证后显示定金
			int price=(int) info.carSubscription;
			holder.mEarnest.setText(price+"元");
			holder.llEarnest.setVisibility(View.VISIBLE);
		}else{
			
			//用户没有认证提示认证后可查看价格
			holder.mCarMoney.setText("认证用户可查看价格");
			holder.mCarMoneyMark.setVisibility(View.INVISIBLE);
			holder.mCarMoney.setTextSize(8);
			//用户没有认证定金不显示
			holder.llEarnest.setVisibility(View.INVISIBLE);
		}
		
		Glide.with(context).load(info.carImgURL).placeholder(R.drawable.displaycar).into(holder.mCarImage);
		final TextView text=holder.mplan;
		holder.llPlan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int p=(int) v.getTag();
				CarsInfo carsInfo = CarsManager.getInstance().getCartCarList().get(p);
				showDialog(p,text,carsInfo);
			}
		});
		
		//设置购物车中勾选的需要提交的车辆
		holder.mChekced.setImageResource(R.drawable.finance_plan_default);
		for(int i=0;i<CarsManager.getInstance().getCartCarMap().size();i++)
		{
			CarsInfo item = getItem(position);
			CarsInfo carsInfo = CarsManager.getInstance().getCartCarMap().get(item.carID);
			if(carsInfo.isSelect)
			{
				holder.mChekced.setImageResource(R.drawable.finance_plan_choose);
			}else{
				holder.mChekced.setImageResource(R.drawable.finance_plan_default);
			}
		}

		holder.mChekced.setOnClickListener(this);
		holder.mCartCar.setOnClickListener(this);

		holder.mChekced.setTag(position);
		holder.mCartCar.setTag(position);
		holder.llPlan.setTag(position);

		return convertView;
	}

	
	
	//显示弹出的融资方案
	protected void showDialog(final int p, final TextView text, final CarsInfo carsInfo) {
		
		ConfirmListDialog.showListDialog(context, "请选择融资方案", financialPlan, p,carsInfo, new OnListDialogLietener() {
			
			private ImageView choose;
			private CarsInfo in;
//			private CarsInfo chooseInfo=null;

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id,FinancePlanAdapter myAdapter) {
		
				choose =(ImageView) view.findViewById(R.id.iv_confirm_list);
				ChooseContent=financialPlan.get(position).plan;
				
				CarsManager.getInstance().setCarPlanSelect(carsInfo, ChooseContent);
				
				myAdapter.notifyDataSetChanged();
				
				
			}
			
			@Override
			public void onConfirm() {
				if(!TextUtils.isEmpty(ChooseContent))
				{
					text.setText(ChooseContent);
					CarsManager.getInstance().setCarPlanSelect(carsInfo, ChooseContent);
				}
				
			}
			
			@Override
			public void onCancel() {
				text.setText(info.financePlan);
				CarsManager.getInstance().setCarPlanSelect(carsInfo, carsInfo.financePlan);
			}
		});
		
	}

	//设置全选
	public void selectAll() {
		for(int i=0;i<CarsManager.getInstance().getCartCarMap().size();i++)
		{
			List<CarsInfo> carList = CarsManager.getInstance().getCartCarList();
			CarsInfo carsInfo = CarsManager.getInstance().getCartCarMap().get(carList.get(i).carID);
			if(!carsInfo.isSelect)
			{
				CarsManager.getInstance().setCartCarSelectTrue(carsInfo);
			}
		}
		notifyDataSetChanged();
	}
	
	//取消全部选中
	public void cancelSeclectAll()
	{
		for(int i=0;i<CarsManager.getInstance().getCartCarMap().size();i++)
		{
			List<CarsInfo> carList = CarsManager.getInstance().getCartCarList();
			CarsInfo carsInfo = CarsManager.getInstance().getCartCarMap().get(carList.get(i).carID);
			if(carsInfo.isSelect)
			{
				CarsManager.getInstance().setCartCarSelectFalse(carsInfo);
				
			}
		}
		notifyDataSetChanged();
	}

	// 单选
	public void selectSingle(int id) {

		CarsInfo select=getItem(id);
		CarsInfo carsInfo = CarsManager.getInstance().getCartCarMap().get(select.carID);
		if(!carsInfo.isSelect)
		{
			CarsManager.getInstance().setCartCarSelectTrue(select);
			CarsManager.getInstance().setSelectIds(select.carID);
//			if(CarsManager.getInstance().getSelectList().size()==CarsManager.getInstance().getCartCarList().size())
//			{
//				mCheck.setChecked(true);
//			}
			if(checkCarSelectAll())
			{
				mCheck.setChecked(true);
			}
			
			
		}else{
			CarsManager.getInstance().setCartCarSelectFalse(select);
			mCheck.setChecked(false);
			CarsManager.getInstance().removeSelectIds(select.carID);
		}
		notifyDataSetChanged();
		
	}
	
	//检查是否全选,只有当全部选中才返回true
	public boolean checkCarSelectAll()
	{
		List<CarsInfo> list = CarsManager.getInstance().getCartCarList();
		for(int i=0;i<list.size();i++)
		{
			CarsInfo carsInfo = list.get(i);
			if(!carsInfo.isSelect)
			{
				return false;
			}
		}
		return true;
	}

	public class ViewHolder {
		ImageView mChekced;
		ImageView mCarImage;
		TextView mCarInfo;
		TextView mCarTime;
		TextView mCarMoney;
		TextView mCarMoneyMark;
		ImageView mIvLevel;
		ImageView mCartCar;
		View Line;
		LinearLayout llPlan;
		TextView mplan;
		TextView mEarnest;
		LinearLayout llEarnest;
		LinearLayout llMarket;
	}

	// 响应按钮点击事件,调用子定义接口，并传入View
	@Override
	public void onClick(View v) {
		mCallback.click(v);

	}

}
