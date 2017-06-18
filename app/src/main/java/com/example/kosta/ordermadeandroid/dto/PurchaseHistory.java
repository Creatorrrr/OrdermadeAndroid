package com.example.kosta.ordermadeandroid.dto;

import java.util.Date;

/**
 * Created by kosta on 2017-06-08.
 */

public class PurchaseHistory {

	private String id;
	private Member maker;
	private Member consumer;
	private Request request;
	private String orderDate;
	private String invoiceNumber;
	private int charge;
	private String deliveryStatus;
	private String payment;
	private String page;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(String deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	@Override
	public String toString() {
		return "PurchaseHistory{" +
				"id='" + id + '\'' +
				", maker=" + maker +
				", consumer=" + consumer +
				", request=" + request +
				", orderDate=" + orderDate +
				", invoiceNumber='" + invoiceNumber + '\'' +
				", charge=" + charge +
				", deliveryStatus='" + deliveryStatus + '\'' +
				", payment='" + payment + '\'' +
				", page='" + page + '\'' +
				'}';
	}
}
