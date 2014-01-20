package com.yesmynet.liuqingzhi.shudu.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.yesmynet.liuqingzhi.shudu.dto.Node;
import com.yesmynet.liuqingzhi.shudu.dto.Shudu;

public class ShuduSolverTest {

	@Test
	public void testSolve() {
		Integer[][] datas=new Integer[][]{
				{9,null,5,7,6,2,1,null,4},
				{2,6,7,1,3,4,5,8,9},
				{3,1,4,8,9,5,6,7,2},
				{8,3,2,9,7,6,4,5,1},
				{1,7,6,4,5,3,2,9,8},
				{5,4,9,2,1,8,6,7,3},
				{6,2,null,3,8,7,null,4,null},
				{4,5,3,6,2,9,8,1,7},
				{7,9,8,5,4,1,3,2,6}
				};
		Shudu data=new Shudu(datas);
		Node<Shudu> initShudu=new Node<Shudu>();
		initShudu.setData(data);
		
		
		ShuduSolver shuduSolver=new ShuduSolver();
		shuduSolver.solve(initShudu);
		System.out.println(initShudu);
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
