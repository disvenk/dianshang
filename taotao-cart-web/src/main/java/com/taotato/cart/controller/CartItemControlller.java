package com.taotato.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.pojo.TaotaoResult;
import com.common.utils.CookieUtils;
import com.common.utils.JsonUtils;
import com.mchange.v2.async.StrandedTaskReporting;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class CartItemControlller {

	@Autowired
	private ItemService itemServiceImpl;
	
	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY;
	@Value("${CART_EXPIRE}")
	private Integer CART_EXPIRE;
	
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId,Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从cookie中取出商品列表
		List<TbItem> cartList = getCartList(request);
		//添加一个标记判断遍历完成是否存在
		boolean flag = false;
		//遍历商品列表
		for (TbItem tbItem : cartList) {
			//查看要添加的商品是否存在
			if(tbItem.getId() == itemId.longValue()){
				//如果存在就相加
				tbItem.setNum(tbItem.getNum()+num);
				flag = true;
				break;
			}
		}
		
		if(!flag){
			TbItem tbItem = itemServiceImpl.getItemById(itemId);
			//取第一张图片
			String images = tbItem.getImage();
			if(StringUtils.isNotBlank(images)){
				String image = images.split(",")[0];
				tbItem.setImage(image);
			}
			//添加商品的数量
			tbItem.setNum(num);
			//把商品添加到购物车列表
			cartList.add(tbItem);
		}
		//将更新的购物车的列表重新回写到cookie
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList),CART_EXPIRE,
				true);
		return "cartSuccess";
	}
	
	//从cookie中取购物车列表
	private List<TbItem> getCartList(HttpServletRequest request){
		//去购物车列表
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_KEY,true);
		//判断json是否是null
		if(StringUtils.isNotBlank(json)){
			//把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		
		return new ArrayList<>();
	}
	
	//展示购物车
	@RequestMapping("/cart/cart")
	public String showCartItem(HttpServletRequest request){
		List<TbItem> list = getCartList(request);
		request.setAttribute("cartList", list);
		return "cart";
	}
	
	//更改购物车商品数量
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult changeCartItemNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//从购物车取出商品列表
		List<TbItem> list = getCartList(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId() == itemId.longValue()){
				tbItem.setNum(num);
				break;
			}
		}
		
		//写回去
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(list),CART_EXPIRE,true);
		return TaotaoResult.ok();
	}
	
	//删除购物车商品
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		//得到购物车商品列表
		List<TbItem> list = getCartList(request);
		for (TbItem tbItem : list) {
			if(tbItem.getId() == itemId.longValue()){
				list.remove(tbItem);
				break;
			}
		}
		
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(list),CART_EXPIRE,true);
		return "redirect:/cart/cart.html";
	}
}
