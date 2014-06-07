package com.yesmynet.liuqingzhi.shudu.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.yesmynet.liuqingzhi.shudu.dto.Node;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu;

public class ShuduSolverTest {

	@Test
	public void testSolve() {
		Integer[][] datas=new Integer[][]{
				{null,null,null,3,null,1,8,null,null},
				{null,6,9,5,null,null,1,null,null},
				{null,1,null,2,null,null,null,null,null},
				{null,3,null,8,2,null,7,null,5},
				{null,null,null,null,null,3,2,null,null},
				{4,2,null,6,null,null,null,8,null},
				{null,9,6,null,null,5,4,null,8},
				{3,4,7,null,null,null,6,5,null},
				{5,null,null,null,null,9,null,7,null}
				};
		Shudu data=new Shudu(datas);
		Node<Shudu> initShudu=new Node<Shudu>();
		initShudu.setData(data);
		
		
		ShuduSolver shuduSolver=new ShuduSolver();
		ShuduPrint shuduPrint=new ShuduPrint();
		
		shuduSolver.solve(initShudu);
		shuduPrint.print(initShudu);
	}
	@Test
	public void testCombinations() {
		ShuduSolver shuduSolver=new ShuduSolver();
		List<List<Integer>> combinations=null;
		combinations = shuduSolver.combinations(Arrays.asList(1,2,3));
		printCombinations(combinations);
		
		
		combinations = shuduSolver.combinations(Arrays.asList(1,2,3,4));
		printCombinations(combinations);
		
	}
	@Test
	public void testCount1()
	{
		BigDecimal bigDecimal = new BigDecimal(81);
		BigDecimal factorial = factorial(bigDecimal);
		
		System.out.printf("阶乘结果=%s\n",factorial);
	}
	private BigDecimal factorial(BigDecimal num)
	{
		BigDecimal re=num;
		
		if(re.intValue()==1)
		{
			re = new BigDecimal(1);
		}
		else
		{
			re=re.multiply(factorial(num.subtract(new BigDecimal(1))));
		}
		
		return re;
	}
	private void printCombinations(List<List<Integer>> combinations)
	{
		int i=0;
		for(List<Integer> oneCombination:combinations)
		{
			Integer[] array = oneCombination.toArray(new Integer[0]);
			String string = Arrays.toString(array);
			System.out.printf("i=%s,data=%s\n",i,string);
			i++;
		}
	}

}
