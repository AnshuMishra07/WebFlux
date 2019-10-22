package com.learning.webflux.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Account")
public class Account {
@Id
	private String accountId;
	private String name;
	private String embossingName;
	private String amount;
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmbossingName() {
		return embossingName;
	}
	public void setEmbossingName(String embossingName) {
		this.embossingName = embossingName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	

}
