package cn.longchou.wholesale.activity;

import java.util.ArrayList;
import java.util.List;

import cn.kang.dialog.library.ConfirmDialog;
import cn.kang.dialog.library.ConfirmDialog.OnConfirmListener;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.fragment.CarMarketFragment;
import cn.longchou.wholesale.fragment.CarMarketListFragment;
import cn.longchou.wholesale.fragment.CustomServerFragment;
import cn.longchou.wholesale.fragment.FinanceFragment;
import cn.longchou.wholesale.fragment.MyCenterFrament;
import cn.longchou.wholesale.fragment.OneLevelFragment;
import cn.longchou.wholesale.fragment.OrdersFragment;
import cn.longchou.wholesale.fragment.ThreeLevelFragment;
import cn.longchou.wholesale.fragment.TwoLevelFragment;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.service.NotificationService;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.BadgeView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
/**
 * 
* @Description: 主页面
*
* @author kangkang 
*
* @date 2015年12月22日 下午11:17:37 
*
 */
public class MainActivity extends BaseActivity {

    public ViewPager mMainViewPager;
	private RadioGroup mRadioGroup;
	public RadioButton mRbCarMarket;
	public RadioButton mRbOrders;
	private RadioButton mMoney;
	private RadioButton mRbFinance;
	private List<Fragment> fragments;
	private FrameLayout mFlContent;
	private static final String TAG_LOGIN = "TAG_LOGIN";
	private RadioButton mRbMyCenter;
	private FragmentManager fragmentManager;
	
	private CarMarketFragment mCarMarketFragmet;
	private Bundle bundle;
	private OrdersFragment mOrdersFragment;
	private FinanceFragment mFinanceFragment;
	private MyCenterFrament mMyCenterFragment;
	private CarMarketListFragment mCarMarketList;
	private OneLevelFragment mOneLevel;
	private TwoLevelFragment mTwoLevelFragment;
	private ThreeLevelFragment mThreeLevelFragment;
	public FrameLayout mFlScreen;
	public static DrawerLayout mDrawerLayout;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_button);
        mRbCarMarket = (RadioButton) findViewById(R.id.rb_carmarket);
        mRbOrders = (RadioButton) findViewById(R.id.rb_orders);
        mRbFinance = (RadioButton) findViewById(R.id.rb_finance);
        mRbMyCenter = (RadioButton) findViewById(R.id.rb_mycenter);
        mRBService = (RadioButton) findViewById(R.id.rb_custom_service);
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
        
        button = (Button) findViewById(R.id.bt);
        
        mFlScreen = (FrameLayout) findViewById(R.id.fl_screen);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.dl_screen);
        Intent intent=new Intent(this,NotificationService.class);
        startService(intent);

	}

	//退出应用是保存状态，返回键
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
//        if (keyCode == KeyEvent.KEYCODE_BACK) {  
//            moveTaskToBack(false);  
//            return true;  
//        }  
//        return super.onKeyDown(keyCode, event);  
		if (((keyCode == KeyEvent.KEYCODE_BACK) ||(keyCode == KeyEvent.KEYCODE_HOME))&& event.getRepeatCount() == 0) 
		{
			//如果是从车市列表页面则调到首页
			if(Constant.isCarMarketList)
			{
				setButtonSelection(0);
				Constant.isCarMarketList=false;
				
			}else{
				//如果不是车市列表页面则弹出对话框退出应用
				showExitDialog();
			}
		}
		return false;
    } 
	
	private void showExitDialog() {
		ConfirmDialog.showDialog(MainActivity.this, "提示", "亲,您真的要退出吗？", new OnConfirmListener() {
			
			@Override
			public void onConfirm() {
				 android.os.Process.killProcess(android.os.Process.myPid());
				
			}
			
			@Override
			public void onCancel() {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	@Override
	public void initData() {
		
		 int select = 0;
	     bundle = getIntent().getExtras();
	        if (bundle != null){
	        	select = bundle.getInt("select");
	        }
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		//隐藏掉所有的Fragment
		hideFragments(ft);
        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rb_carmarket:
					setButtonSelection(0);
					break;
				case R.id.rb_orders:
					boolean isLogin = PreferUtils.getBoolean(getApplicationContext(), "isLogin", false);
					if(isLogin)
					{
						setButtonSelection(1);
					}else{
						Intent intent=new Intent(MainActivity.this,LoginActivity.class);
						startActivity(intent);
						setButtonSelection(0);
					}
					break;
				case R.id.rb_finance:
					setButtonSelection(2);	
					
					break;
				case R.id.rb_mycenter:
					setButtonSelection(3);
					
					break;
				case R.id.rb_custom_service:
					setButtonSelection(8);
					break;
				default:
					break;
				}
			}
		});
        //默认选中第一个
        setButtonSelection(select);
        
        //初始化关闭侧边栏
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	
        budget = new BadgeView(this, button);
//        budget.setText("12"); // 需要显示的提醒类容
        budget.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        budget.setTextColor(Color.WHITE); // 文本颜色
        budget.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
        budget.setTextSize(10); // 文本大小
		//badge1.setBadgeMargin(3, 3); // 水平和竖直方向的间距
        budget.setBadgeMargin(3); //各边间隔
		// badge1.toggle(); //显示效果，如果已经显示，则影藏，如果影藏，则显示
//        budget.show();// 只有显示
        budget.hide();//影藏显示
	}
	//设置角标的数据
	public static BadgeView getBadgeViewText()
	{
		return budget;
	}
	
	
	//设置接口把DrawerLayout传递出去
	private static OnScreenLisener onScreenLisener;
	private Button button;
	private static BadgeView budget;
	private RadioButton mRBService;
	private CustomServerFragment mServerFragment;
	
	
	public static void setOnScreenListener(OnScreenLisener onScreenLisener)
	{
		MainActivity.onScreenLisener=onScreenLisener;
	}
	
	public interface OnScreenLisener{
		void onDrawerLayout(DrawerLayout mDrawerLayout);
	}
	
	//提供方法把DrawerLayout对象传递到外面
	public static DrawerLayout getDrawerLayout(){
		
		return mDrawerLayout;
	}
	
	
	//设置选中的是哪个RadioButton
	public void setButtonSelection(int index) {
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
		hideFragments(transaction);
		switch (index) {
		case 0:
			if (mCarMarketFragmet == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				mCarMarketFragmet= new CarMarketFragment();
				mCarMarketFragmet.setArguments(bundle);
				transaction.add(R.id.fl_content, mCarMarketFragmet);
				
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(mCarMarketFragmet);
				mRbCarMarket.setChecked(true);
				
			}
			break;
		case 1:
			if (mOrdersFragment == null) {
				mOrdersFragment = new OrdersFragment();
				mOrdersFragment.setArguments(bundle);
				transaction.add(R.id.fl_content, mOrdersFragment);
			} else {
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(mOrdersFragment);
				mRbOrders.setChecked(true);
			}
			break;   
		case 2:
			if (mFinanceFragment == null) {
				mFinanceFragment = new FinanceFragment();
				mFinanceFragment.setArguments(bundle);
				transaction.add(R.id.fl_content, mFinanceFragment);
			} else {      
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(mFinanceFragment);
				mRbFinance.setChecked(true);
			}
			break;
		case 3:
			if (mMyCenterFragment == null){
				mMyCenterFragment = new MyCenterFrament();
				mMyCenterFragment.setArguments(bundle);
				transaction.add(R.id.fl_content, mMyCenterFragment);
			}else{
				transaction.show(mMyCenterFragment);
				mRbMyCenter.setChecked(true);
			}
			break;
		case 4:
			if (mCarMarketList == null){
				mCarMarketList = new CarMarketListFragment();
				mCarMarketList.setArguments(bundle);
				transaction.add(R.id.fl_content, mCarMarketList);
//				transaction.addToBackStack(null);
			}else{
				transaction.show(mCarMarketList);
			}
			break;
		case 5://筛选的一级fragment
			if (mOneLevel == null){
				mOneLevel = new OneLevelFragment();
				mOneLevel.setArguments(bundle);
				transaction.add(R.id.fl_screen, mOneLevel);
			}else{
				transaction.show(mOneLevel);
			}
			break;
		case 6://筛选的二级fragment
			if (mTwoLevelFragment == null){
				mTwoLevelFragment = new TwoLevelFragment();
				mTwoLevelFragment.setArguments(bundle);
				transaction.add(R.id.fl_screen, mTwoLevelFragment);
			}else{
				transaction.show(mTwoLevelFragment);
			}
			break;
		case 7://筛选的三级fragment
			if (mThreeLevelFragment == null){
				mThreeLevelFragment = new ThreeLevelFragment();
				mThreeLevelFragment.setArguments(bundle);
				transaction.add(R.id.fl_screen, mThreeLevelFragment);
			}else{
				transaction.show(mThreeLevelFragment);
			}
			break;
		case 8:
			if (mServerFragment == null) {
				mServerFragment = new CustomServerFragment();
				mServerFragment.setArguments(bundle);
				transaction.add(R.id.fl_content, mServerFragment);
			} else {      
				// 如果ContactsFragment不为空，则直接将它显示出来
				transaction.show(mServerFragment);
				mRBService.setChecked(true);
			}
			break;
		}
		transaction.commit();
	}
	
	//通过FragmentTransaction对象的hide方法隐藏Fragment
	private void hideFragments(FragmentTransaction transaction) {
		if (mCarMarketFragmet!= null) {
			transaction.hide(mCarMarketFragmet);
		}
		if (mOrdersFragment != null) {
			transaction.hide(mOrdersFragment);
		}
		if (mFinanceFragment != null){
			transaction.hide(mFinanceFragment);
		}
		if (mMyCenterFragment != null){
			transaction.hide(mMyCenterFragment);
		}
		if (mCarMarketList != null){
			transaction.hide(mCarMarketList);
		}
		if (mOneLevel != null){
			transaction.hide(mOneLevel);
		}
		if (mTwoLevelFragment != null){
			transaction.hide(mTwoLevelFragment);
		}
		if (mThreeLevelFragment != null){
			transaction.hide(mThreeLevelFragment);
		}
		if (mServerFragment != null){
			transaction.hide(mServerFragment);
		}
	}

	@Override
	public void initListener() {
		mDrawerLayout.setDrawerListener(new DrawerListener() {
			
			@Override
			public void onDrawerStateChanged(int newState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDrawerOpened(View drawerView) {
				
				
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				Constant.isCancel=true;
				setButtonSelection(4);
				
			}
		});
	}

	@Override
	public void processClick(View v) {
		
	}
}
