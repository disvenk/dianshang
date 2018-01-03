package com.taotao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.pojo.EasyUITreeNode;
import com.taotao.service.ItemCatService;

@Controller
public class ItemCatController {
	
		@Autowired
		private ItemCatService itemCatService;
		
		//根据商品的parentid查询商品的类目
		@RequestMapping("/item/cat/list")
		@ResponseBody
		public List<EasyUITreeNode> getItemCatList(@RequestParam(value="id", defaultValue="0")Long parentId) throws Exception {
			
			List<EasyUITreeNode> list = itemCatService.getCatList(parentId);
			return list;
		}
	
}
