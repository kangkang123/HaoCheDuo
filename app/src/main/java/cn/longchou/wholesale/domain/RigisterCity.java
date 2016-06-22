package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
* @Description: 注册时的城市
*
* @author kangkang
*
* @date 2016年6月6日 下午4:09:20 
*
 */
public class RigisterCity {
	
	//获取所有的省份的名称
	public static List<String> getProvince=new ArrayList<String>();
	//获取所有的省份里面的城市的名称
	public static Map<String,List<String>> getProvinceCity=new HashMap<String,List<String>>();
	
	public List<ShopName> shopList;

	public class ShopName{
		public String id;
		public String shopAddress;
		public String shopName;
	}
	public static List<String> getShopName(List<ShopName> shopList){
		List<String> list=new ArrayList<String>();
		for(int i=0;i<shopList.size();i++)
		{
			ShopName shopName = shopList.get(i);
			list.add(shopName.shopName);
		}
		return list;
	}
	
	
	//得到所有的省
	public static List<String> getProvinceCity(String result)
	{
//		List<String> getProvince=new ArrayList<String>();
		try {
			JSONObject json=new JSONObject(result);
			JSONArray jsonArray = json.getJSONArray("cityList");
			//获取所有的直辖市
			JSONArray zxList = json.getJSONArray("zxList");
			for(int x=0;x<zxList.length();x++)
			{
				JSONObject obj=(JSONObject) zxList.get(x);
				String city = obj.getString("name");
				if(!getProvince.contains(city))
				{
					getProvince.add(city);
					List<String> citys=new ArrayList<String>();
					citys.add(city);
					getProvinceCity.put(city, citys);
				}
			}
			//最外层的省份
			for(int i=0;i<jsonArray.length();i++)
			{
			    //获取当前的省份
				JSONObject object = (JSONObject) jsonArray.get(i); 
				Iterator<String> keys = object.keys();
				while(keys.hasNext())
				{
					String key = keys.next();
					JSONArray jsonCity = (JSONArray) object.get(key);
					List<String> citys=new ArrayList<String>();
					for(int j=0;j<jsonCity.length();j++)
					{
						JSONObject obj=(JSONObject) jsonCity.get(j);
						String city = obj.getString("name");
						citys.add(city);
					}
				    //如果省市的集合中不包含省份的话就添加进去
					if(!getProvinceCity.containsKey(key))
					{
						getProvince.add(key);
						getProvinceCity.put(key, citys);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getProvince;
	}
	//获取城市和省份的集合
	public static Map<String, List<String>> getProvinceCity()
	{
		return getProvinceCity;
	}
	
	public static List<String> getProvice()
	{
		return getProvince;
	}
}
