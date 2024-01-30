package com.tenco.bank.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Account {

	
	private Integer id; 
	private String number; 
	private String password; 
	private Long balance; 
	private Integer userId;
	private Timestamp createdAt; 
	
	// 출금 기능
	public void withdraw(Long amount) {
		this.balance -= amount;
	}
	
	
	// 입금 기능
	public void deposit(Long amount) {
		this.balance += amount;
	}
	
	
	// 패스워드 체크 기능
	
	
	// 잔액 조회 기능
	
	
	// 계좌 명의 확인 기능
	
	
}
