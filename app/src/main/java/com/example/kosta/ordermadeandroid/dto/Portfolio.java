package com.example.kosta.ordermadeandroid.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kosta on 2017-06-08.
 */

public class Portfolio implements Serializable{

	private String id;
	private String title;
	private String content;
	private List<Tag> tags;
	private String category;
	private Member maker;
	private String image;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Member getMaker() {
		return maker;
	}

	public void setMaker(Member maker) {
		this.maker = maker;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
