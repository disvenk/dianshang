package com.taotao.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.pojo.SearchResult;
import com.taotao.search.dao.SearchDao;
import com.taotao.search.service.SearchIndexService;

/**   
*    
* 项目名称：taotao-search-service   
* 类名称：SearchIndexServiceImpl   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2017年9月24日 上午1:46:43   
* @version        
*/

@Service
public class SearchIndexServiceImpl implements SearchIndexService {


		@Autowired
		private SearchDao searchDao;
		//在solr中查询索引
		@Override
		public SearchResult searchIndex(String queryString, int page, int rows) throws Exception {
			// 1、创建一个SolrQuery对象。
			SolrQuery query = new SolrQuery();
			// 2、设置查询条件
			query.setQuery(queryString);
			// 3、设置分页条件
			query.setStart((page - 1) * rows);
			query.setRows(rows);
			// 4、需要指定默认搜索域。
			query.set("df", "item_title");
			// 5、设置高亮
			query.setHighlight(true);
			query.addHighlightField("item_title");
			query.setHighlightSimplePre("<em style=\"color:red\">");
			query.setHighlightSimplePost("</em>");
			// 6、执行查询，调用SearchDao。得到SearchResult
			SearchResult result = searchDao.search(query);
			// 7、需要计算总页数。
			long recordCount = result.getRecordCount();
			long pageCount = recordCount / rows;
			if (recordCount % rows > 0) {
				pageCount++;
			}
			result.setPageCount(pageCount);
			// 8、返回SearchResult
			return result;
		}

	}

