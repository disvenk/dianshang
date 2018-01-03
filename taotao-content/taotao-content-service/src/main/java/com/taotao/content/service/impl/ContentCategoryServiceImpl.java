package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.pojo.EasyUITreeNode;
import com.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService{

	@Autowired
	private TbContentCategoryMapper tbContentCategoryMapper;
	
	//根据父节点id查询子节点
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {
		// 1、取查询参数id，parentId
				// 2、根据parentId查询tb_content_category，查询子节点列表。
				TbContentCategoryExample example = new TbContentCategoryExample();
				//设置查询条件
				Criteria criteria = example.createCriteria();
				criteria.andParentIdEqualTo(parentId);
				//执行查询
				// 3、得到List<TbContentCategory>
				List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
				// 4、把列表转换成List<EasyUITreeNode>ub
				List<EasyUITreeNode> resultList = new ArrayList<>();
				for (TbContentCategory tbContentCategory : list) {
					EasyUITreeNode node = new EasyUITreeNode();
					node.setId(tbContentCategory.getId());
					node.setText(tbContentCategory.getName());
					node.setState(tbContentCategory.getIsParent()?"closed":"open");
					//添加到列表
					resultList.add(node);
				}
				return resultList;
			}

	//增加节点
	@Override
	public TaotaoResult addContentCategory(long parentId, String name) {
		//接收两个参数
		//向tb_content_category表中插入数据。
		//创建一个TbContentCategory对象
		TbContentCategory tbContentCategory = new TbContentCategory();
		//补全TbContentCategory对象的属性
				tbContentCategory.setIsParent(false);
				tbContentCategory.setName(name);
				tbContentCategory.setParentId(parentId);
				//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
				tbContentCategory.setSortOrder(1);
				//状态。可选值:1(正常),2(删除)
				tbContentCategory.setStatus(1);
				tbContentCategory.setCreated(new Date());
				tbContentCategory.setUpdated(new Date());
				// c)向tb_content_category表中插入数据
				tbContentCategoryMapper.insert(tbContentCategory);
				// 3、判断父节点的isparent是否为true，不是true需要改为true。
				TbContentCategory parentNode = tbContentCategoryMapper.selectByPrimaryKey(parentId);
				if (!parentNode.getIsParent()) {
					parentNode.setIsParent(true);
					//更新父节点
					tbContentCategoryMapper.updateByPrimaryKey(parentNode);
				}
				// 4、需要主键返回。
				// 5、返回TaotaoResult，其中包装TbContentCategory对象
				return TaotaoResult.ok(tbContentCategory);

	}
	}
	
	

