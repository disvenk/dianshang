package com.taotao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.pojo.TaotaoResult;
import com.taotao.search.service.SearchItemService;

@Controller
public class IndexManagerController {

	@Autowired
	private SearchItemService searchItemService;
	
	//导入商品索引
	@RequestMapping("/index/import")
	@ResponseBody
	public TaotaoResult importAllItems() {
		try {
			TaotaoResult result = searchItemService.importAllItemToIndex();
			System.out.println("导入索引");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, "导入数据失败");
		}
	}
}
