package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* @Description: 地址管理
*
* @author kangkang
*
* @date 2016年6月4日 下午4:50:35 
*
 */
public class AddressManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
	public String phone;
	public String city;
	public AddressManager(String name, String phone, String city) {
		super();
		this.name = name;
		this.phone = phone;
		this.city = city;
	}
	
	public static List<AddressManager> getAddressManager()
	{
		List<AddressManager> list=new ArrayList<AddressManager>();
		list.add(new AddressManager("你好", "123", "中国"));
		list.add(new AddressManager("你好", "1234", "中国"));
		list.add(new AddressManager("你好", "1235", "中国"));
		return list;
	}
}
