package com.yesmynet.liuqingzhi.shudu.dto;

import java.util.List;

import com.yesmynet.liuqingzhi.shudu.dto.Shudu.Position;

public class Node<T> {
	private T data;
	private Node<T> parent;
	private List<Node<T>> children;
	private InfoDto result;
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Node<T> getParent() {
		return parent;
	}
	public void setParent(Node<T> parent) {
		this.parent = parent;
	}
	public List<Node<T>> getChildren() {
		return children;
	}
	public void setChildren(List<Node<T>> children) {
		this.children = children;
	}
	public InfoDto getResult() {
		return result;
	}
	public void setResult(InfoDto result) {
		this.result = result;
	}
}
