package com.itheima.store.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车的实体类
 * @author 趴布里克
 *
 */
public class Cart {
	/**
	 * 购物车的属性
	 */
	private Double total = 0d;
	//Map集合存放购物项的信息,便于删除操作
	private Map<String, CartItem> map = new LinkedHashMap<String, CartItem>();
	
	//只需get方法,因为是内部操作
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public void setMap(Map<String, CartItem> map) {
		this.map = map;
	}
	public Map<String, CartItem> getMap() {
		return map;
	}
	
	/**
	 * 购物车的方法
	 */
	//将商品添加到购物车
	public void addCart(CartItem cartItem) {
		//先判断购物车中是否有该商品
		String pid = cartItem.getProduct().getPid();
		if(map.containsKey(pid)) {
			//购物车中有该商品
			CartItem _cartItem = map.get(pid);
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());
		}else {
			//购物车中没有改商品
			map.put(pid, cartItem);
		}
		
		total += cartItem.getSubtotal();
	}
	
	//将商品从购物车中移除
	public void removeCart(String pid) {
		//从map中移除某个元素
		CartItem cartItem = map.remove(pid);
		//总金额减去移除元素的金额
		total -= cartItem.getSubtotal();
	}
	
	//清空购物车
	public void clearCart() {
		//清空集合,并将总金额设置为0即可
		map.clear();
		total = 0d;
	}
	
}
