package cn.longchou.wholesale.manage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.longchou.wholesale.domain.CarsInfo;
import cn.longchou.wholesale.domain.HomePage.Cars;


public class CarsManager {

	/** 用于记录购物车的信息 */
	private List<CarsInfo> mCartCarList = new ArrayList<CarsInfo>();
	
	private Map<Integer, CarsInfo> mCartCarMap = new ConcurrentHashMap<Integer, CarsInfo>();
	
	//用户记录关注的信息
	private Map<Integer, CarsInfo> mAttentionMap = new ConcurrentHashMap<Integer, CarsInfo>();
//	private Map<Integer, CarsInfo> mAttentionMap = new ConcurrentHashMap<Integer, CarsInfo>();
	
	private List<Integer> selectIDs=new ArrayList<Integer>();
	
	private static CarsManager instance;
	
	//获取CarsManager对象
    public static synchronized CarsManager getInstance() {
		if (instance == null) {
			instance = new CarsManager();
		}
		return instance;
	}
    
    public void joinCartCar(Cars info){
    	CarsInfo carsInfo=mCartCarMap.get(info.carID);
    	if(null==carsInfo)
    	{
    		carsInfo=CarsInfo.clone(info);
    		mCartCarMap.put(info.carID, carsInfo);
    		mCartCarList.add(carsInfo);
    	}
    }
    
    //清除购物车
    public void clearCartCar(){
    	mCartCarMap.clear();
    	mCartCarList.clear();
    }
    
    //设置购物车中金融的选中方案
    public void setCarPlanSelect(CarsInfo info,String plan)
    {
    	CarsInfo planceSelect = CarsInfo.setPlanceSelect(info, plan);
    	mCartCarMap.put(planceSelect.carID, planceSelect);
    	for(int i=0;i<mCartCarList.size();i++)
    	{
    		CarsInfo carsInfo = mCartCarList.get(i);
    		if(carsInfo.carID==info.carID)
    		{
    			mCartCarList.remove(i);
    			mCartCarList.add(i,planceSelect);
    		}
    	}
    }
    
    //更改购物车中的选择按钮的状态为true
    public void setCartCarSelectTrue(CarsInfo info)
    {
    	//得到更改后选中按钮的状态
    	CarsInfo setSelectTrue = CarsInfo.setSelectTrue(info);
    	//把改变后的值重新放入Map集合中
    	mCartCarMap.put(setSelectTrue.carID, setSelectTrue);
    	
    	for(int i=0;i<mCartCarList.size();i++)
    	{
    		CarsInfo carsInfo = mCartCarList.get(i);
    		if(carsInfo.carID==info.carID)
    		{
    			mCartCarList.remove(i);
    			mCartCarList.add(i,setSelectTrue);
    		}
    	}
    }
    
    //更改购物车中的选择按钮的状态为false
    public void setCartCarSelectFalse(CarsInfo info)
    {
    	//得到更改后选中按钮的状态
    	CarsInfo setSelectfalse = CarsInfo.setSelectFalse(info);
    	//把改变后的值重新放入Map集合中
    	mCartCarMap.put(setSelectfalse.carID, setSelectfalse);
    	
    	for(int i=0;i<mCartCarList.size();i++)
    	{
    		CarsInfo carsInfo = mCartCarList.get(i);
    		if(carsInfo.carID==info.carID)
    		{
    			mCartCarList.remove(i);
    			mCartCarList.add(i,setSelectfalse);
    		}
    	}
    }
    
    public void removeCartCar(Cars info)
    {
    	CarsInfo carsInfo=mCartCarMap.get(info.carID);
    	if(null!=carsInfo)
    	{
    		mCartCarMap.remove(carsInfo.carID);
    		mCartCarList.remove(carsInfo);
    	}
    }
    
    public void removeCartCar(CarsInfo info)
    {
    	CarsInfo carsInfo=mCartCarMap.get(info.carID);
    	if(null!=carsInfo)
    	{
    		mCartCarMap.remove(carsInfo.carID);
    		mCartCarList.remove(carsInfo);
    	}
    }
    
    //获取购物车Map集合
    public Map<Integer, CarsInfo> getCartCarMap()
    {
    	return mCartCarMap;
    }
    
    //获取购物车List列表
    public List<CarsInfo> getCartCarList()
    {
    	return mCartCarList;
    }
    
    //保存勾选的购物车id
    public void setSelectIds(Integer id)
    {
    	selectIDs.add(id);
    }
    
    //删除勾选的购物车id
    public void removeSelectIds(Integer id)
    {
    	if (selectIDs.contains(id)) {
    		selectIDs.remove(id);
		}
    }
    
    public List<Integer> getSelectList()
    {
    	return selectIDs;
    }
    
}
