package com.itheima.store.domain;
/**
 * 购物项的实体类
 * @author 趴布里克
 *
 */
public class CartItem {
	//商品
	private Product product;
	//数量
	private Integer count;
	//小计
	private Double subtotal;
	public CartItem() {
		super();
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Double getSubtotal() {
		//数量乘以单价
		return count * product.getShop_price();
	}
	
	//set方法可不要,内部设置
	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
	
	
}
