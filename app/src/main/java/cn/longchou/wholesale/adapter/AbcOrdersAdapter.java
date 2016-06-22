package cn.longchou.wholesale.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cn.longchou.wholesale.R;
import cn.longchou.wholesale.adapter.MyBuyDrawerAdapter.ViewHolder;
import cn.longchou.wholesale.domain.AvailBrand;

import com.justlcw.letterlistview.letter.LetterBaseListAdapter;

/**
 * 
* @Description: 字母排序的adapter
*
* @author kangkang
*
* @date 2016年2月23日 上午9:39:16 
*
 */
public class AbcOrdersAdapter extends LetterBaseListAdapter<NameValuePair>{
    /** 字母对应的key,因为字母是要插入到列表中的,为了区别,所有字母的item都使用同一的key.  **/
    private static final String LETTER_KEY = "letter";
    
    private Context context;
    private String choose;
    
    /** 这里的数据都已经按着字母排序好了, 所以传入进来的数据也应排序好,不然会出现跳转问题.  **/
    
        
    
    public AbcOrdersAdapter(Context context,String choose)
    {
        super();
        this.context=context;
        this.choose=choose;
        
        List<String> sortBrand = AvailBrand.getSortBrand();
        
        List<NameValuePair> dataList = new ArrayList<NameValuePair>();
        for(int i=0; i<sortBrand.size(); i++)
        {
            NameValuePair pair = new BasicNameValuePair(String.valueOf(i), sortBrand.get(i));
            dataList.add(pair);
        }
        setContainerList(dataList);
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public String getItemString(NameValuePair t)
    {
        return t.getValue();
    }

    @Override
    public NameValuePair create(char letter)
    {
        return new BasicNameValuePair(LETTER_KEY, String.valueOf(letter));
    }

    @Override
    public boolean isLetter(NameValuePair t)
    {
        //判断是不是字母行,通过key比较,这里是NameValuePair对象,其他对象,就由你自己决定怎么判断了.
        return t.getName().equals(LETTER_KEY);
    }

    @Override
    public View getLetterView(int position, View convertView, ViewGroup parent)
    {
        //这里是字母的item界面设置.
        if(convertView == null)
        {
            convertView = new TextView(context);
            ((TextView)convertView).setGravity(Gravity.CENTER_VERTICAL);
            ((TextView)convertView).setTextSize(14);
            convertView.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }
        ((TextView)convertView).setText(list.get(position).getValue());
        
       
        return convertView;
    }

    @Override
    public View getContainerView(int position, View convertView, ViewGroup parent)
    {
        //这里是其他正常数据的item界面设置.
    	 ViewHolder holder=null;
 		if(convertView==null)
 		{
 			holder=new ViewHolder();
 			convertView=View.inflate(context, R.layout.item_dl_my_buy, null);
 			holder.choose=(TextView) convertView.findViewById(R.id.tv_my_buy_right_choose);
 			holder.tick=(ImageView) convertView.findViewById(R.id.iv_my_buy_right_check);
 			
 			convertView.setTag(holder);
 		}else{
 			holder=(ViewHolder) convertView.getTag();
 		}
         holder.choose.setText(list.get(position).getValue());
         //筛选界面的内容
         if("brand".equals(choose))
         {
        	 if(list.get(position).getValue().equals(AvailBrand.mScreenHotBrand))
 			{
 				holder.tick.setVisibility(View.VISIBLE);
 				holder.choose.setTextColor(Color.rgb(237, 108, 1));
 			}else{
 				holder.tick.setVisibility(View.INVISIBLE);
 				holder.choose.setTextColor(Color.rgb(51, 51, 51));
 		    }
        	 //求购界面的内容
         }else if("buy".equals(choose))
         {
        	 if(list.get(position).getValue().equals(AvailBrand.mHotBrand))
             {
            	 holder.tick.setVisibility(View.VISIBLE);
            	 holder.choose.setTextColor(Color.rgb(237, 108, 1));
             }else{
            	 holder.tick.setVisibility(View.INVISIBLE);
            	 holder.choose.setTextColor(Color.rgb(51, 51, 51));
             }
         }
         
        return convertView;
    }
    
    class ViewHolder{
		private TextView choose;
		private ImageView tick;
	}
}
