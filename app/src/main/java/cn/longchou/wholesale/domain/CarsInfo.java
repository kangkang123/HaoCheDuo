package cn.longchou.wholesale.domain;

import cn.longchou.wholesale.domain.HomePage.Cars;

public class CarsInfo {

	//活动类型
	public String carAction;
	//车辆的时间等信息
	public String carDesc;
	//车辆的id
	public Integer carID;
	//车辆图片的地址
	public String carImgURL;
	//车辆功能的描述
	public String carName;
	//车辆的价格
	public String carPrice;
	//车辆定金
	public float carSubscription;
	//车辆类型
	public String carType;
	//是否在购物车中
	public boolean inCart;
	//是否关注
	public boolean isFollow;
	//是否被选中
	public boolean isSelect;
	//金融方案
	public String financePlan;
	//副标题
	public String subTitle;
	
	//创建车辆信息的类
	public static CarsInfo clone(Cars info) {
		CarsInfo carInfo=new CarsInfo();
		carInfo.carPrice=info.carPrice;
		carInfo.carAction=info.carAction;
		carInfo.carDesc=info.carDesc;
		carInfo.carID=info.carID;
		carInfo.carImgURL=info.carImgURL;
		carInfo.carName=info.carName;
		carInfo.carSubscription=info.carSubscription;
		carInfo.carType=info.carType;
		carInfo.inCart=info.inCart;
		carInfo.isFollow=info.isFollow;
		carInfo.isSelect=false;
		carInfo.subTitle=info.subTitle;
		carInfo.financePlan="A 批售融资方案按天计息即时到账";
		return carInfo;
	}
	//设置选中状态为true
	public static CarsInfo setSelectTrue(CarsInfo info)
	{
		CarsInfo carInfo=new CarsInfo();
		carInfo.carPrice=info.carPrice;
		carInfo.carAction=info.carAction;
		carInfo.carDesc=info.carDesc;
		carInfo.carID=info.carID;
		carInfo.carImgURL=info.carImgURL;
		carInfo.carName=info.carName;
		carInfo.carSubscription=info.carSubscription;
		carInfo.carType=info.carType;
		carInfo.inCart=info.inCart;
		carInfo.isFollow=info.isFollow;
		carInfo.isSelect=true;
		carInfo.financePlan=info.financePlan;
		carInfo.subTitle=info.subTitle;
		return carInfo;
	}
	
	//设置金融的选中方案
	public static CarsInfo setPlanceSelect(CarsInfo info,String plan)
	{
		CarsInfo carInfo=new CarsInfo();
		carInfo.carPrice=info.carPrice;
		carInfo.carAction=info.carAction;
		carInfo.carDesc=info.carDesc;
		carInfo.carID=info.carID;
		carInfo.carImgURL=info.carImgURL;
		carInfo.carName=info.carName;
		carInfo.carSubscription=info.carSubscription;
		carInfo.carType=info.carType;
		carInfo.inCart=info.inCart;
		carInfo.isFollow=info.isFollow;
		carInfo.isSelect=info.isSelect;
		carInfo.financePlan=plan;
		carInfo.subTitle=info.subTitle;
		return carInfo;
	}
	
	//设置选中状态为false
	public static CarsInfo setSelectFalse(CarsInfo info)
	{
		CarsInfo carInfo=new CarsInfo();
		carInfo.carPrice=info.carPrice;
		carInfo.carAction=info.carAction;
		carInfo.carDesc=info.carDesc;
		carInfo.carID=info.carID;
		carInfo.carImgURL=info.carImgURL;
		carInfo.carName=info.carName;
		carInfo.carSubscription=info.carSubscription;
		carInfo.carType=info.carType;
		carInfo.inCart=info.inCart;
		carInfo.isFollow=info.isFollow;
		carInfo.isSelect=false;
		carInfo.financePlan=info.financePlan;
		carInfo.subTitle=info.subTitle;
		return carInfo;
	}
	
}









