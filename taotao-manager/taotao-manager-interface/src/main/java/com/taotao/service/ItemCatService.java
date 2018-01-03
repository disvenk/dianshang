package com.taotao.service;

import java.util.List;

import com.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	//根据父节点的id查询字节点的列表
	public List<EasyUITreeNode> getCatList(long parentId) throws Exception;
}
