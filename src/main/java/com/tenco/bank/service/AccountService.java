package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.withdrawFormDto;
import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.History;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.utils.Define;

@Service // IoC의 대상 + 싱글톤으로 관리 된다
public class AccountService {

	
	// Solid원칙
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private HistoryRepository historyRepository;
	
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



	/*
	 * 출금 기능 만들기
	 * 1. 계좌 존재 여부 확인 -- select
	 * 2. 본인 계좌 인증 -- select (계좌 인증에 select가 필요 할까? )
	 * 3. 계좌 비번 확인 -- select
	 * 4. 잔액 여부 확인 -- select
	 * 5. 출금 처리 -- update
	 * 6. 거래 내역 등록 -- insert (history)
	 * 7. 트랜잭션 처리 
	 */
	@Transactional
	public void updateAccountWithdraw(withdrawFormDto dto, Integer id) {
		// 1
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if(accountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//2
		accountEntity.checkOwner(id);
	
		// 3
		// 결과값 f == f 면 true
		accountEntity.checkPassword(dto.getWAccountPassword());
		
		// 4
		accountEntity.checkBalance(dto.getAmount());
		
		// 5 
		// 현재 생성된 객체(Account) 상태값 변경
		accountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(accountEntity);
		
		// 6
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(accountEntity.getBalance());
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);
		
		int rowResultCount = historyRepository.insert(history);
		if(rowResultCount != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다.", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	
	
}
