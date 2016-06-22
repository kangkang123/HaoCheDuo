package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * 
* @Description: 筛选界面的可用品牌及车系
*
* @author kangkang
*
* @date 2016年1月28日 上午11:08:11 
*
 */
public class AvailBrand {

	//存放热门品牌
	public static List<String> mHotBrandList=new ArrayList<String>();
	
	//存放字母排序品牌
	public static List<String> mSortBrandList=new ArrayList<String>();
	
	//带有A,B,C的字母排序
	public static List<String> mSortABBrandList=new ArrayList<String>();
	
	//存放求购中选中的热门车系
	public static String mHotCarLine=null;
	
	//存放求购中选中的热门品牌
	public static String mHotBrand=null;
	
	//存放筛选中选中的热门车系
	public static String mScreenHotCarLine="全部";
	
	//存放筛选中选中的热门品牌
	public static String mScreenHotBrand="全部";
	
	//存放热门品牌及车系的所有内容
	public static Map<String,List<String>> hotBrands=new HashMap<String, List<String>>();
	
	public static Map<String,List<String>> sortBrands=new HashMap<String, List<String>>();
	
	//存放按字母排序的列表
//	public static Map<String, Map<String, List<String>>> sortBrands=new HashMap<String, Map<String, List<String>>>();
	
	
	//索引的个数
	public int indexCount;
	
	//品牌
	public Map<String,Map<String,List<String>>> brand;
	
	//热门品牌
	public Map<String,List<String>> hotBrand;
	
	//下标索引
	public List<String> index;
	
	//设置热门品牌的内容
	public static void setHotBrand(Map<String,List<String>> hotBrand){
		AvailBrand.hotBrands=hotBrand;
		Iterator<String> it = hotBrand.keySet().iterator();
//		//只有当热门品牌中不包含全部的时候才放入全部
//		if(!mHotBrandList.contains("全部"))
//		{
//			mHotBrandList.add("全部");
//		}
		while(it.hasNext())
		{
			//获取车的品牌
			String key = it.next();
			//判断集合中是否包含品牌，如果包含则不重复添加
			if(!mHotBrandList.contains(key)){
				//存放所有的热门的品牌
				mHotBrandList.add(key);
			}
		}
	}
	
	//所有的车的品牌，用于排序的搜索
	public static void setSortBrand(Map<String, Map<String, List<String>>> brand) {
		//把品牌和对应的车系保存起来
//		AvailBrand.sortBrands=brand;
		List<String> list=null;
		
		//第一次的所有键是A,B,C等
		Iterator<String> letter=brand.keySet().iterator();
		while (letter.hasNext()) {
			String next = letter.next();
			if(!mSortABBrandList.contains(next))
			{
//				list=new ArrayList<String>();
//				list.add(next);
//				sortBrands.put(next, list);
				
				//保存字母ABC
				mSortABBrandList.add(next);
				
				//根据键获取到值，包含品牌车系等，List<String>存放的是品牌对应的车系,单个字母下的map集合
				Map<String, List<String>> brands = brand.get(next);
//				sortBrands.putAll(brands);
				
				//获取所有的键，及品牌
				Iterator<String> itBrand=brands.keySet().iterator();
				//遍历品牌，取出所有的品牌
				while(itBrand.hasNext())
				{
					String key = itBrand.next();
					if(!mSortBrandList.contains(key))
					{
						mSortBrandList.add(key);
						mSortABBrandList.add(key);
					}
					if(!sortBrands.containsKey(key))
					{
						sortBrands.put(key, brands.get(key));
					}
					
				}
			}
			
		}

	}
	
	//获取保存的带有字母的map集合
	public static Map<String,List<String>> getMapSortBrand()
	{
		return sortBrands;
	}
	
	//得到字母排序的品牌
	public static List<String> getSortBrand()
	{
		return mSortBrandList;
	}
	
	//得到字母排序的品牌(包含字母)
	public static List<String> getSortABCBrand()
	{
		return mSortABBrandList;
	}
	
	//得到热门品牌
	public static List<String> getHotBrand()
	{
		return mHotBrandList;
	}
	
	//热门品牌类
	public class HotBrand{
		public String brand;
		public boolean select;
		
		public HotBrand(String brand,boolean select) {
			this.brand=brand;
			this.select=select;
		}
		
		//重写equals方法
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof HotBrand))
			{
				throw new ClassCastException("类型不匹配");
			}
			HotBrand s=(HotBrand) o;
			return this.brand.equals(s.brand);
		}
	}
	
}
