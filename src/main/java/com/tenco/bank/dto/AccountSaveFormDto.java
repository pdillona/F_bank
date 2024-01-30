package com.tenco.bank.dto;

import lombok.Data;

@Data //messageConverter사용시 반드시 setter가 필요하다.
public class AccountSaveFormDto {

	private String number;
	private String password;
	private Long balance;
	
	
}
