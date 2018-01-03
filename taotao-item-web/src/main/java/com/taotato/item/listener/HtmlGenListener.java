package com.taotato.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;
import com.taotato.item.controller.Item;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener{
 
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	@Autowired
	private ItemService itemServiceImpl;
	@Autowired
	private  FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Override
	public void onMessage(Message message) {
		
		try {
			//创建一个MessageListener的接口实现类
			//从message中取商品id
			TextMessage textMessage = (TextMessage) message;
			String strItemId = textMessage.getText();
			Long itemId = Long.parseLong(strItemId);
			//获取商品信息
			TbItem tbItem = itemServiceImpl.getItemById(itemId);
			Item item = new Item(tbItem);
			//获取商品描述
			TbItemDesc tbItemDesc = itemServiceImpl.getItemDesc(itemId);
			//创建数据集
			Map< String, Object> mapData = new HashMap<String,Object>();
			mapData.put("item", item);
			mapData.put("itemDesc", tbItemDesc);
			//创建商品详请页面模板
				//得到一个configuration对象
			Configuration configuration = freeMarkerConfigurer.createConfiguration();
				//使用configuration对象获得一个模板
			Template template = configuration.getTemplate("item.htm");
			//创建输出文件的FileWriter对象
			FileWriter fileWriter = new FileWriter(new File(HTML_OUT_PATH+itemId+".html"));
			//生成文件
			template.process(mapData, fileWriter);
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
}
