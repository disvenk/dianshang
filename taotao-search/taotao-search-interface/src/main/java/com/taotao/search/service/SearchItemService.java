package com.taotao.search.service;

import com.common.pojo.TaotaoResult;

public interface SearchItemService {
	public TaotaoResult importAllItemToIndex() throws Exception;

	public TaotaoResult addDocument(long itemId) throws Exception;
}
