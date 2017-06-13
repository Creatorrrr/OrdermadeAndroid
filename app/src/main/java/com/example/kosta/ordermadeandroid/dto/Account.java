package com.example.kosta.ordermadeandroid.dto;

/**
 * Created by kosta on 2017-06-08.
 */

public class Account {

	private Member member;
	private int money;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
}
