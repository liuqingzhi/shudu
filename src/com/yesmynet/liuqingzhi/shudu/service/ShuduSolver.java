package com.yesmynet.liuqingzhi.shudu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yesmynet.liuqingzhi.shudu.dto.InfoDto;
import com.yesmynet.liuqingzhi.shudu.dto.Node;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.DigitPosition;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.GroupDigitType;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.Position;

public class ShuduSolver {

	public void solve(Node<Shudu> datas)
	{
		solveInternal(datas);
	}
	/**
	 * 打印出数独的推导过程
	 * @param datas
	 * @return
	 */
	protected String printAnswer(Node<Shudu> datas)
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
	 * 得到树的每一级深度和该深度对应的所有节点。
	 * @param datas
	 * @return
	 */
	private Map<Integer,List<Node<Shudu>>> getLevelNodeMap(Node<Shudu> datas)
	{
		Map<Integer,List<Node<Shudu>>> re=new HashMap<Integer,List<Node<Shudu>>>();
		addNodeToMap(re,datas.getPosition().getX(),datas);
		for(Node<Shudu> child:datas.getChildren())
		{
			Map<Integer, List<Node<Shudu>>> childLevelNodeMap = getLevelNodeMap(child);
			addNodeToMap(re,childLevelNodeMap);
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
	/**
	 * 设置节点的位置，方便打印。
	 * 在这里只是表示一个节点是第几行第几列（树的根结点是第0行，第0列）。
	 * @param datas
	 */
	private void settingNodePosition(Node<Shudu> datas)
	{
		//Map<Integer,Integer> levelNodeNumMap=new HashMap<Integer,Integer>();
		//datas.setPosition(new Position(0,0));
		int i=0;
		for(Node<Shudu> child:datas.getChildren())
		{
			Position position = datas.getPosition();
			child.setPosition(new Position(position.getX()+1,i));
			settingNodePosition(child);
			i++;
		}
	}
	/**
	 * 计算出数独的解答
	 * @param currentDatas
	 */
	private void solveInternal(Node<Shudu> currentDatas)
	{
		Shudu data = currentDatas.getData();
		if(data.isHasEmpty())
		{
			ShuduTry easiestTry = getEasiestTry(data);
			List<Integer> toTryDigits = getToTryDigits(easiestTry,data);
			if(toTryDigits!=null && !toTryDigits.isEmpty())
			{
				List<List<Integer>> combinations = combinations(toTryDigits);
				List<Node<Shudu>> generateChildren = generateChildren(combinations,easiestTry,currentDatas);
				currentDatas.setChildren(generateChildren);
				if(generateChildren!=null && !generateChildren.isEmpty())
				{
					for(Node<Shudu> childNode:generateChildren)
					{
						InfoDto validNewTry = validNewTry(childNode,easiestTry);
						if(validNewTry!=null && validNewTry.getSuccess()!=null && validNewTry.getSuccess()==false)
						{
							//这一次尝试违反了规则
							childNode.setResult(validNewTry);
						}
						else
						{
							solveInternal(childNode);
						}
					}
				}
			}
		}
		else
		{
			//所有数字都填充完了
		}
	}
	/**
	 * 验证一次尝试是否符合规则
	 * @param childNode 新产生的尝试，也就是要验证的数据
	 * @param easiestTry 根据哪一组数字产生的新尝试
	 * @return
	 */
	private InfoDto validNewTry(Node<Shudu> childNode,ShuduTry easiestTry)
	{
		InfoDto re=new InfoDto();
		Shudu data = childNode.getData();
		for (int i=0;i<data.getSideDigitNum();i++)
		{
			for(GroupDigitType type: GroupDigitType.values())
			{
				re=data.validGroupDigit(type, i);
				if(re!=null && re.getSuccess()!=null &&re.getSuccess()==false)
				{
					//有不符合规则的数据，直接退出
					return re;
				}
			}
			
		}
		return re;
	}
	/**
	 * 产生子节点，就是当前得到的所有尝试
	 * @param combinations 所有要排列当前缺少的数字的所有组合
	 * @param easiestTry 当前最容易的尝试
	 * @param currentDatas 当前尝试树
	 */
	private List<Node<Shudu>> generateChildren(List<List<Integer>> combinations,ShuduTry easiestTry,Node<Shudu> currentDatas)
	{
		List<Node<Shudu>> children=new ArrayList<Node<Shudu>>();
		List<Position> emptyDigitPosition = getEmptyDigitPosition(easiestTry.getSrc());
		for(List<Integer> dataArray:combinations)
		{
			Node<Shudu> child=new Node<Shudu>();
			Shudu cloneShudu = cloneShudu(currentDatas.getData());
			child.setData(cloneShudu);
			child.setParent(currentDatas);
			
			if(dataArray.size()!=emptyDigitPosition.size())
				throw new RuntimeException("排列组合产生的数字个数和要填充的数字的位置个数不一样多");
			for(int i=0;i<emptyDigitPosition.size();i++)
			{
				Position p = emptyDigitPosition.get(i);
				Integer integer = dataArray.get(i);
				
				cloneShudu.setData(p.getX(), p.getY(), integer);
			}
			children.add(child);
			
		}
		//currentDatas.setChildren(children);
		return children;
	}
	/**
	 * 得到所有空的数字的位置
	 * @param src
	 * @return
	 */
	private List<Position> getEmptyDigitPosition(List<DigitPosition> src)
	{
		List<Position> re=new ArrayList<Position>();
		for(DigitPosition dp:src)
		{
			if(dp.getDigit()==null)
				re.add(dp.getPosition());
		}
		return re;
	}
	/**
	 * 复制一个Shudu对象。
	 * @param src
	 * @return
	 */
	private Shudu cloneShudu(Shudu src)
	{
		Shudu re=new Shudu(src.getDatas());
		return re;
	}
	/**
	 * 列出一组数字的所有组合。
	 * 算法是，把这组数字的第1个数字拿出来，对剩余的数字求出所有组合，然后循环这些所有组合，对于每次循环，再做第2重循环，就是把第1个数字按顺序
	 * 插入到当前的这个组合中，这样，循环一次就又得到多个组合，这2重循环完成就得到了所有数字的组合。
	 * @param datas 表示所有的排列组合，List中每个元素又是一个List，后者就是其中一种排列组合。
	 * @return
	 */
	protected List<List<Integer>> combinations(List<Integer> datasParam)
	{
		if(datasParam==null || datasParam.isEmpty())
			throw new RuntimeException("要排列组合的数字不能是空的");
		
		List<Integer> datas=new ArrayList<Integer>(datasParam);
		if(datas.size()==1)
		{
			List<List<Integer>> dataArray=new ArrayList<List<Integer>>();
			Integer[] dataArray1=new Integer[]{datas.get(0)};
			dataArray.add(Arrays.asList(dataArray1));
			return dataArray;
		}
		else
		{
			Integer integer = datas.get(0);
			datas.remove(0);
			
			List<List<Integer>> combinations = combinations(datas);
			List<List<Integer>> newCombinations=new ArrayList<List<Integer>>();
			for(List<Integer> dataArray:combinations)
			{
				for(int i=0;i<=dataArray.size();i++)
				{
					List<Integer> newDataArray=new ArrayList<Integer>(dataArray);
					newDataArray.add(i, integer);
					newCombinations.add(newDataArray);
				}
				
			}
			return newCombinations;
		}
	}
	/**
	 * 计算阶乘
	 * @param num
	 * @return
	 */
	private Integer factorial(int num)
	{
		if(num<=0 )
			throw new RuntimeException("阶乘的起始值不能小于等于0");
		if(num==1)
		{
			return 1;
		}
		else
		{
			return num * factorial(num-1);
		}
	}
	
	/**
	 * 得到要尝试的数字，也就是原数据中缺失的所有数字
	 * @param shuduTry
	 * @param data
	 * @return
	 */
	private List<Integer> getToTryDigits(ShuduTry shuduTry,Shudu data)
	{
		List<Integer> re=new ArrayList<Integer>();
		List<DigitPosition> src = shuduTry.getSrc();
		for(int ii=1;ii<10;ii++)
		{
			boolean findTheDigit=false;
			for(DigitPosition dp:src)
			{
				if(dp.getDigit()!=null && ii==dp.getDigit())
				{
					findTheDigit=true;
					break;
				}
			}
			if(!findTheDigit)
				re.add(ii);
				
		}
		return re;
	}
	/**
	 * 得到最容易尝试的一组数字
	 * @param data
	 * @return
	 */
	private ShuduTry getEasiestTry(Shudu data)
	{
		ShuduTry re=null;
		List<ShuduTry> hasEmptyGroups = new ArrayList<ShuduTry>(); 
		hasEmptyGroups.addAll(getHasEmptyGroups(data,GroupDigitType.Row));
		hasEmptyGroups.addAll(getHasEmptyGroups(data,GroupDigitType.Column));
		hasEmptyGroups.addAll(getHasEmptyGroups(data,GroupDigitType.Grid));
		if(hasEmptyGroups!=null && !hasEmptyGroups.isEmpty())
		{
			Collections.sort(hasEmptyGroups, new Comparator<ShuduTry>(){
				@Override
				public int compare(ShuduTry o1, ShuduTry o2) {
					return o1.getEmptyNum()-o2.getEmptyNum();
				}});
			re=hasEmptyGroups.get(0);
		}
		
		return re;
	}
	private List<ShuduTry> getHasEmptyGroups(Shudu data,GroupDigitType groupDigitType)
	{
		List<ShuduTry> re=new ArrayList<ShuduTry>();
		int sideDigitNum = data.getSideDigitNum();
		for(int i=0;i<sideDigitNum;i++)
		{
			List<DigitPosition> groupDigit = data.getGroupDigit(groupDigitType, i);
			int emptyNum = emptyNum(groupDigit);
			if(emptyNum>0)
			{
				ShuduTry s=new ShuduTry();
				s.setSrc(groupDigit);
				s.setSrcType(groupDigitType);
				s.setIndex(i);
				s.setEmptyNum(emptyNum);
				
				re.add(s);
			}
		}
		return re;
	}
	private int emptyNum(List<DigitPosition> groupDigit)
	{
		int re=0;
		for(DigitPosition dp:groupDigit)
		{
			if(dp.getDigit()==null)
			{
				re++;
			}
		}
		return re;
	}
	private class ShuduTry
	{
		private List<DigitPosition> src;
		private int emptyNum;
		private GroupDigitType srcType;
		private int index;
		public List<DigitPosition> getSrc() {
			return src;
		}
		public void setSrc(List<DigitPosition> src) {
			this.src = src;
		}
		public int getEmptyNum() {
			return emptyNum;
		}
		public void setEmptyNum(int emptyNum) {
			this.emptyNum = emptyNum;
		}
		public GroupDigitType getSrcType() {
			return srcType;
		}
		public void setSrcType(GroupDigitType srcType) {
			this.srcType = srcType;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
	}
}
