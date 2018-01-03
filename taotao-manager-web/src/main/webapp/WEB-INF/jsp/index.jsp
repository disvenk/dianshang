<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>淘淘商城后台管理系统</title>
<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.1/themes/default/easyui.css" />
<link rel="stylesheet" type="text/css" href="js/jquery-easyui-1.4.1/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="css/taotao.css" />
<script type="text/javascript" src="js/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<style type="text/css">
	.content {
		padding: 10px 10px 10px 10px;
	}
</style>
</head>
<body class="easyui-layout">
    <div data-options="region:'west',title:'菜单',split:true" style="width:180px;">
    	<ul id="menu" class="easyui-tree" style="margin-top: 10px;margin-left: 5px;">
         	<li>
         		<span>商品管理</span>
         		<ul>
         		<!-- 我们直接这样写，点击的时候就会在后面直接增加一个路径的名称去请求 -->
	         		<li data-options="attributes:{'url':'item-add'}">新增商品</li>
	         		<li data-options="attributes:{'url':'item-list'}">查询商品</li>
	         		<li data-options="attributes:{'url':'item-param-list'}">规格参数</li>
	         	</ul>
         	</li>
         	<li>
         		<span>网站内容管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':'content-category'}">内容分类管理</li>
	         		<li data-options="attributes:{'url':'content'}">内容管理</li>
	         	</ul>
         	</li>
         	<li>
         		<span>索引库管理</span>
         		<ul>
	         		<li data-options="attributes:{'url':'index-manager'}">索引库管理</li>
	         	</ul>
         	</li>
         </ul>
    </div>
    <div data-options="region:'center',title:''">
    	<div id="tabs" class="easyui-tabs">
		    <div title="首页" style="padding:20px;">
		        	
		    </div>
		</div>
    </div>
    
<script type="text/javascript">
$(function(){
	$('#menu').tree({//获取到id为menu下面所有的子节点，就是每一个标签
		//使用onclick给每个子节点绑定点击事件，function中node就是每一个子节点
		onClick: function(node){
			//判断menu下面当前被点击的节点是不是叶子节点
		if($('#menu').tree("isLeaf",node.target)){//判断是否是叶子节点，node.target就是获得当前节点的元素是谁
				var tabs = $("#tabs");
				//选择当前节点名称且已经打开的选项卡
				var tab = tabs.tabs("getTab",node.text);//得到指定的选项卡面板,‘which’参数可以是标题或索引的选项卡面板。
				
				if(tab){//如果这个选项存在不是undefined，就直接切换到该选项卡
					tabs.tabs("select",node.text);//选择一个选项卡面板,‘which’参数可以是标题或索引的选项卡面板。
				}else{
					//如果选项卡没有打开，就添加
					tabs.tabs('add',{
					    title:node.text,
					    href: node.attributes.url,//请求节点中的url属性中的路径，这里会产生一个空的div,请求将制定路径中的代码填充到div中
					    closable:true,
					    bodyCls:"content"//给该选项卡面板添加一个css样式，这个样式已经在style中定义了
					});
					
				}
			} 
			//alert(node.text);
		}
	});
});
</script>
</body>
</html>