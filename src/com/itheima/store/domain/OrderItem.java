package com.itheima.store.domain;
/**
 * 订单项的实体类
 * @author 趴布里克
 *
 */
public class OrderItem {
	private String itemId;
	private Integer count;
	private Double subtotal;
	//商品信息-->pid
	private Product product;
	//订单信息-->oid
	private Order order;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
}
