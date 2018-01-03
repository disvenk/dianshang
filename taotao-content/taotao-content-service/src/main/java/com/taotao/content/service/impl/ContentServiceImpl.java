package com.taotao.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.common.pojo.TaotaoResult;
import com.common.utils.JsonUtils;
import com.taotao.content.jedis.JedisClient;
import com.taotao.content.service.ContentService;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.pojo.TbContentExample.Criterion;

@Service
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${CONTENT_KEY}")
	private String CONTENT_KEY;
	
	//添加内容
	@Override
	public TaotaoResult addContent(TbContent content) {
		//补全属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入数据
		contentMapper.insert(content);
		//缓存同步，删除缓存，让下次直接到数据库去查询再更新到缓存
		jedisClient.hdel(CONTENT_KEY, content.getCategoryId().toString());
		return TaotaoResult.ok();
	}

	//根据内容分类id查找里面具体的内容
	@Override
	public List<TbContent> getContentList(long cid) {
		//先查询缓存，如果没有命中就会查询数据库，然后在添加缓存
		//查询缓存
		try {
			String json = jedisClient.hget(CONTENT_KEY, cid + "");
			//判断json是否为空
			if (StringUtils.isNotBlank(json)) {
				//把json转换成list
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//根据cid查询内容列表
				TbContentExample example = new TbContentExample();
				//设置查询条件
				Criteria criteria = example.createCriteria();
				criteria.andCategoryIdEqualTo(cid);
				//执行查询
				List<TbContent> list = contentMapper.selectByExample(example);
				
				//向缓存中添加数据
				try {
					jedisClient.hset(CONTENT_KEY, cid + "", JsonUtils.objectToJson(list));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return list;
	}
}
