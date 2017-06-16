package com.example.kosta.ordermadeandroid.dto;

import java.sql.Date;

/**
 * Created by kosta on 2017-06-08.
 */

public class InviteRequest {

	private String id;
	private String message;
	private Member maker;
	private String requestTime;
	private Request request;
	private String form;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Member getMaker() {
		return maker;
	}

	public void setMaker(Member maker) {
		this.maker = maker;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}
}
