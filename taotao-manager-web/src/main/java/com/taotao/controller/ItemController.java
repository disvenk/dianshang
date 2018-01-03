package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.pojo.EasyUIDataGridResult;
import com.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class ItemController {
		
	@Autowired
	private ItemService itemService;
	
	//查询所有商品的分页数据
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) throws Exception{
		EasyUIDataGridResult result = itemService.getItemList(page, rows); 
		return result;
	}
	
	//添加保存商品
	@RequestMapping("/item/save")
	@ResponseBody
	public TaotaoResult saveItem(TbItem tbItem,String desc){
		TaotaoResult result = itemService.addItem(tbItem, desc);
		return result;
	}
}
