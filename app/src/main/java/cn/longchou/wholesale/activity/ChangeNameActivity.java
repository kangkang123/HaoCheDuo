package cn.longchou.wholesale.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.utils.UIUtils;

/**
 * Created by wfy on 2016/6/21.
 */
public class ChangeNameActivity extends BaseActivity {
    private ImageView mBack;
    private TextView mTitle;
    private TextView mComfirm;
    private EditText mChangeName;
    private ImageView mDelete;
    @Override
    public void initView() {
        setContentView(R.layout.activity_change_name);
        mBack = (ImageView) findViewById(R.id.iv_my_news_back);
        mTitle = (TextView) findViewById(R.id.tv_my_news_title);
        mComfirm = (TextView) findViewById(R.id.tv_my_title_login);
        mChangeName = (EditText) findViewById(R.id.et_change_name);
        mDelete = (ImageView) findViewById(R.id.iv_delete);
    }

    @Override
    public void initData() {
        mTitle.setText("修改登录名");
        mComfirm.setVisibility(View.VISIBLE);
        mComfirm.setText("确定");

    }

    @Override
    public void initListener() {
        mBack.setOnClickListener(this);
        mDelete.setOnClickListener(this);
        mComfirm.setOnClickListener(this);

    }

    @Override
    public void processClick(View v) {
        switch (v.getId()){
            case R.id.iv_my_news_back:
                finish();
                break;
            case R.id.iv_delete:
                mChangeName.setText("");
                break;
            case R.id.tv_my_title_login:
                UIUtils.showToastSafe(mChangeName.getText().toString().trim()+"");
                break;
        }

    }
}
