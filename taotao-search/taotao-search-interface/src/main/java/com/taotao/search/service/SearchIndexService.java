package com.taotao.search.service;

import com.common.pojo.SearchResult;

/**   
*    
* 项目名称：taotao-search-interface   
* 类名称：SearchIndexService   
* 类描述：   
* 创建人：Administrator   
* 创建时间：2017年9月24日 上午1:42:32   
* @version        
*/
public interface SearchIndexService {
	public SearchResult searchIndex(String queryString, int page, int rows) throws Exception;
}
