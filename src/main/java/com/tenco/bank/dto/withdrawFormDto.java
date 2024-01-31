package com.tenco.bank.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class withdrawFormDto {
	
	private Long amount;
	private String wAccountNumber;
	private String wAccountPassword;
	

}
