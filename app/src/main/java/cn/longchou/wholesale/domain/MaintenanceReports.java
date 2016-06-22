package cn.longchou.wholesale.domain;

import java.util.List;

/**
 * Created by kangkang on 2016/6/16.
 * 车辆的维保记录
 */
public class MaintenanceReports {

    public List<MaintenanceReport> maintenanceReports;

    public class MaintenanceReport{
        //维保记录的图片
        public String imgUrl;
    }
}
