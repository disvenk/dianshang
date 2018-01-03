package com.taotato.item.controller;

import com.taotao.pojo.TbItem;

public class Item extends TbItem{
	public String[] getImages(){
		String image = this.getImage();
		if(image !=null && !image.equals("")){
			String[] images = image.split(",");
			return images;
		}
		return null;
	}
	
public Item() {
		
	}
	
	public Item(TbItem tbItem) {
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}
}
