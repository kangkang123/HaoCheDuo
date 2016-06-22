package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.manage.CarsManager;
import cn.longchou.wholesale.utils.PreferUtils;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @Description: 车市首页listview的填充内容
 * 
 * @author kangkang
 * 
 * @date 2015年12月22日 下午11:18:10
 * 
 */
public class MarketCarAdapter extends BaseAdapter implements OnClickListener {

	private Context context;
	private List<Integer> ids;
	private String fragment;
	private ImageView mCarImage;
	private TextView mCarInfo;
	private TextView mCarTime;
	private TextView mCarMoney;
	private ImageView mIvLevel;
	private ImageView mCartCar;
	private CallMarket mCallback;
	
	List<Cars> carsList;

	/**
	 * 自定义接口，用于回调按钮点击事件到Activity
	 */
	public interface CallMarket {
		public void click(View v);
	}

	//订单的构造
	public MarketCarAdapter(Context context, List<Cars> carsList,String fragment, CallMarket callback) {
		this.context = context;
		this.fragment = fragment;
		this.mCallback = callback;
		this.carsList = carsList;
		
	}
	
	@Override
	public int getCount() {
		return carsList.size();
		
	}

	@Override
	public Cars getItem(int position) {
		// TODO Auto-generated method stub
		return carsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
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
			holder.Line = convertView.findViewById(R.id.view_line);
			holder.mCarMoneyMark=(TextView) convertView.findViewById(R.id.tv_car_money_tv);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (fragment.equals("carcar")) {
			holder.mChekced.setVisibility(View.VISIBLE);
			holder.mCartCar.setVisibility(View.GONE);
			holder.Line.setVisibility(View.GONE);
		}
		if (fragment.equals("market")) {
			holder.mChekced.setVisibility(View.GONE);
			holder.mCartCar.setVisibility(View.VISIBLE);
			holder.Line.setVisibility(View.VISIBLE);
		}
		
		Cars item = getItem(position);
		holder.mCarTime.setText(item.carDesc);
		holder.mCarInfo.setText(item.carName);
		//用户认证后才能查看价格
		boolean isCertified = PreferUtils.getBoolean(context, "isCertified", false);
		if(isCertified)
		{
			holder.mCarMoney.setText(item.carPrice+"");
			holder.mCarMoneyMark.setVisibility(View.VISIBLE);
		}else{
			holder.mCarMoney.setText("认证用户可查看价格");
			holder.mCarMoneyMark.setVisibility(View.INVISIBLE);
			holder.mCarMoney.setTextSize(8);
		}
		
		Glide.with(context).load(item.carImgURL).placeholder(R.drawable.displaycar).into(holder.mCarImage);
		String carAction = item.carAction;
		if("抢购".equals(carAction))
		{
			holder.mIvLevel.setImageResource(R.drawable.miao);
		}
		boolean isLogin = PreferUtils.getBoolean(context, "isLogin", false);
		//用户登录后才能加入购物车
		if(isLogin)
		{
			if(item.inCart)
			{
				holder.mCartCar.setImageResource(R.drawable.minicardefault);
			}else{
				holder.mCartCar.setImageResource(R.drawable.minicar);
			}
		}else{
			holder.mCartCar.setImageResource(R.drawable.minicar);
			CarsManager.getInstance().clearCartCar();
		}
		
		CarsInfo cartCarMap = CarsManager.getInstance().getCartCarMap().get(item.carID);
		if(cartCarMap!=null)
		{
			holder.mCartCar.setImageResource(R.drawable.minicardefault);
		}

		holder.mChekced.setOnClickListener(this);
		holder.mCartCar.setOnClickListener(this);

		holder.mChekced.setTag(position);
		holder.mCartCar.setTag(position);

		return convertView;
	}

	// 单选
	public void selectSingle(int id) {

		Cars select=getItem(id);
		if(!select.inCart)
		{
			select.inCart=true;
			CarsManager.getInstance().joinCartCar(select);
		}
		
		notifyDataSetChanged();
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
	}

	// 响应按钮点击事件,调用子定义接口，并传入View
	@Override
	public void onClick(View v) {
		mCallback.click(v);

	}

}

