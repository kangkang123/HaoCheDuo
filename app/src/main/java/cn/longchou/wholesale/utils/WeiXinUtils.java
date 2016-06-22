package cn.longchou.wholesale.utils;

import java.io.StringWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import cn.longchou.wholesale.domain.PayInfo;
import cn.longchou.wholesale.global.Constant;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Xml;

public class WeiXinUtils {

	
	public static boolean checkEnable(Context paramContext) {  
        boolean i = false;  
        ConnectivityManager com=(ConnectivityManager)paramContext.getSystemService("connectivity");
        NetworkInfo localNetworkInfo = com.getActiveNetworkInfo();
        if ((localNetworkInfo != null) && (localNetworkInfo.isAvailable()))  
            return true;  
        return false;  
    }  
  
    /** 
     * 将ip的整数形式转换成ip形式 
     *  
     * @param ipInt 
     * @return 
     */  
    public static String int2ip(int ipInt) {  
        StringBuilder sb = new StringBuilder();  
        sb.append(ipInt & 0xFF).append(".");  
        sb.append((ipInt >> 8) & 0xFF).append(".");  
        sb.append((ipInt >> 16) & 0xFF).append(".");  
        sb.append((ipInt >> 24) & 0xFF);  
        return sb.toString();  
    }  
  
    /** 
     * 获取当前ip地址 
     *  
     * @param context 
     * @return 
     */  
    public static String getLocalIpAddress(Context context) {  
        try {  
            
            WifiManager wifiManager = (WifiManager) context  
                    .getSystemService(Context.WIFI_SERVICE);  
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
            int i = wifiInfo.getIpAddress();  
            return int2ip(i);  
        } catch (Exception ex) {  
            return " 获取IP出错鸟!!!!请保证是WIFI,或者请重新打开网络!\n" + ex.getMessage();  
        }  
        // return null;  
    }  
	
	
	
	public static String getSign(String orderNumber,int totalMoney, String nonce_str, String notify_url) throws Exception {  
		String signTemp = "appid="+Constant.APP_ID +
				"&body="+"test"   +
				"&mch_id="+Constant.mch_id   +
				"&nonce_str="+nonce_str  +
				"&notify_url="+notify_url   +
				"&out_trade_no="+orderNumber   +
				"&spbill_create_ip="+"14.23.150.211"   +
				"&total_fee="+totalMoney   +
				"&trade_type="+"APP"   +
				"&key="+Constant.key; 
				String sign = MD5.getMessageDigest(signTemp.getBytes()).toUpperCase();
				System.out.println("sign:"+sign);
				return sign; 
		}
	
	public static String getResultSign(String nonce_str,String prepayid, String time)
	{
		String signTemp = "appid="+Constant.APP_ID +
				"&noncestr="+nonce_str  +
				"&package="+"Sign=WXPay"   +
				"&partnerid="+Constant.mch_id   +
				"&prepayid="+prepayid   +
				"&timestamp="+time   +
				"&key="+Constant.key; 
				String sign = MD5.getMessageDigest(signTemp.getBytes()).toUpperCase();
				System.out.println("sign:"+sign);
		return sign;
	}
	
}
