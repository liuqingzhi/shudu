package com.yesmynet.liuqingzhi.shudu.dto;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.yesmynet.liuqingzhi.shudu.dto.Shudu.DigitPosition;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.GroupDigitType;

public class ShuduTest {

	@Test
	public void testGetGroupDigit() {
		Integer[][] datas=new Integer[][]{
				{9,8,5,7,6,2,1,3,4},
				{2,6,7,1,3,4,5,8,9},
				{3,1,4,8,9,5,6,7,2},
				{8,3,2,9,7,6,4,5,1},
				{1,7,6,4,5,3,2,9,8},
				{5,4,9,2,1,8,6,7,3},
				{6,2,1,3,8,7,9,4,5},
				{4,5,3,6,2,9,8,1,7},
				{7,9,8,5,4,1,3,2,6}
				};
		Shudu data=new Shudu(datas);
		
		String groupDigits=null;
		GroupDigitType type=GroupDigitType.Row;
		int groupIndex=1;
		
		type=GroupDigitType.Row;
		groupIndex=0;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=1;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=2;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=3;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=4;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=5;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=6;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=7;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Row;
		groupIndex=8;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);

		
		type=GroupDigitType.Column;
		groupIndex=0;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=1;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=2;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=3;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=4;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=5;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=6;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=7;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Column;
		groupIndex=8;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		

		type=GroupDigitType.Grid;
		groupIndex=0;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=1;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=2;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=3;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=4;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=5;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=6;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=7;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
		type=GroupDigitType.Grid;
		groupIndex=8;
		groupDigits=getGroupDigitString(data.getGroupDigit(type, groupIndex));
		System.out.printf("groupDigits=%s,index=%s,type=%s\n",groupDigits,groupIndex,type);
		
	}
	private String getGroupDigitString(List<DigitPosition> dp)
	{
		StringBuilder sb=new StringBuilder();
		for(DigitPosition db1:dp)
		{
			sb.append(db1.getDigit()).append(",");
		}
		sb.append("位置：");
		for(DigitPosition db1:dp)
		{
			sb.append(db1.getPosition().toString()).append(",");
		}
		return sb.toString();
	}

}
