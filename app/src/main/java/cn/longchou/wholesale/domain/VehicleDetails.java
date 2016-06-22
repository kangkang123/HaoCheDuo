package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.longchou.wholesale.bean.CityProvinces;
import cn.longchou.wholesale.manage.CarsManager;

/**
 * 
* @Description: 车辆详情
*
* @author kangkang
*
* @date 2016年2月2日 下午2:17:46 
*
 */
public class VehicleDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//办证材料的内容
	public static List<MustKnow> mMustKnow=new ArrayList<VehicleDetails.MustKnow>();

	//办证须知中的其他
	public static String mOthers=null;
	
	//办证须知中的材料
	public static String mMaterial=null;
	
	//车辆图片
	public List<String> carImags;
	
	//上牌时间行驶里程等
	public  CarPreview carPreview;
	
	//证件及手续报告
	public Certifications certifications;
	
	//检查报告
	public CheckReports checkReports;
	
	public Map<String,String> mustKnows;
	
	public String originalPrice;
	
	public static synchronized VehicleDetails getInstance(){
		if (instance == null) {
			instance = new VehicleDetails();
		}
		return instance;
	}
	
	//遍历map集合把集合放到list集合中
	public List<MustKnow> getMustKnows(Map<String,String> mustKnows) {
		Map<String,String> map=mustKnows;
		Iterator<String> it=map.keySet().iterator();
		while (it.hasNext()) {
			
			String key = it.next();
			if("其他".equals(key))
			{
				mOthers=map.get(key);
			}else if("办证材料".equals(key))
			{
				mMaterial=map.get(key);
			}else{
				String value = map.get(key);
				MustKnow mustKnow = new MustKnow(key,value);
				if(!(mMustKnow.contains(mustKnow)))
				{
					mMustKnow.add(mustKnow);
				}
			}
		}
		return mMustKnow;
	}
	
	
	//办证须知列表的内容
	public List<MustKnow> getMust()
	{
		return mMustKnow;
	}
	
	public class MustKnow{
		
		public String key;
		public String value;
		
		public MustKnow(String key, String value) {
			this.key=key;
			this.value=value;
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof MustKnow))
			{
				throw new ClassCastException("类型不匹配");
			}
			MustKnow s=(MustKnow) o;
			return this.key.equals(s.key);
		}
	}
	
	//基本参数
	public Primarys primarys;

	private static VehicleDetails instance;
	
	//基本参数
	public class Primarys{
		public String 厂商;
		public String 发动机;
		public String 级别;
		public String 车身结构;
	}
	
	//检查报告
	public class CheckReports{
		public float 内饰;
		public float 外观;
		public boolean 机械及电器损伤;
	}
	
	//上牌时间行驶里程等
	public class CarPreview{
		public String 上牌时间;
		public String 排放标准;
		public String 行驶里程;
		public String 车辆所在地;
	}
	
	//证件及手续报告
	public class Certifications{
		public String VIN码;
		public String 交强险;
		public String 使用性质;
		public String 内饰颜色;
		public String 出厂日期;
		public String 外观颜色;
		public String 年审有效期;
		public String 排放标准;
		public String 登记日期;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
