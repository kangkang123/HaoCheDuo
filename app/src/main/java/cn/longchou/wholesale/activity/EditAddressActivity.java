package cn.longchou.wholesale.activity;

import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import cn.longchou.wholesale.R;
import cn.longchou.wholesale.base.BaseActivity;
import cn.longchou.wholesale.domain.AddressList;
import cn.longchou.wholesale.domain.SaveMyInfo;
import cn.longchou.wholesale.global.Constant;
import cn.longchou.wholesale.utils.PreferUtils;
import cn.longchou.wholesale.utils.UIUtils;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

/**
 * 
* @Description: 编辑地址
*
* @author kangkang
*
* @date 2016年6月4日 下午4:16:07 
*
 */
public class EditAddressActivity extends BaseActivity implements OnWheelChangedListener {

	private ImageView mBack;
	private TextView mTitle;
	private TextView mDeleteAddress;
	private EditText mName;
	private EditText mPhone;
	private EditText mCode;
	private EditText mcity;
	private EditText mCityDetail;
	private TextView mSave;
	private AddressList.UserAddressBean address;

	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private JSONObject mJsonObj;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;

	/**
	 * 所有省
	 */
	private String[] mProvinceDatas;
	/**
	 * key - 省 value - 市s
	 */
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区s
	 */
	private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();

	/**
	 * 当前省的名称
	 */
	private String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	private String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	private String mCurrentAreaName ="";

	@Override
	public void initView() {
		setContentView(R.layout.activity_eidt_address);
		mBack = (ImageView) findViewById(R.id.iv_my_news_back);
		mTitle = (TextView) findViewById(R.id.tv_my_news_title);
		
		mDeleteAddress = (TextView) findViewById(R.id.tv_delete_address);
		mName = (EditText) findViewById(R.id.et_address_name);
		mPhone = (EditText) findViewById(R.id.et_address_phone);
		mCode = (EditText) findViewById(R.id.et_address_code);
		mcity = (EditText) findViewById(R.id.et_address_city);
		mCityDetail = (EditText) findViewById(R.id.et_address_detail);
		mSave = (TextView) findViewById(R.id.tv_my_title_login);


		initJsonData();

		mProvince = (WheelView) findViewById(R.id.id_province);
		mCity = (WheelView) findViewById(R.id.id_city);
		mArea = (WheelView) findViewById(R.id.id_area);

		initDatas();

		mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);

		mProvince.setVisibleItems(5);
		mCity.setVisibleItems(5);
		mArea.setVisibleItems(5);
		updateCities();
		updateAreas();

	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities()
	{
		int pCurrent = mProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null)
		{
			cities = new String[] { "" };
		}
		mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas()
	{
		int pCurrent = mCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mAreaDatasMap.get(mCurrentCityName);

		if (areas == null)
		{
			areas = new String[] { "" };
		}
		mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
		mArea.setCurrentItem(0);
	}

	/**
	 * 解析整个Json对象，完成后释放Json对象的内存
	 */
	private void initDatas()
	{
		try
		{
			JSONArray jsonArray = mJsonObj.getJSONArray("citylist");
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonP = jsonArray.getJSONObject(i);// 每个省的json对象
				String province = jsonP.getString("p");// 省名字

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try
				{
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("c");
				} catch (Exception e1)
				{
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++)
				{
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("n");// 市名字
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try
					{
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("a");
					} catch (Exception e)
					{
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];// 当前市的所有区
					for (int k = 0; k < jsonAreas.length(); k++)
					{
						String area = jsonAreas.getJSONObject(k).getString("s");// 区域的名称
						mAreasDatas[k] = area;
					}
					mAreaDatasMap.put(city, mAreasDatas);
				}

				mCitisDatasMap.put(province, mCitiesDatas);
			}

		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		mJsonObj = null;
	}

	@Override
	public void initData() {
		mTitle.setText("编辑地址");
		mSave.setText("保存");
		mSave.setVisibility(View.VISIBLE);

		Bundle bundle = getIntent().getExtras();
		if(bundle!=null)
		{
			address = (AddressList.UserAddressBean)bundle.getSerializable("address");
			mName.setText(address.userName);
			mPhone.setText(address.phone);
			mCode.setText(address.postCode);
			mCurrentProviceName=address.province;
			mCurrentCityName=address.city;
			mCurrentAreaName=address.county;
			String city=address.province+address.city+address.county;
			mcity.setText(city);
			mCityDetail.setText(address.address);
			mDeleteAddress.setVisibility(View.VISIBLE);
		}else{
			mDeleteAddress.setVisibility(View.INVISIBLE);

		}

	}

	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private void initJsonData()
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("city.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1)
			{
				sb.append(new String(buf, 0, len));
			}
			is.close();
			mJsonObj = new JSONObject(sb.toString());
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void initListener() {
		mBack.setOnClickListener(this);
		mDeleteAddress.setOnClickListener(this);
		mSave.setOnClickListener(this);

	}

	@Override
	public void processClick(View v) {
		switch (v.getId()) {
		case R.id.iv_my_news_back:
			finish();
            break;
		//删除地址
		case R.id.tv_delete_address:
			deleteAddress();
			break;
		//保存地址
		case R.id.tv_my_title_login:
            saveAddress();
			break;

		default:
			break;
		}

	}

	//删除地址
	private void deleteAddress() {
		String token=PreferUtils.getString(getApplicationContext(),"token",null);
		HttpUtils http=new HttpUtils();
		String url= Constant.RequestDeleteAddress+address.id+"?";
		RequestParams params=new RequestParams();
		params.addBodyParameter("Token",token);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				Gson gson=new Gson();
				SaveMyInfo json = gson.fromJson(result, SaveMyInfo.class);
				UIUtils.showToastSafe(json.msg);
				setResult(1);
				EditAddressActivity.this.finish();

			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	//保存地址
	private void saveAddress() {
		String name = mName.getText().toString().trim();
		String phone = mPhone.getText().toString().trim();
		String code = mCode.getText().toString().trim();
		String city = mcity.getText().toString().trim();
		String cityDetail = mCityDetail.getText().toString().trim();

		if(TextUtils.isEmpty(name)){
			UIUtils.showToastSafe("姓名不能为空");
		}else if(TextUtils.isEmpty(phone)){
			UIUtils.showToastSafe("手机号不能为空");
		}else if(TextUtils.isEmpty(city)){
			UIUtils.showToastSafe("请选择省市区");
		}else if(TextUtils.isEmpty(cityDetail)){
			UIUtils.showToastSafe("请填写详细地址");
		}else{
			upLoadAddress(name,phone,code,mCurrentProviceName,mCurrentCityName,mCurrentAreaName,cityDetail);
		}
	}

	private void upLoadAddress(String name, String phone, String code, String province,String city,String county, String cityDetail) {
		String token=PreferUtils.getString(getApplicationContext(),"token",null);
		HttpUtils http=new HttpUtils();
		RequestParams params=new RequestParams();
		String url="";
		//如果address不为空的话是修改内容
		if(address!=null)
		{
			url=Constant.RequestUpdateAddress;
			String id=address.id;
			params.addBodyParameter("id",id);
		}//如果address为空的话是增加地址内容
		else{
			url= Constant.RequestAddressSave;
		}

		params.addBodyParameter("Token",token);
		params.addBodyParameter("userName",name);
		params.addBodyParameter("phone",phone);
		params.addBodyParameter("postCode",code);
		params.addBodyParameter("province",province);
		params.addBodyParameter("city",city);
		params.addBodyParameter("county",county);
		params.addBodyParameter("address",cityDetail);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result=responseInfo.result;
				System.out.print(result);
				Gson gson=new Gson();
				SaveMyInfo json = gson.fromJson(result, SaveMyInfo.class);
				UIUtils.showToastSafe(json.msg);
				setResult(1);
				EditAddressActivity.this.finish();
			}

			@Override
			public void onFailure(HttpException e, String s) {

			}
		});
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {

		if (wheel == mProvince)
		{
			updateCities();
		} else if (wheel == mCity)
		{
			updateAreas();
		} else if (wheel == mArea)
		{
			mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
		}

		mcity.setText(mCurrentProviceName + mCurrentCityName + mCurrentAreaName);
	}
}
