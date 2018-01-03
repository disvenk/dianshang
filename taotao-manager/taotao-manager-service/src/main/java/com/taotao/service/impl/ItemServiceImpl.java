package com.taotao.service.impl;


import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.pojo.EasyUIDataGridResult;
import com.common.pojo.TaotaoResult;
import com.common.utils.IDUtils;
import com.common.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.manager.jedis.JedisClient;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.service.ItemService;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource(name="topicDestination")
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${REDIS_ITEM_EXPIRE}")
	private Integer REDIS_ITEM_EXPIRE;
	
	//查找所有的商品分页显示
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) throws Exception {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample tbItemExample = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(tbItemExample);
		//取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo(list);
		
		//创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult(); 
		result.setTotal(pageInfo.getTotal());
		result.setRows(pageInfo.getList());
		return result;
	}

	//添加商品
	@Override
	public TaotaoResult addItem(TbItem tbItem, String desc) {
		//生成商品id
		final long itemId = IDUtils.genItemId();
		//补全Item对象的属性
		tbItem.setId(itemId);
		//商品状态：1正常，2下架，3删除
		tbItem.setStatus((byte) 1);
		Date date = new Date();
		tbItem.setCreated(date);
		tbItem.setUpdated(date);
		// 3、向商品表插入数据
		tbItemMapper.insert(tbItem);
		// 4、创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 5、补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(date);
		itemDesc.setUpdated(date);
		// 6、向商品描述表插入数据
		tbItemDescMapper.insert(itemDesc);
		//发送一个商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId+"");
				return textMessage;
			}
		});
		// 7、TaotaoResult.ok()
		//无需返回数据是TaoTaoResult中的data就不用携带数据了
		return TaotaoResult.ok();
		
	}

	//根据id查询商品信息
	@Override
	public TbItem getItemById(long itemId) {
		try{
			String json = jedisClient.get(REDIS_ITEM_KEY+":"+itemId+":BASE");
			if(StringUtils.isNotBlank(json)){
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		TbItem tbItem = tbItemMapper.selectByPrimaryKey(itemId);
		
		try{
			jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":BASE",JsonUtils.objectToJson(tbItem));
			jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":BASE", REDIS_ITEM_EXPIRE);
		}catch(Exception e){
			e.printStackTrace();
		}
		return tbItem;
	}

	//根据商品id查询商品描述
	@Override
	public TbItemDesc getItemDesc(long itemId) {
		try{
			String json = jedisClient.get(REDIS_ITEM_KEY+":"+itemId+":DESC");
			if(StringUtils.isNotBlank(json)){
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);
		

		try{
			jedisClient.set(REDIS_ITEM_KEY+":"+itemId+":DESC",JsonUtils.objectToJson(tbItemDesc));
			jedisClient.expire(REDIS_ITEM_KEY+":"+itemId+":DESC", REDIS_ITEM_EXPIRE);
		}catch(Exception e){
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
