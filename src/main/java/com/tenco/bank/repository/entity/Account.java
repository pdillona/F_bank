package com.tenco.bank.repository.entity;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import org.springframework.http.HttpStatus;

import com.tenco.bank.handler.exception.CustomRestfulException;

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
	public void checkPassword(String password) {
		if(this.password.equals(password)== false) {
			throw new CustomRestfulException("계좌 비밀번호가 틀렸습니다.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 잔액 조회 기능
	public void checkBalance(Long amount) {
		if(this.balance < amount) {
			throw new CustomRestfulException("출금 잔액이 부족합니다.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 계좌 명의 확인 기능
	public void checkOwner(Integer principalId) {
		if(this.userId != principalId) {
			throw new CustomRestfulException("계좌 소유자가 아닙니다.",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// 포메터 기능
	public String formatBalance() {
		// 1000 --> 1,000
		DecimalFormat df = new DecimalFormat("#,###");
		String formaterNumber = df.format(balance);
		return formaterNumber + "원";
		
	}
}
