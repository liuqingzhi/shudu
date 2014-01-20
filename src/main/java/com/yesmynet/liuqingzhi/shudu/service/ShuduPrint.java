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
	/**
	 * 打印
	 * @param datas
	 */
	public void print(Node<Shudu> datas)
	{
		String printInternal = printInternal(datas);
		printInternal=printJson(datas);
		System.out.println(printInternal);
	}
	private String printJson(Node<Shudu> datas)
	{
		Gson gson=new GsonBuilder().setPrettyPrinting().serializeNulls().create();
		String json = gson.toJson(datas);
		return json;
	}
	/**
	 * 打印出数独的推导过程
	 * @param datas
	 * @return
	 */
	private String printInternal(Node<Shudu> datas)
	{
		StringBuilder sb=new StringBuilder();
		datas.setPosition(new Position(0,0));
		settingNodePosition(datas);
		Map<Integer, List<Node<Shudu>>> levelNodeMap = getLevelNodeMap(datas);
		List<Node<Shudu>> deepestLevelNodes = getDeepestLevelNodes(levelNodeMap);
		
		
		return sb.toString();
	}
	/**
	 * 得到树的最深一级的所有节点
	 * @param levelNodeMap 以层级为key,该层级的所有节点为value的Map
	 * @return
	 */
	private List<Node<Shudu>> getDeepestLevelNodes(Map<Integer, List<Node<Shudu>>> levelNodeMap)
	{
		Integer maxLevel=0;
		for(Map.Entry<Integer,List<Node<Shudu>>> entry:levelNodeMap.entrySet())
		{
			Integer key = entry.getKey();
			if(key>maxLevel)
				maxLevel=key;
		}
		return levelNodeMap.get(maxLevel);
	}
	/**
	 * 设置节点的位置，方便打印。
	 * 在这里只是表示一个节点是第几行第几列（树的根结点是第0行，第0列）。
	 * @param datas
	 */
	private void settingNodePosition(Node<Shudu> datas)
	{
		List<Node<Shudu>> children = datas.getChildren();
		if(children!=null && !children.isEmpty())
		{
			int i=0;
			for(Node<Shudu> child:datas.getChildren())
			{
				Position position = datas.getPosition();
				child.setPosition(new Position(position.getX()+1,i));
				settingNodePosition(child);
				i++;
			}
		}
		
	}
	/**
	 * 得到树的每一级深度和该深度对应的所有节点。
	 * @param datas
	 * @return
	 */
	private Map<Integer,List<Node<Shudu>>> getLevelNodeMap(Node<Shudu> datas)
	{
		Map<Integer,List<Node<Shudu>>> re=new HashMap<Integer,List<Node<Shudu>>>();
		addNodeToMap(re,datas.getPosition().getX(),datas);
		List<Node<Shudu>> children = datas.getChildren();
		if(CollectionUtils.isNotEmpty(children))
		{
			for(Node<Shudu> child:children)
			{
				Map<Integer, List<Node<Shudu>>> childLevelNodeMap = getLevelNodeMap(child);
				addNodeToMap(re,childLevelNodeMap);
			}
		}
		
		return re;
	}
	private void addNodeToMap(Map<Integer,List<Node<Shudu>>> map,Integer level,Node<Shudu> node)
	{
		List<Node<Shudu>> list = map.get(level);
		if(list==null)
		{
			list=new ArrayList<Node<Shudu>>();
			map.put(level, list);
		}
		list.add(node);
	}
	private void addNodeToMap(Map<Integer,List<Node<Shudu>>> mapMain,Map<Integer,List<Node<Shudu>>> mapIncrease)
	{
		for(Map.Entry<Integer,List<Node<Shudu>>> entry:mapIncrease.entrySet())
		{
			Integer key = entry.getKey();
			List<Node<Shudu>> list = mapMain.get(key);
			if(list==null)
			{
				list=new ArrayList<Node<Shudu>>();
				mapMain.put(key, list);
			}
			
			list.addAll(entry.getValue());
		}
	}
}
