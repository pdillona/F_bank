package com.tenco.bank.dto;

import lombok.Data;

@Data
public class SignUpFormDto {

	private String username;
	private String password;
	private String fullname;

	// 파일 처리
}
