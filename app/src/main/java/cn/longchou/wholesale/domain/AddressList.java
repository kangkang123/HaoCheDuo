package cn.longchou.wholesale.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kangkang on 2016/6/29.
 * 地址管理的地址列表
 */
public class AddressList implements Serializable{

    /**
     * createTime : 1466749840000
     * id : 5
     * userId : 221
     */

    public List<UserAddressBean> userAddress;


    public static class UserAddressBean implements Serializable{
        public String createTime;
        public String id;
        public String userId;
        public String address;
        public String city;
        public String county;
        public String phone;
        public String postCode;
        public String province;
        public String userName;
    }
}
