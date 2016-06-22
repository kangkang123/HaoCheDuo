package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.List;

public class VehicleLicenseFee {

	//市区
	public String city;
	//过户交易费
	public String transfer;
	//转籍交易费
	public String membership;
	//公车费
	public String bus;
	//快至服务费
	public String fast;
	
	public static List<VehicleLicenseFee> getVehicleFee()
	{
		List<VehicleLicenseFee> list=new ArrayList<VehicleLicenseFee>();
		for(int i=0;i<3;i++)
		{
			if(i==0)
			{
				VehicleLicenseFee machine=new VehicleLicenseFee();
				machine.city="市区";
				machine.transfer="4323";
				machine.membership="24";
				machine.bus="356";
				machine.fast="23";
				list.add(machine);
			}else if(i==1)
			{
				VehicleLicenseFee machine=new VehicleLicenseFee();
				machine.city="淮阴区";
				machine.transfer="4323";
				machine.membership="24";
				machine.bus="35346";
				machine.fast="23";
				list.add(machine);
			}else if(i==2)
			{
				VehicleLicenseFee machine=new VehicleLicenseFee();
				machine.city="近乎去";
				machine.transfer="4323";
				machine.membership="24";
				machine.bus="36";
				machine.fast="23";
				list.add(machine);
			}
		}
		return list;
	}
}
