package cn.longchou.wholesale.bean;

import java.io.Serializable;

public class CityProvinces implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 保存所有的省和市
	public String provinces;
	// 用于标识是不是省
	public boolean isProvinces;
    // 城市定位是否选中
	public boolean isSelect;
	// 求购是否选中
	public boolean isBuy;

	public CityProvinces() {

	}
	
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof CityProvinces))
		{
			throw new ClassCastException("类型不匹配");
		}
		CityProvinces s=(CityProvinces) o;
		return this.provinces.equals(s.provinces);
	}
	
	

	//初始化把获取的数据封装到对象中
	public CityProvinces(String provinces, boolean isProvinces) {
		this.provinces = provinces;
		this.isProvinces = isProvinces;
		this.isSelect=false;
		this.isBuy=false;
	}

	private static CityProvinces instance;

	// 获取CarsManager对象
	public static synchronized CityProvinces getInstance() {
		if (instance == null) {
			instance = new CityProvinces();
		}
		return instance;
	}

	//获取CityProvinces对象，改变其中的选中的状态为true，然后返回
	public CityProvinces getCityProvincesTrue(CityProvinces info) {
		CityProvinces cityinfo = new CityProvinces();
		cityinfo.isProvinces = info.isProvinces;
		cityinfo.provinces = info.provinces;
		cityinfo.isSelect = true;
		this.isBuy=info.isBuy;
		return cityinfo;

	}
	
	//获取CityProvinces对象，改变其中的选中的状态为false，然后返回
	public CityProvinces getCityProvincesFalse(CityProvinces info) {
		CityProvinces cityinfo = new CityProvinces();
		cityinfo.isProvinces = info.isProvinces;
		cityinfo.provinces = info.provinces;
		cityinfo.isSelect = false;
		this.isBuy=info.isBuy;
		return cityinfo;
	}
	
	//获取CityProvinces对象，改变其中的选中的状态为true，然后返回
	public CityProvinces getCityBuyTrue(CityProvinces info) {
		CityProvinces cityinfo = new CityProvinces();
		cityinfo.isProvinces = info.isProvinces;
		cityinfo.provinces = info.provinces;
		cityinfo.isSelect = info.isSelect;
		this.isBuy=true;
		return cityinfo;
		
	}
	
	//获取CityProvinces对象，改变其中的选中的状态为false，然后返回
	public CityProvinces getCityBuyFalse(CityProvinces info) {
		CityProvinces cityinfo = new CityProvinces();
		cityinfo.isProvinces = info.isProvinces;
		cityinfo.provinces = info.provinces;
		cityinfo.isSelect = info.isSelect;
		this.isBuy=false;
		return cityinfo;
	}

}
