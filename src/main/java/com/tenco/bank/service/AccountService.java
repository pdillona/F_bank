package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.utils.Define;

@Service // IoC의 대상 + 싱글톤으로 관리 된다
public class AccountService {

	
	// Solid원칙
	@Autowired
	private AccountRepository accountRepository;
	
	// 계좌 생성
	// 사용자의 정보 필요 (접근 주체의 아이디 = principalId)
	// 계좌 중복여부 확인 todo
	@Transactional
	public void createAccount(AccountSaveFormDto dto, Integer principalId) {
		
		
		
		
		// 계좌 번호 중복 확인
		if(readAccount(dto.getNumber())!= null) {
			throw new CustomPageException(Define.EXIST_ACCOUNT, 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		
		// 예외 처리
		
		System.out.println("계좌 생성 서비스 유저아이디: " + principalId);
		
		Account account = new Account();
		account.setNumber(dto.getNumber());
		account.setPassword(dto.getPassword());
		account.setBalance(dto.getBalance());
		account.setUserId(principalId);
		
		System.out.println(account.toString());
		
		int resultRowCount = accountRepository.insert(account);
		
		System.out.println("리절트로우" + resultRowCount);
		
		if(resultRowCount != 1 ) {
			throw new CustomPageException(Define.FAIL_TO_CREATE_ACCOUNT, 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	
	
	// 단일 계좌 검색 기능
	public Account readAccount(String number) {
		
		return accountRepository.findByNumber(number.trim());
		
	}
	
	// 계좌 목록 보기 기능
	public List<Account> readAccountListByUserId(Integer principalId){
		
		//
		return accountRepository.findAllByUserId(principalId);
	}
	
	
	
}
