package com.common.pojo;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable{
	//查询的表时tb_item_cat
	private long id;//类目的id
	private String text;//类目的name，里面的名称就是text文本
	private String state;//对用status,1表示是父节点，0表示是子节点
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
