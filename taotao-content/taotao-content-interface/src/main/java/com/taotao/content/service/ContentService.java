package com.taotao.content.service;

import java.util.List;

import com.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	public TaotaoResult addContent(TbContent content);
	public List<TbContent> getContentList(long cid);
}
