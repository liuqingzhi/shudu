package com.yesmynet.liuqingzhi.shudu.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yesmynet.liuqingzhi.shudu.dto.InfoDto;
import com.yesmynet.liuqingzhi.shudu.dto.Node;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.DigitPosition;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.GroupDigitType;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu.Position;

public class ShuduSolver {
	private Logger logger =LoggerFactory.getLogger(getClass());
	private Gson gson=new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	private ShuduPrint shuduPrint=new ShuduPrint();
	/**
	 * 用于生成ID的计数器
	 */
	private int idCounter=1;
	private Node<Shudu> datasToPrint=null;
	public void solve(Node<Shudu> datas)
	{
		datasToPrint=datas;
		if(StringUtils.isBlank(datas.getId()))
		{
			datas.setId(getNextId());
		}
		solveInternal(datas);
		logger.debug("最后遍历了所有路径后的结果={}",shuduPrint.print(datas));
	}
	/**
	 * 计算出数独的解答
	 * @param currentDatas
	 */
	private void solveInternal(Node<Shudu> currentDatas)
	{
		Shudu data = currentDatas.getData();
		logger.debug("尝试的次数={},currentDatas.id={},print={}",idCounter,currentDatas.getId());//shuduPrint.print(datasToPrint));
		if(data.isHasEmpty())
		{
			List<ShuduTry> easiestTry2 = getEasiestTry(data);
			for(ShuduTry easiestTry:easiestTry2)
			{
				List<Integer> toTryDigits = getToTryDigits(easiestTry,data);
				if(CollectionUtils.isNotEmpty(toTryDigits))
				{
					List<List<Integer>> combinations = combinations(toTryDigits);
					List<Node<Shudu>> generateChildren = generateChildren(combinations,easiestTry,currentDatas);
					if(currentDatas.getChildren() == null )
					{
						currentDatas.setChildren(new ArrayList<Node<Shudu>>());
					}
					currentDatas.getChildren().addAll(generateChildren);
					
					if(CollectionUtils.isNotEmpty(generateChildren))
					{
						for(Node<Shudu> childNode:generateChildren)
						{
							InfoDto validNewTry = validNewTry(childNode,easiestTry);
							childNode.setResult(validNewTry);
							if(validNewTry.getSuccess()==null)
							{
								//这一次尝试还有空的，并且，到目前为止没有违反规则
								solveInternal(childNode);
							}
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
	 * 得到一个节点的ID
	 * @return
	 */
	private String getNextId()
	{
		String re="";
		re+=""+idCounter++;
		
		return re;
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
		List<InfoDto> allResult=new ArrayList<InfoDto>();
		for (int i=0;i<data.getSideDigitNum();i++)
		{
			for(GroupDigitType type: GroupDigitType.values())
			{
				InfoDto validGroupDigitTry = validGroupDigitTry(type, i,childNode.getData());
				allResult.add(validGroupDigitTry);
				if(validGroupDigitTry!=null && validGroupDigitTry.getSuccess()!=null &&validGroupDigitTry.getSuccess()==false)
				{
					//有不符合规则的数据，直接退出
					return validGroupDigitTry;
				}
			}
			
		}
		boolean hasEmpty=false;
		for(InfoDto infoDto:allResult)
		{
			if(infoDto.getSuccess()==null)
			{
				hasEmpty=true;
				break;
			}
		}
		if(!hasEmpty)
		{
			re.setSuccess(true);
			re.setMsg("正确");
		}
		return re;
	}
	/**
	 * 验证一组数字是否符合规则
	 * @param groupDigitType
	 * @param i
	 * @return 返回的InfoDto对象中的success属性表示这一组数字是否符合规：null表示有空数字，true表示都填完了并且符合规则，
	 * false表示都填完了但是违反了规则
	 */
	private InfoDto validGroupDigitTry(GroupDigitType groupDigitType,int i,Shudu shudu)
	{
		InfoDto re=new InfoDto();
		
		List<DigitPosition> groupDigit = shudu.getGroupDigit(groupDigitType,i);
		int sideDigitNum = shudu.getSideDigitNum();
		if(groupDigit.size()!=sideDigitNum)
		{
			throw new RuntimeException("得到的一组数字不是规定的个数，期望是"+ sideDigitNum +"个数字，但是实际是"+ groupDigit.size() +"个");
		}
		/*for(DigitPosition dp:groupDigit)
		{
			if(dp.getDigit()==null)
				return null;
		}*/
		for(int ii=1;ii<10;ii++)
		{
			List<Position> digIndex=new ArrayList<Position>(); 
			for(DigitPosition dp:groupDigit)
			{
				if(dp.getDigit()!=null && dp.getDigit().equals(ii))
				{
					digIndex.add(dp.getPosition());
				}
			}
			if(digIndex.size()>1)
			{
				//这是不符合规则了
				re.setSuccess(false);
				Position[] array = digIndex.toArray(new Position[0]);
				re.setMsg(String.format("数字%s出现了%s次，分别是%s",ii,digIndex.size(),Arrays.toString(array)));
			}
		}
		
		if(re.getSuccess()==null)
		{
			//说明到目前为止没有违反规则
			boolean hasEmpty=false;
			for(DigitPosition dp:groupDigit)
			{
				if(dp.getDigit()==null)
				{
					hasEmpty=true;
					break;
				}
			}
			
			if(!hasEmpty)
			{
				//所有数字都填完了，并且没有重复数字，说明这一组数字是对的。
				re.setSuccess(true);
				re.setMsg("正确");
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
			child.setId(getNextId());
			child.setData(cloneShudu);
			child.setParent(currentDatas);
			
			
			if(dataArray.size()!=emptyDigitPosition.size())
				throw new RuntimeException("排列组合产生的数字个数("+ dataArray.size() +")和要填充的数字的位置个数("+ emptyDigitPosition.size() +")不一样多");
			for(int i=0;i<emptyDigitPosition.size();i++)
			{
				Position p = emptyDigitPosition.get(i);
				Integer integer = dataArray.get(i);
				
				cloneShudu.setData(p.getX(), p.getY(), integer);
			}
			cloneShudu.setTryedPosition(emptyDigitPosition);
			
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
	private List<ShuduTry> getEasiestTry(Shudu data)
	{
		List<ShuduTry> re=new ArrayList<ShuduTry>();
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
			Integer emptyNum=hasEmptyGroups.get(0).getEmptyNum();
			for(ShuduTry shuduTry:hasEmptyGroups)
			{
				if(emptyNum>=shuduTry.getEmptyNum())
				{
					re.add(shuduTry);		
				}
				else
				{
					break;
				}
			}
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
