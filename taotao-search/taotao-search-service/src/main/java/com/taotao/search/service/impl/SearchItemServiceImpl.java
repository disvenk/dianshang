package com.taotao.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.pojo.SearchItem;
import com.common.pojo.TaotaoResult;
import com.taotao.search.mapper.ItemMapper;
import com.taotao.search.service.SearchItemService;
/**
 * 导入商品数据到索引库
 * <p>Title: SearchItemServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.cn</p> 
 * @version 1.0
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SolrServer solrServer;
	@Autowired
	private ItemMapper itemMapper;
	//导入商品索引
	@Override
	public TaotaoResult importAllItemToIndex() throws Exception{
		// 1、查询所有商品数据。
		List<SearchItem> itemList = itemMapper.getItemList();
		// 2、创建一个SolrServer对象。
		for (SearchItem searchItem : itemList) {
			// 3、为每个商品创建一个SolrInputDocument对象。
			SolrInputDocument document = new SolrInputDocument();
			// 4、为文档添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_des());
			// 5、向索引库中添加文档。
			solrServer.add(document);
		}
		//提交
		solrServer.commit();
		// 6、返回TaotaoResult。
		return TaotaoResult.ok();
	}
	
	//消息发送后添加索引
	@Override
	public TaotaoResult addDocument(long itemId) throws Exception {
		// 1、根据商品id查询商品信息。
		SearchItem searchItem = itemMapper.geTbItemById(itemId);
		// 2、创建一SolrInputDocument对象。
		SolrInputDocument document = new SolrInputDocument();
		// 3、使用SolrServer对象写入索引库。
		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
		document.addField("item_desc", searchItem.getItem_des());
		// 5、向索引库中添加文档。
		solrServer.add(document);
		solrServer.commit();
		// 4、返回成功，返回TaotaoResult。
		return TaotaoResult.ok();
	}

}
