package cn.longchou.wholesale.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.longchou.wholesale.R;

/**
 * Created by kangkang on 2016/6/15.
 * 反馈页面的adapter
 */
public class PopWindowAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private List<String> mList;

    public PopWindowAdapter(List<String> list, Context context,List<String> mList) {
        this.list = list;
        this.context = context;
        this.mList=mList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null)
        {
            holder=new ViewHolder();
            convertView=View.inflate(context, R.layout.iem_lv_pop,null);
            holder.mTextView= (TextView) convertView.findViewById(R.id.tv_item_pop);
            holder.mImageView= (ImageView) convertView.findViewById(R.id.iv_pop_tick);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(list.get(position));
        String choose=list.get(position);
        holder.mImageView.setVisibility(View.GONE);
        if(mList.contains(choose))
        {
            holder.mImageView.setVisibility(View.VISIBLE);
        }else{
            holder.mImageView.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        TextView mTextView;
        ImageView mImageView;
    }
}
