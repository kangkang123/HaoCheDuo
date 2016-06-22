package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.List;

public class LicensePlate {

	public String position;
	public String present;
	
	public static List<LicensePlate> getLicensePlate()
	{
		List<LicensePlate> list=new ArrayList<LicensePlate>();
		for(int i=0;i<3;i++)
		{
			if(i==0)
			{
				LicensePlate machine=new LicensePlate();
				machine.position="市区";
				machine.present="一方到场";
				list.add(machine);
			}else if(i==1)
			{
				LicensePlate machine=new LicensePlate();
				machine.position="淮阴";
				machine.present="都没到场";
				list.add(machine);
			}else if(i==2)
			{
				LicensePlate machine=new LicensePlate();
				machine.position="金福县";
				machine.present="都到了";
				list.add(machine);
			}
		}
		return list;
	}
}
