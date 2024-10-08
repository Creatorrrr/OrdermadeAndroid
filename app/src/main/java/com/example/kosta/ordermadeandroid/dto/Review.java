package com.example.kosta.ordermadeandroid.dto;

/**
 * Created by kosta on 2017-06-08.
 */

public class Review {

	private String id;
	private String title;
	private Member consumer;
	private String content;
	private int grade;
	private Product product;

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

	public Member getConsumer() {
		return consumer;
	}

	public void setConsumer(Member consumer) {
		this.consumer = consumer;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}


}
