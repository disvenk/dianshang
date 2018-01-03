package com.taotao.testActiveMq;

import java.io.IOException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.print.attribute.standard.MediaPrintableArea;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ProducerAck;
import org.junit.Test;

public class TestActiveMq {
	@Test
	public void testQueueProducer() throws JMSException{
		//创建connectionFactor对象，需要指定服务端的ip和端口号
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//使用connectionFactory创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		
		//使用connection创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个Destnation目的地，目的地有两种queue、topic
		Queue queue = session.createQueue("test-queue");
		//使用session创建一个producer对象
		MessageProducer Producer = session.createProducer(queue);
		//创建message对象，创建一个textmessage对象
		TextMessage textMessage = session.createTextMessage("我去你妈的javaeedddddd");
		//使用producer对象发送消息
		Producer.send(textMessage);
		
		//全部关闭
		Producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testQueueConsumer() throws JMSException, IOException{
		//创建一个ConnectionFactory
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		
		//创建session对象
		Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个Distination，和发送端保持一致queue，并且队列名要一致
		Queue queue = session.createQueue("test-queue");
		//使用session创建一个consumer对象
		MessageConsumer consumer = session.createConsumer(queue);
		
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					String text = null;
					//取消息的内容
					text = textMessage.getText();
					// 第八步：打印消息。
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待键盘输入
				System.in.read();
				// 第九步：关闭资源
				consumer.close();
				session.close();
				connection.close();
	}
	
	@Test
	public void testTopticProducer() throws JMSException{
		//创建一个connectionFacotry对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个connectionFactory对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个distination,创建一个topic
		Topic topic = session .createTopic("test-topic");
		//使用session创建一个producer对象
		MessageProducer messageProducer = session.createProducer(topic);
		//创建一个Message对象，创建一个textMessager对象
		TextMessage textMessage = session.createTextMessage("去你妈的人生，去你妈的java");
		//发送消息
		messageProducer.send(textMessage);
		//关闭资源
		connection.close();
		session.close();
		messageProducer.close();
		
	}
	
	@Test
	public void testTopicConsumer() throws JMSException, IOException{
		//创建ConneciontFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		
		//创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用session创建一个Distination，
		Topic topic = session.createTopic("test-topic");
		//使用session创建一个Consumer对象
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage textMessage = (TextMessage) message;
					String text = null;
					// 取消息的内容
					text = textMessage.getText();
					// 第八步：打印消息。
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("topic的消费端03。。。。。");
		// 等待键盘输入
		System.in.read();
		// 第九步：关闭资源
		consumer.close();
		session.close();
		connection.close();
	}
}
