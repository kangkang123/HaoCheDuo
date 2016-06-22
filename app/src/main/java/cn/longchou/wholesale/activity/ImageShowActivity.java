package cn.longchou.wholesale.activity;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.example.imagedemo.HackyViewPager;
import com.example.imagedemo.ImageDetailFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.SystemUtils;
/**
 * 
* @Description: 显示图片的activity
*
* @author kangkang
*
* @date 2016年3月22日 下午1:47:27 
*
 */
public class ImageShowActivity extends BaseActivity {

	private ImageView imageView;
	private ImageView mBack;
	private TextView mTitle;
	
	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index"; 
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;

	@Override
	public void initView() {
		setContentView(R.layout.activity_image_show);
		imageView = (ImageView) findViewById(R.id.iv_show);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		mPager = (HackyViewPager) findViewById(R.id.pager);
		
		indicator = (TextView) findViewById(R.id.indicator);

	}
	

	@Override
	public void initData() {
		mTitle.setText("图片");
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		String carUrl = getIntent().getStringExtra("car");
//		ArrayList<String> images=(ArrayList<String>) getIntent().getSerializableExtra("carsImages");
		ArrayList<String> images =getIntent().getStringArrayListExtra("carsImages");
//		Glide.with(this).load(carUrl).into(imageView);
//		
//		imageView.setOnTouchListener(new TouchListener());
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), images);
		mPager.setAdapter(mAdapter);
		
		ImageLoader imageLoader=ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(ImageShowActivity.this));
		imageLoader.displayImage(carUrl, imageView);
		
		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager.getAdapter().getCount());
		indicator.setText(text);
		// 更新下标
		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator, arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}

		});
//		if (savedInstanceState != null) {
//			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
//		}

		mPager.setCurrentItem(pagerPosition);

	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
	
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
			break;
		default:
			break;
		}

	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}
	
	private class ImagePagerAdapter extends FragmentStatePagerAdapter {

		public ArrayList<String> fileList;

		public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public Fragment getItem(int position) {
			String url = fileList.get(position);
			return ImageDetailFragment.newInstance(url);
		}

	}

}
