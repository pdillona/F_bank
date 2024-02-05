package com.tenco.bank.dto;

import lombok.Data;

@Data
public class BoardDto {

	private String title;
	private String body;
	private int id;
	private int userId;
	private boolean completed;
	
	
	
}
