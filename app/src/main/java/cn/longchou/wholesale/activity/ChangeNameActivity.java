package cn.longchou.wholesale.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.SaveMyInfo;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
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
                if(TextUtils.isEmpty(mChangeName.getText().toString().trim())){
                    UIUtils.showToastSafe("用户名不能为空！");
                }else{
                    saveMyInfo("nickName",mChangeName.getText().toString().trim()+"");
                }
                break;
        }

    }

    //修改个人信息
    private void saveMyInfo(String key, final String value){
        String token= PreferUtils.getString(getApplicationContext(),"token",null);
        HttpUtils http=new HttpUtils();
        String url= Constant.RequestSaveMyInfo;
        RequestParams params=new RequestParams();
        params.addBodyParameter(key,value);
        params.addBodyParameter("Token",token);
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result=responseInfo.result;
                Gson gson=new Gson();
                SaveMyInfo json = gson.fromJson(result, SaveMyInfo.class);
                UIUtils.showToastSafe(json.msg);
                Constant.personName=value;
                setResult(4);
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}
