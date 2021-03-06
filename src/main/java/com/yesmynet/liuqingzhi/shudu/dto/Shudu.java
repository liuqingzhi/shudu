package com.yesmynet.liuqingzhi.shudu.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Shudu {
	/**
	 * 表示一组数字
	 * @author liuqingzhi
	 *
	 */
	public enum GroupDigitType {
		/**
		 * 表示行
		 */
		Row,
		/**
		 * 表示列
		 */
		Column,
		/**
		 * 表示一个九宫格
		 */
		Grid;
	}
	/**
	 * 表示一个数字在整个数独中的坐标
	 * @author liuqingzhi
	 *
	 */
	public static class Position
	{
		/**
		 * 第几行，以0表示第1行
		 */
		private int x;
		/**
		 * 第几列，以0表示第1列
		 */
		private int y;
		public Position(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		@Override
		public String toString() {
			return "("+x+","+y+")";
		}
		@Override
		public boolean equals(Object obj) {
			if(obj!=null && obj instanceof Position)
			{
				return this.toString().endsWith(obj.toString());
			}
			return super.equals(obj);
		}
		
	}
	/**
	 * 表示数独中一个数字和它对应的位置
	 * @author liuqingzhi
	 *
	 */
	public static class DigitPosition
	{
		/**
		 * 数字
		 */
		private Integer digit;
		/**
		 * 位置
		 */
		private Position position;
		public Integer getDigit() {
			return digit;
		}
		public void setDigit(Integer digit) {
			this.digit = digit;
		}
		public Position getPosition() {
			return position;
		}
		public void setPosition(Position position) {
			this.position = position;
		}
		
	}
	private final Integer sideLength=3;
	private final Integer[][] datas;
	private final int sideDigitNum=9;
	/**
	 * 本次设设置了数字的坐标。
	 * 就是当按照上一步的条件推导出新的数字后，新填写的数字的坐标
	 */
	private List<Position> tryedPosition;
	public Shudu() {
		super();
		datas=new Integer[sideDigitNum][sideDigitNum];
	}
	public Shudu(Integer[][] datas)
	{
		this.datas=new Integer[sideDigitNum][sideDigitNum];
		
		for(int i=0;i<sideDigitNum;i++)
		{
			for(int j=0;j<sideDigitNum;j++)
			{
				this.datas[i][j]=datas[i][j];
			}
		}
	}
	/**
	 * 设置数据
	 * @param x 第几行，0表示第1行。
	 * @param y 第几列，0表示第1列。
	 * @param data 要设置的数据
	 * @return
	 */
	public Shudu setData(int x,int y,Integer data)
	{
		if(x<sideDigitNum && y<sideDigitNum && (data==null || (data>=1 && data<=9)))
		{
			datas[x][y]=data;
		}
		return this;
	}
	/**
	 * 得到指定坐标的值
	 * @param x
	 * @param y
	 * @return
	 */
	public Integer getData(int x,int y)
	{
		if(x<sideDigitNum && y<sideDigitNum)
		{
			return datas[x][y];
		}
		else
		{	
			throw new RuntimeException("坐标值超出范围,x="+x+",y="+y);
		}
			
	}
	/**
	 * 是否有空的格子
	 * @return
	 */
	public boolean isHasEmpty()
	{
		boolean re=false;
		for(int i=0;i<sideDigitNum;i++)
		{
			for(int j=0;j<sideDigitNum;j++)
			{
				if(datas[i][j] ==null)
				{
					re=true;
					break;
				}
			}
		}
		return re;
	}
	/**
	 * 
	 * @param groupDigitType
	 * @param i 表示第几行、或第几列，或第几个九宫格，都是用0表示第1个。
	 * @return
	 */
	public List<DigitPosition> getGroupDigit(GroupDigitType groupDigitType,int i)
	{
		List<DigitPosition> re=null;
		switch(groupDigitType)
		{
		case Row:
			re=getRow(i);
			break;
		case Column:
			re=getColumn(i);
			break;
		case Grid:
			re=getGrid(i);
			break;
		}
		return re;
	}
	public Integer getSideLength() {
		return sideLength;
	}
	public Integer[][] getDatas() {
		return datas;
	}
	public int getSideDigitNum() {
		return sideDigitNum;
	}
	public List<Position> getTryedPosition() {
		return tryedPosition;
	}
	public void setTryedPosition(List<Position> tryedPosition) {
		this.tryedPosition = tryedPosition;
	}
	private List<DigitPosition> getRow(int y)
	{
		List<DigitPosition> re=new ArrayList<DigitPosition>();
		Integer[] integers = datas[y];
		
		for(int i=0;i<integers.length;i++)
		{
			DigitPosition d=new DigitPosition();
			d.setDigit(integers[i]);
			d.setPosition(new Position(y,i));
			re.add(d);
		}
		
		return re;
	}
	private List<DigitPosition> getColumn(int y)
	{
		List<DigitPosition> re=new ArrayList<DigitPosition>();
		for(int i=0;i<sideDigitNum;i++)
		{
			for(int j=0;j<sideDigitNum;j++)
			{
				if(j==y)
				{
					DigitPosition d=new DigitPosition();
					d.setDigit(datas[i][j]);
					d.setPosition(new Position(i,j));
					re.add(d);
				}
			}
		}
		
		return re;
	}
	private List<DigitPosition> getGrid(int iGrid)
	{
		int topLeftRow=iGrid/3;
		int topLeftColumn=iGrid%3; 
		
		topLeftColumn=topLeftColumn*3;
		topLeftRow=topLeftRow*3;
		List<DigitPosition> re=new ArrayList<DigitPosition>();
		for(int i=0;i<3;i++)
		{
			for(int j=0;j<3;j++)
			{
				DigitPosition d=new DigitPosition();
				d.setDigit(datas[topLeftRow+i][topLeftColumn+j]);
				d.setPosition(new Position(topLeftRow+i,topLeftColumn+j));
				re.add(d);
			}
		}
		
		return re;
	}
	
}

