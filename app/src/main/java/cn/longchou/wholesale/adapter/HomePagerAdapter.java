package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.List;



















import cn.longchou.wholesale.activity.ImageShowActivity;
import cn.longchou.wholesale.activity.ProtocolActivity;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.UIUtils;
import android.R.interpolator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

/**
 * 
* @Description: viewPager的的轮播条的填充
*
* @author kangkang 
*
* @date 2015年12月22日 下午11:18:45 
*
 */
public class HomePagerAdapter extends PagerAdapter {

	private ArrayList<String> carImags;
	private Context context;
	private String mark;
	List<ImageView> images=new ArrayList<ImageView>();
	public HomePagerAdapter(Context context,List<ImageView> images,List<String> carImags,String mark) {
		this.images=images;
		this.carImags=(ArrayList<String>) carImags;
		this.context=context;
		this.mark=mark;
	}


	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view==object;
	}

	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		final int pos=position % images.size();
		final ImageView view=images.get(pos);
//		if(carImags!=null)
//		{
			//给image设置点击事件
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//如果是点击首页banner图跳转的时候
					if(mark.equals("return"))
					{
						String url = carImags.get(pos);
						if(!TextUtils.isEmpty(url))
						{
							Intent intent=new Intent(context,ProtocolActivity.class);
							intent.putExtra("redir", "redir");
							context.startActivity(intent);
						}
					}//如果是点击车辆详情图片的时候
					else if(mark.equals("image"))
					{
						Intent intent=new Intent(context,ImageShowActivity.class);
						intent.putExtra("image_index", position);
						intent.putStringArrayListExtra("carsImages", carImags);
						context.startActivity(intent);
					}
				}
			});
//		}
		
		//判断容器中是否有内容，如果有内容的话就移除里面的内容
		ViewParent vp =view.getParent();  
        if (vp!=null){  
            ViewGroup parent = (ViewGroup)vp;  
            parent.removeView(view);  
        }  
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
