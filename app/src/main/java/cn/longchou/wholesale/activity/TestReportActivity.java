package cn.longchou.wholesale.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MachineAdapter;
import cn.longchou.wholesale.adapter.TestReportAdapter;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.HomePage.Cars;
import cn.longchou.wholesale.domain.TestReport;
import cn.longchou.wholesale.domain.TestReport.Items;
import cn.longchou.wholesale.domain.TestReport.Mechanical;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.SystemUtils;
import cn.longchou.wholesale.view.ListViewForScrollView;

import com.example.imagedemo.HackyViewPager;
import com.example.imagedemo.ImageDetailFragment;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;


public class TestReportActivity extends BaseActivity {
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			Bitmap bitmap=(Bitmap) msg.obj;
			mReport.setImageBitmap(bitmap);
			mReport.setOnTouchListener(new TouchListener());
			float s=getScreenWidth(getApplicationContext())/404f;
			Matrix matrix=new Matrix();
			matrix.setScale(s, s);
			mReport.setImageMatrix(matrix);
		};
	};

	private ImageView mBack;
	private TextView mTitle;
	private LinearLayout mLLMachine;
	private ListView mLvMachine;
	private ImageView mIvExterior;
	private ImageView mIvTrim;
	private ImageView mIvBones;
	private ListViewForScrollView mLvReport;
	private TextView mCarHurt;
	private RatingBar mRBExterior;
	private RatingBar mRBTrim;
	private TextView mZhiCheck;
	private ImageView mReport;
	private float mx,my;
	private HackyViewPager mPager;

	@Override
	public void initView() {
		setContentView(R.layout.activity_test_report);

		mBack = (ImageView) findViewById(R.id.iv_my_news_check);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title_check);

		//机械及电器的布局
		mLLMachine = (LinearLayout) findViewById(R.id.ll_machinery_electrical);

		//机械及电器的列表
		mLvMachine = (ListView) findViewById(R.id.lv_machine_electrical);
		
		mLvReport = (ListViewForScrollView) findViewById(R.id.lv_test_report);
		
	    mIvExterior = (ImageView) findViewById(R.id.iv_check_report_exterior); 
	    mIvTrim = (ImageView) findViewById(R.id.iv_check_report_trim); 
	    mIvBones = (ImageView) findViewById(R.id.iv_check_report_bones); 
	    
	    mCarHurt = (TextView) findViewById(R.id.tv_check_report_car_hurt);
	    
	    mRBExterior = (RatingBar) findViewById(R.id.rb_test_report_exterior);
	    
	    mRBTrim = (RatingBar) findViewById(R.id.rb_test_report_trim);
	    
	    mZhiCheck = (TextView) findViewById(R.id.tv_my_title_check);
	    
	    mReport = (ImageView) findViewById(R.id.iv_report);
	    
	    mPager = (HackyViewPager) findViewById(R.id.pager);
	    
	    setViewPagerPercent(mIvExterior);
	    setViewPagerPercent(mIvTrim);
	    setViewPagerPercent(mIvBones);
	}
	
	//设置viewpager的轮播图的百分比
	private void setViewPagerPercent(ImageView iv) {
		//窗口高度    
	    int screenHeight = SystemUtils.getScreenWidth(getApplicationContext())*2/3;
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) iv
				.getLayoutParams();
		params.width=SystemUtils.getScreenWidth(getApplicationContext());
		params.height=screenHeight;
		iv.setLayoutParams(params);
	}

	@Override
	public void initData() {
		mTitle.setText("车况报告");

		Cars cars=(Cars) getIntent().getExtras().getSerializable("cars");
		List<Items> items = TestReport.getInstance().getTestItems();
//		setItems(items);
		getServerData(cars);
		
    }
	
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		int width = outMetrics.widthPixels;
		return width;
	}
	
	private void getTestReportImage(final String url) {
		new Thread(){
//			String url="http://7xt527.com2.z0.glb.qiniucdn.com/wholes/20160426/201604262720625";
			public void run() {
				URL ur;
				try {
					ur = new URL(url);
					URLConnection connection = ur.openConnection();
					InputStream inputStream = connection.getInputStream();
					Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					ByteArrayOutputStream baos =new ByteArrayOutputStream();  
					bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
//					mReport.setImageBitmap(bitmap);
					Message message = handler.obtainMessage();
					message.obj=bitmap;
					handler.sendMessage(message);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			};
		}.start();

	}
	
	private final class TouchListener implements OnTouchListener {  
        
        /** 记录是拖拉照片模式还是放大缩小照片模式 */  
        private int mode = 0;// 初始状态    
        /** 拖拉照片模式 */  
        private static final int MODE_DRAG = 1;  
        /** 放大缩小照片模式 */  
        private static final int MODE_ZOOM = 2;  
          
        /** 用于记录开始时候的坐标位置 */  
        private PointF startPoint = new PointF();  
        /** 用于记录拖拉图片移动的坐标位置 */  
        private Matrix matrix = new Matrix();  
        /** 用于记录图片要进行拖拉时候的坐标位置 */  
        private Matrix currentMatrix = new Matrix();  
        
      
        /** 两个手指的开始距离 */  
        private float startDis;  
        /** 两个手指的中间点 */  
        private PointF midPoint; 
        
  
        @Override  
        public boolean onTouch(View v, MotionEvent event) {  
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */  
            switch (event.getAction() & MotionEvent.ACTION_MASK) {  
            // 手指压下屏幕  
            case MotionEvent.ACTION_DOWN:  
                mode = MODE_DRAG;  
                // 记录ImageView当前的移动位置  
                currentMatrix.set(mReport.getImageMatrix());  
                startPoint.set(event.getX(), event.getY());  
                break;  
            // 手指在屏幕上移动，改事件会被不断触发  
            case MotionEvent.ACTION_MOVE:  
                // 拖拉图片  
                if (mode == MODE_DRAG) {  
                    float dx = event.getX() - startPoint.x; // 得到x轴的移动距离  
                    float dy = event.getY() - startPoint.y; // 得到x轴的移动距离  
                    // 在没有移动之前的位置上进行移动  
                    matrix.set(currentMatrix);  
                    matrix.postTranslate(dx, dy);  
                }  
                // 放大缩小图片  
                else if (mode == MODE_ZOOM) {  
                    float endDis = distance(event);// 结束距离  
                    if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10  
                        float scale = endDis / startDis;// 得到缩放倍数  
                        matrix.set(currentMatrix);  
                        matrix.postScale(scale, scale,midPoint.x,midPoint.y);  
                    }  
                }  
                break;  
            // 手指离开屏幕  
            case MotionEvent.ACTION_UP:  
                // 当触点离开屏幕，但是屏幕上还有触点(手指)  
            case MotionEvent.ACTION_POINTER_UP:  
                mode = 0;  
                break;  
            // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕  
            case MotionEvent.ACTION_POINTER_DOWN:  
                mode = MODE_ZOOM;  
                /** 计算两个手指间的距离 */  
                startDis = distance(event);  
                /** 计算两个手指间的中间点 */  
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10  
                    midPoint = mid(event);  
                    //记录当前ImageView的缩放倍数  
                    currentMatrix.set(mReport.getImageMatrix());  
                }  
                break;  
            }  
            mReport.setImageMatrix(matrix);  
            return true;  
        }  
  
        /** 计算两个手指间的距离 */  
        private float distance(MotionEvent event) {  
            float dx = event.getX(1) - event.getX(0);  
            float dy = event.getY(1) - event.getY(0);  
            /** 使用勾股定理返回两点之间的距离 */  
            return (float) Math.sqrt(dx * dx + dy * dy);
        }  
  
        /** 计算两个手指间的中间点 */  
        private PointF mid(MotionEvent event) {  
            float midX = (event.getX(1) + event.getX(0)) / 2;  
            float midY = (event.getY(1) + event.getY(0)) / 2;  
            return new PointF(midX, midY);  
        }  
  
    }  

	private void getServerData(Cars cars) {
		HttpUtils http=new HttpUtils();
		String url=Constant.RequestCheckReport;
		RequestParams params=new RequestParams();
		params.addBodyParameter("c", "as");
		params.addBodyParameter("carID", cars.carID+"");
		http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> resultInfo) {
				String result=resultInfo.result;
				parseData(result);
				
			}
		});
		
	}

	protected void parseData(String result) {
		Gson gson=new Gson();
		TestReport data = gson.fromJson(result, TestReport.class);
		if(null!=data)
		{
			List<String> images = data.carReportImages;
			if(images!=null)
			{
				getTestReportImage(images.get(0));
			}
//			Glide.with(this).load(images.get(0)).into(mReport);
//			String url=images.get(0);
//			String url="http://7xt527.com2.z0.glb.qiniucdn.com/wholes/20160426/201604262720625";
//			try {
//				URL ur=new URL(url);
//				URLConnection connection = ur.openConnection();
//				InputStream inputStream = connection.getInputStream();
//				Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(inputStream);
//				mReport.setImageBitmap(bitmap);
//				
//				
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
//		{
//			Items items = data.items;
//			if(null!=items)
//			{
//				List<Items> listItems = TestReport.getInstance().getItems(items);
//				setItems(listItems);
//			}
//			Map<String, String> map = data.mechanicals;
//			if(null!=map)
//			{
//				List<Mechanical> mechanicals = TestReport.getInstance().getMechanicals(map);
//				setMechanical(mechanicals);
//			}
//			if(null!=data.breakdowns)
//			{
//				if(TextUtils.isEmpty(data.breakdowns.breakdownDesc))
//				{
//					mCarHurt.setText(data.breakdowns.breakdownDesc);
//				}else{
//					mCarHurt.setText("无故障损伤");
//				}
//			}
//			if(null!=data.outside)
//			{
//				//设置星级
//				mRBExterior.setRating(data.outside.星级);
//				Glide.with(this).load(data.outside.outsideImageUrl).placeholder(R.drawable.displaycar).into(mIvExterior);
//			}
//			if(null!=data.inside)
//			{
//				mRBTrim.setRating(data.inside.星级);
//				Glide.with(this).load(data.inside.outsideImageUrl).placeholder(R.drawable.displaycar).into(mIvTrim);
//			}
//			if(null!=data.skeleton)
//			{
//				//设置图片
//				Glide.with(this).load(data.skeleton.numbericImage1).placeholder(R.drawable.displaycar).into(mIvBones);
//			}
//		}
		
	}

	//车辆损伤的情况
	private void setMechanical(List<Mechanical> mechanicals) {
		if(mechanicals.size()==0 || mechanicals==null)
		{
			mLLMachine.setVisibility(View.GONE);
		}else{
			mLLMachine.setVisibility(View.VISIBLE);
			MachineAdapter adapter=new MachineAdapter(getApplicationContext(),mechanicals);
			mLvMachine.setAdapter(adapter);
		}
		
	}

	//检查报告的异常情况
	private void setItems(List<Items> listItems) {
		TestReportAdapter adapter=new TestReportAdapter(TestReportActivity.this,listItems);
		mLvReport.setAdapter(adapter);
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mZhiCheck.setOnClickListener(this);
	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_check:
			finish();
			break;
		case R.id.tv_my_title_check:
			Intent intent=new Intent(TestReportActivity.this,ProtocolActivity.class);
			startActivity(intent);
			break;
		
		default:
			break;
		}

	}

}
