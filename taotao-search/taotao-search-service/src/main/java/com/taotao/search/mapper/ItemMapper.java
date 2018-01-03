package com.taotao.search.mapper;

import java.util.List;

import com.common.pojo.SearchItem;
import com.taotao.pojo.TbItem;


public interface ItemMapper {

	List<SearchItem> getItemList();
	SearchItem geTbItemById(long itemId);
}
