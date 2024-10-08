package com.example.kosta.ordermadeandroid.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kosta on 2017-06-08.
 */

public class Request implements Serializable {

	private String id;
	private String title;
	private Member maker;
	private Member consumer;
	private String category;
	private String content;
	private int hopePrice;
	private int price;
	private List<Comment> comments;
	private List<Attach> attachs;
	private String bound;
	private String payment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Member getMaker() {
		return maker;
	}

	public void setMaker(Member maker) {
		this.maker = maker;
	}

	public Member getConsumer() {
		return consumer;
	}

	public void setConsumer(Member consumer) {
		this.consumer = consumer;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getHopePrice() {
		return hopePrice;
	}

	public void setHopePrice(int hopePrice) {
		this.hopePrice = hopePrice;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public List<Attach> getAttachs() {
		return attachs;
	}

	public void setAttachs(List<Attach> attachs) {
		this.attachs = attachs;
	}

	public String getBound() {
		return bound;
	}

	public void setBound(String bound) {
		this.bound = bound;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	@Override
	public String toString() {
		return "Request{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", maker=" + maker +
				", consumer=" + consumer +
				", category='" + category + '\'' +
				", content='" + content + '\'' +
				", hopePrice=" + hopePrice +
				", price=" + price +
				", comments=" + comments +
				", attachs=" + attachs +
				", bound='" + bound + '\'' +
				", payment='" + payment + '\'' +
				'}';
	}
}
