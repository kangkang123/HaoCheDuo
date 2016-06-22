package cn.longchou.wholesale.holder;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.domain.StockFinance;
import cn.longchou.wholesale.utils.UIUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class StockFinanceHolder extends BaseHolder<StockFinance>{

	private TextView mTitle;
	private TextView mContent;
	private ImageView mLine;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.item_list_stock_finance);
		mTitle = (TextView) view.findViewById(R.id.tv_item_stock_finance);
		mContent = (TextView) view.findViewById(R.id.tv_item_stock_finance_content);
		mLine = (ImageView) view.findViewById(R.id.iv_stock_finance_line);
		return view;
	}

	@Override
	public void refreshView() {
		StockFinance data = getData();
		mTitle.setText(data.title);
		mContent.setText(data.content);
		
	}

}
