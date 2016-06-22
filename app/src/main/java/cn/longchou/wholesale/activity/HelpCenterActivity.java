package cn.longchou.wholesale.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.utils.UIUtils;
import cn.longchou.wholesale.view.ItemHelpCenter;
/**
 * 
* @Description: 帮助中心界面
*
* @author kangkang
*
* @date 2016年1月14日 上午11:05:15 
*
 */
public class HelpCenterActivity extends BaseActivity {

	private ImageView mBack;
	private TextView mTitle;
	private ItemHelpCenter mQuestion;
	private ItemHelpCenter mBuyCar;
	private ItemHelpCenter mInventory;
	private ItemHelpCenter mCertification;
	private ItemHelpCenter mIntruduction;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_help_center);

		 mBack = (ImageView) findViewById(R.id.iv_my_news_back);
	     mTitle = (TextView) findViewById(R.id.tv_my_news_title);
	     
	     mQuestion = (ItemHelpCenter) findViewById(R.id.help_common_question);
	     mBuyCar = (ItemHelpCenter) findViewById(R.id.help_buycar_process);
	     mInventory = (ItemHelpCenter) findViewById(R.id.help_apply_inventory);
	     mCertification = (ItemHelpCenter) findViewById(R.id.help_apply_certification);
	     mIntruduction = (ItemHelpCenter) findViewById(R.id.help_car_intruduction);
	}

	@Override
	public void initData() {
		mTitle.setText("帮助中心");
		
		//更改图标
		mQuestion.changeImage(R.drawable.help_question);
		mBuyCar.changeImage(R.drawable.help_buy_car);
		mInventory.changeImage(R.drawable.help_guo_hu);
		mCertification.changeImage(R.drawable.help_tui_kuan);
		mIntruduction.changeImage(R.drawable.help_apply_intruduction);
		
		//更改文字
		mQuestion.changeText("常见问题");
		mBuyCar.changeText("买车流程");
		mInventory.changeText("过户流程");
		mCertification.changeText("退款流程");
		mIntruduction.changeText("隆筹好车介绍");
		
		//去掉下划线
		mQuestion.setLineVisible(false);
		mCertification.setLineVisible(false);
		mIntruduction.setLineVisible(false);
		
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		
		//各个条目设置点监听
		mQuestion.setOnClickListener(this);
		mBuyCar.setOnClickListener(this);
		mInventory.setOnClickListener(this);
		mCertification.setOnClickListener(this);
		mIntruduction.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		Intent intent=new Intent(HelpCenterActivity.this,HelpQuestionActivity.class);
		switch (v.getId()) {
		
		case R.id.iv_my_news_back:
			finish();
			break;
		case R.id.help_common_question:
			intent.putExtra("title", "常见问题");
			startActivity(intent);
			break;
		case R.id.help_buycar_process:
			intent.putExtra("title", "买车流程");
			startActivity(intent);
			break;
		case R.id.help_apply_inventory:
			intent.putExtra("title", "过户流程");
			startActivity(intent);
			break;
		case R.id.help_apply_certification:
			intent.putExtra("title", "退款流程");
			startActivity(intent);
			break;
		case R.id.help_car_intruduction:
			intent.putExtra("title", "隆筹好车介绍");
			startActivity(intent);
			break;
		

		default:
			break;
		}

	}

}
