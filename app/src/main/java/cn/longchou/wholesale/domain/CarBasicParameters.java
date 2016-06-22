package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
* @Description: 车辆基本参数
*
* @author kangkang
*
* @date 2016年2月3日 下午2:03:31 
*
 */

public class CarBasicParameters {

	public String key;
	public String value;
	public CarBasicParameters(String key,String value) {
		this.key=key;
		this.value=value;
	}
	
	public static List<CarBasicParameters> getListResult(String result)
	{
		
		return null;
		
	}
	
	public static  List<CarBasicParameters> getResult(String result) throws Exception
	{
		List<CarBasicParameters> list=new ArrayList<CarBasicParameters>();
		JSONObject json = new JSONObject(result);
		
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String next = keys.next();
			String string = json.getString(next);
			if("true".equals(string))
			{
				string="有";
			}else if("false".equals(string))
			{
				string="无";
			}
			list.add(new CarBasicParameters(next, string));
		}
		
		return list;
	}
}
