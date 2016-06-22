package cn.longchou.wholesale.dialog;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseDialog;
import cn.longchou.wholesale.utils.SystemUtils;
import cn.longchou.wholesale.utils.UIUtils;

public class ImageShowDialog extends BaseDialog {

	private Context context;
	private ImageView image;
	private LinearLayout lLShow;
	public ImageShowDialog(Context context) {
		super(context);
		this.context=context;
	}
	
	public static void showDialog(Context context)
	{
		ImageShowDialog dialog=new ImageShowDialog(context);
		dialog.show();
	}

	@Override
	public void initView() {
		setContentView(R.layout.dialog_image_show);
		image = (ImageView) findViewById(R.id.iv_image_show);
		
		lLShow = (LinearLayout) findViewById(R.id.ll_image_show);

	}

	@Override
	public void initData() {
		image.setImageResource(R.drawable.header_image2);

		int width=SystemUtils.getScreenWidth(context)-40;
		FrameLayout.LayoutParams LLparams=(android.widget.FrameLayout.LayoutParams) lLShow.getLayoutParams();
		LLparams.width=width;
		LLparams.height=(width-60)*3/4;
		LLparams.leftMargin=20;
		LLparams.rightMargin=20;
		lLShow.setLayoutParams(LLparams);
		LinearLayout.LayoutParams params=(LayoutParams) image.getLayoutParams();
	    params.width=LLparams.width;
	    params.height=LLparams.height;
	    image.setLayoutParams(params);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void processClick(View v) {
		// TODO Auto-generated method stub

	}

}
