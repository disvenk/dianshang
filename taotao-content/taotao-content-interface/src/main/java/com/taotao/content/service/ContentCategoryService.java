package com.taotao.content.service;

import java.util.List;

import com.common.pojo.EasyUITreeNode;
import com.common.pojo.TaotaoResult;

public interface ContentCategoryService {
	public List<EasyUITreeNode> getContentCategoryList(long parentId);
	public TaotaoResult addContentCategory(long parentId, String name);
}
