package cn.longchou.wholesale.domain;

import java.util.ArrayList;
import java.util.List;

public class MachineElectrical {

	public String position;
	public String hurt;
	
	public static List<MachineElectrical> getMachineElectricals()
	{
		List<MachineElectrical> list=new ArrayList<MachineElectrical>();
		for(int i=0;i<3;i++)
		{
			if(i==0)
			{
				MachineElectrical machine=new MachineElectrical();
				machine.position="窗口";
				machine.hurt="中毒";
				list.add(machine);
			}else if(i==1)
			{
				MachineElectrical machine=new MachineElectrical();
				machine.position="座子";
				machine.hurt="损伤";
				list.add(machine);
			}else if(i==2)
			{
				MachineElectrical machine=new MachineElectrical();
				machine.position="方向盘";
				machine.hurt="没有了";
				list.add(machine);
			}
		}
		return list;
	}
}
