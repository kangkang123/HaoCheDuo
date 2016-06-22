package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.longchou.wholesale.bean.CityProvinces;
/**
 * 
* @Description: 获取的城市定位数据
*
* @author kangkang
*
* @date 2016年1月30日 下午5:42:58 
*
 */
public class CityLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//四个市
	public List<String> municipality;
	//省市的数据
	public Map<String,List<String>> provinces;
	
	private static CityLocation cityLocation;
	private static List<CityProvinces> mCitys=new ArrayList<CityProvinces>();
	
	private static List<CityProvinces> mAllCitys=new ArrayList<CityProvinces>();
	
	//保存一级城市中选中的城市
	private static List<CityProvinces> mFirstChoose=new ArrayList<CityProvinces>();
	
	//保存一级城市中取消选中保存的城市
	private static List<CityProvinces> mFirstRemove=new ArrayList<CityProvinces>();
	
	//保存二级城市中选中的城市
	private static List<CityProvinces> mSecondChoose=new ArrayList<CityProvinces>();
	
	//保存二级城市中取消选中是保存的城市
	private static List<CityProvinces> mSecondRemove=new ArrayList<CityProvinces>();
	
	//我的求购中的城市
	private static List<String> mBuyCityList=new ArrayList<String>();
	
	//我的求购中选中的城市
	public static String mBuyCity="上海";
	
	public static void setCityLocation(CityLocation cityLocation)
	{
		CityLocation.cityLocation=cityLocation;
	}
	
	//一级城市选择选中城市放入
	public static List<CityProvinces> getFirstChoose()
	{
		return mFirstChoose;
	}
	
	//一级城市选择取消选中时放入的城市
	public static List<CityProvinces> getFirstRemove()
	{
		return mFirstRemove;
	}
	
	//二级城市选择选中是的城市放入
	public static List<CityProvinces> getSecondChoose()
	{
		return mSecondChoose;
	}
	
	//二级城市选择取消选择的城市放入
	public static List<CityProvinces> getSecondRemove()
	{
		return mSecondRemove;
	}
	
    //二级城市选择清除所有的城市选择
	public static void clearSecondChoose()
	{
		mSecondChoose.clear();
	}
	
	public static CityLocation getCityProvinces()
	{
		return cityLocation;
	}
	
	public static List<CityProvinces> getFirstLocation(CityLocation cityLocation){
		
		if(!mAllCitys.contains(new CityProvinces("全国", false)))
		{
			//首先把全国放入list集合中
			mCitys.add(new CityProvinces("全国", false));
			
			mAllCitys.add(new CityProvinces("全国", false));
			CityProvinces cityProvinces = mAllCitys.get(0);
			CityLocation.setCitySelectTrue(cityProvinces);
//			CityProvinces cityTrue = CityProvinces.getInstance().getCityProvincesTrue(cityProvinces);
		}
		
		List<String> list = cityLocation.municipality;
		//遍历list集合找出所有的城市添加到citys集合中，标识为false因为都不是省份
		for(int i=0;i<list.size();i++)
		{
			String city = list.get(i);
			if(!mAllCitys.contains(new CityProvinces(city,false)))
			{
				mCitys.add(new CityProvinces(city,false));
				
				mAllCitys.add(new CityProvinces(city,false));
				
				mBuyCityList.add(city);
			}
			
		}
		//遍历map集合取出所有的内容，然后放入citys集合中，标识为true因为都是省份
		
		Map<String, List<String>> provinces = cityLocation.provinces;
		Iterator<String> it=provinces.keySet().iterator();
		while (it.hasNext()) {
			String next = it.next();
			if(!mCitys.contains(new CityProvinces(next, true)))
			{
				mCitys.add(new CityProvinces(next, true));
			}
			
			List<String> city = provinces.get(next);
			for(int i=0;i<city.size();i++)
			{
				String string = city.get(i);
				if(!mAllCitys.contains(new CityProvinces(string, true)))
				{
					mAllCitys.add(new CityProvinces(string, true));
					
					mBuyCityList.add(string);
				}
			}
		}
		return mCitys;
	}
	
	//获取我的求购中的所有城市
	public static List<String> getBuyCity(){
		
		return mBuyCityList;
	}
	
	//改变城市选择的状态为true
	public static void setCitySelectTrue(CityProvinces cityProvinces)
	{
		CityProvinces cityProvincesTrue = CityProvinces.getInstance().getCityProvincesTrue(cityProvinces);
		for(int i=0;i<mAllCitys.size();i++)
		{
			CityProvinces citys = mAllCitys.get(i);
			if(citys.provinces.equals(cityProvincesTrue.provinces))
			{
				mAllCitys.remove(i);
				mAllCitys.add(i, cityProvincesTrue);
			}
		}
	}
	
	//改变城市选择的状态为false
	public static void setCitySelectFalse(CityProvinces cityProvinces)
	{
		CityProvinces cityProvincesTrue = CityProvinces.getInstance().getCityProvincesFalse(cityProvinces);
		for(int i=0;i<mAllCitys.size();i++)
		{
			CityProvinces citys = mAllCitys.get(i);
			if(citys.provinces.equals(cityProvincesTrue.provinces))
			{
				mAllCitys.remove(i);
				mAllCitys.add(i, cityProvincesTrue);
			}
		}
	}
	
	//改变求购城市的状态为true
	public static void setCityBuyTrue(CityProvinces cityProvinces)
	{
		CityProvinces cityProvincesTrue = CityProvinces.getInstance().getCityBuyTrue(cityProvinces);
		for(int i=0;i<mAllCitys.size();i++)
		{
			CityProvinces citys = mAllCitys.get(i);
			if(citys.provinces.equals(cityProvincesTrue.provinces))
			{
				mAllCitys.remove(i);
				mAllCitys.add(i, cityProvincesTrue);
			}
		}
	}
	
	//改变求购城市的状态为false
	public static void setCityBuyFalse(CityProvinces cityProvinces)
	{
		CityProvinces cityProvincesTrue = CityProvinces.getInstance().getCityBuyFalse(cityProvinces);
		for(int i=0;i<mAllCitys.size();i++)
		{
			CityProvinces citys = mAllCitys.get(i);
			if(citys.provinces.equals(cityProvincesTrue.provinces))
			{
				mAllCitys.remove(i);
				mAllCitys.add(i, cityProvincesTrue);
			}
		}
	}
	
	public static List<CityProvinces> getAllCitys()
	{
		return mAllCitys;
	}
	
	public static List<CityProvinces> getCitys()
	{
		return mCitys;
	}
	
}
