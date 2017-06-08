package com.example.kosta.ordermadeandroid.dto;

/**
 * Created by kosta on 2017-06-08.
 */

public class Tag {

	private String keyword;
	private double score;
	private Portfolio portfolio;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}
}
