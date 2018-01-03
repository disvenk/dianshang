package com.taotao.service;

import com.common.pojo.EasyUIDataGridResult;
import com.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;

public interface ItemService {
	public  EasyUIDataGridResult getItemList(int page, int rows) throws Exception;
	TaotaoResult addItem(TbItem tbItem,String desc);
	TbItem getItemById(long itemId);
	TbItemDesc getItemDesc(long itemId);
}
