package com.taotao.search.messageListener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.search.service.SearchIndexService;
import com.taotao.search.service.SearchItemService;


public class ItemAddListener implements MessageListener{

	@Autowired
	private SearchItemService searchItemServiceImpl;
	
	@Override
	public void onMessage(Message message) {
		try{
			TextMessage textMessage = null;
			Long itemId = null;
			
			//取商品id
			if(message instanceof TextMessage){
				textMessage = (TextMessage) message;
				itemId = Long.parseLong(textMessage.getText());
			}
			//添加到索引
			searchItemServiceImpl.addDocument(itemId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
