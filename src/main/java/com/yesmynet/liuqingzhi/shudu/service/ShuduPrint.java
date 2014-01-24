package com.yesmynet.liuqingzhi.shudu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yesmynet.liuqingzhi.shudu.dto.Node;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.Position;

/**
 * 打印数独的推导过程的Service
 * @author 刘庆志
 *
 */
public class ShuduPrint {
	private int childCount=1; 
	/**
	 * 打印
	 * @param datas
	 */
	public void print(Node<Shudu> datas)
	{
		String printInternal = "";
		//printInternal=printInternal(datas);
		//printInternal=printJson(datas);
		printInternal=printNodesTree(datas);
		System.out.println(printInternal);
	}
	/**
	 * 在html中以树的形式显示结点
	 * @param datas
	 * @return
	 */
	private String printNodesTree(Node<Shudu> datas)
	{
		StringBuilder sb=new StringBuilder();
		List<Node<Shudu>> children = datas.getChildren();
		boolean hasChild=CollectionUtils.isNotEmpty(children);
		int childCountI=childCount++;
		String shuduDataTable = getShuduDataTable(datas);
		sb.append("{\n");
		sb.append("'id': 'node").append(childCountI++).append("',\n");
		sb.append("'name': 'nodeName").append(childCountI).append("',\n");
		sb.append("'data': ").append(shuduDataTable);
		if(hasChild)
		{	
			sb.append(",");
		}
		sb.append("\n");
		if(hasChild)
		{	
			sb.append("'children': [\n");
			for(Node child:children)
			{
				sb.append(printNodesTree(child));
			}
			sb.append("]\n");
		}	
		sb.append("}");
		
		return sb.toString();
	}
	/**
	 * 打印出所有数字
	 * @param datas
	 * @return
	 */
	private String getShuduDataTable(Node<Shudu> datas)
	{
		StringBuilder sb=new StringBuilder();
		Shudu data = datas.getData();
		List<Position> tryedPosition = data.getTryedPosition();
		sb.append("{");
		sb.append("'shuduData':[\n");
		for(int i=0;i<data.getSideDigitNum();i++)
		{
			sb.append("\t\t[");
			for(int j=0;j<data.getSideDigitNum();j++)
			{
				Integer data2 = data.getData(i, j);
				Position xy=new Position(i,j);
				boolean newTry = isNewTry(xy,tryedPosition);
				if(null==data2)
					sb.append("X");
				else
					sb.append(data2);
				
				if(j<data.getSideDigitNum()-1)
					sb.append(",");
			}
			sb.append("]");
			if(i<data.getSideDigitNum()-1)
				sb.append(",");
			sb.append("\n");
		}
		sb.append("],\n");
		
		sb.append("'newTryed':[");
		if(CollectionUtils.isNotEmpty(tryedPosition))
		{
			int tryedSize=tryedPosition.size();
			for(int i=0;i<tryedSize;i++)
			{
				Position p=tryedPosition.get(i);
				sb.append("[").append(p.getX()).append(",").append(p.getY()).append("]");
				if(i<tryedSize-1)
				{
					sb.append(",");
				}
			}
		}
		sb.append("]\n");
		sb.append("}");
		return sb.toString();
	}
	private boolean isNewTry(Position position,List<Position> allPosition)
	{
		boolean re=false;
		if(position!=null && CollectionUtils.isNotEmpty(allPosition))
		{
			for(Position p:allPosition)
			{
				if(position.getX()==p.getX() && position.getY()==p.getY())
				{	
					re=true;
					break;
				}
					
			}
		}
		return re;
	}
	private String printJson(Node<Shudu> datas)
	{
		Gson gson=new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(datas);
		return json;
	}
}
