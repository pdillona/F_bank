package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.TransferFormDto;
import com.tenco.bank.dto.withdrawFormDto;
import com.tenco.bank.handler.exception.CustomPageException;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.CustomHistoryEntity;
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
		if (readAccount(dto.getNumber()) != null) {
			throw new CustomPageException(Define.EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
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

		if (resultRowCount != 1) {
			throw new CustomPageException(Define.FAIL_TO_CREATE_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 단일 계좌 검색 기능
	public Account readAccount(String number) {

		return accountRepository.findByNumber(number.trim());

	}

	// 계좌 목록 보기 기능
	public List<Account> readAccountListByUserId(Integer principalId) {

		//
		return accountRepository.findAllByUserId(principalId);
	}

	/*
	 * 출금 기능 만들기 1. 계좌 존재 여부 확인 -- select 2. 본인 계좌 인증 -- select (계좌 인증에 select가 필요
	 * 할까? ) 3. 계좌 비번 확인 -- select 4. 잔액 여부 확인 -- select 5. 출금 처리 -- update 6. 거래 내역
	 * 등록 -- insert (history) 7. 트랜잭션 처리
	 */
	@Transactional
	public void updateAccountWithdraw(withdrawFormDto dto, Integer id) {
		// 1
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if (accountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// 2
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
		if (rowResultCount != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 입금 기능 만들기
	// 1. 계좌 존재여부 확인
	// 2. 계좌 존재? -> 본인 계좌 여부 확인
	// 3. 입금 처리
	// 4. 거래 내역 등록
	// 5. 트랜잭션 처리
	@Transactional
	public void updateAccountDeposit(DepositFormDto dto, Integer principalId) {

		// 1. 계좌 존재 여부 확인
		Account accountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if (accountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// 2. 본인 계좌 여부 확인
		accountEntity.checkOwner(principalId);

		// 3. 입금처리
		accountEntity.deposit(dto.getAmount());
		accountRepository.updateById(accountEntity);

		// 6. history에 거래내역 등록
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(null); // 출금 계좌의 잔액을 가져와야하기 때문에
		history.setDBalance(accountEntity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEntity.getId());

		int rowResultCount = historyRepository.insert(history);
		if (rowResultCount != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//	이체 기능만들기
	//	1. 출금 계좌 존재 여부
	//	2. 입금 계좌 존재 확인
	//	3. 출금 계좌 본인 소유 확인
	//	4. 출금 계좌 비번 확인
	//	5. 출금 계좌 잔액 확인
	//	6. 출금 계좌 잔액 객체 수정
	//	7. 입금 계좌 잔액 객체 수정
	//	8. 출금 계좌 update
	//	9. 입금 계좌 update
	//	10. 거래 내역 등록 처리
	//	11.트랜잭션 처리
	@Transactional
	public void updateAccountTransfer(TransferFormDto dto, Integer principalId) {
		Account withdrawAccountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		Account depositAccountEntity = accountRepository.findByNumber(dto.getDAccountNumber());

		if (withdrawAccountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (depositAccountEntity == null) {
			throw new CustomRestfulException("상대방의 계좌 번호가 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		withdrawAccountEntity.checkOwner(principalId);

		withdrawAccountEntity.checkPassword(dto.getPassword());

		withdrawAccountEntity.checkBalance(dto.getAmount());

		withdrawAccountEntity.withdraw(dto.getAmount());

		depositAccountEntity.deposit(dto.getAmount());

		int resultRowCountWithdraw = accountRepository.updateById(withdrawAccountEntity);
		int resultRowCountDeposit = accountRepository.updateById(depositAccountEntity);
		
		if(resultRowCountWithdraw != 1 && resultRowCountDeposit != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		History history = History.builder().amount(dto.getAmount()) // 이체 금액
				.wAccountId(withdrawAccountEntity.getId()) // 출금 계좌
				.dAccountId(depositAccountEntity.getId()) // 입금 계좌
				.wBalance(withdrawAccountEntity.getBalance()) // 출금 계좌 남은 잔액
				.dBalance(depositAccountEntity.getBalance()) // 입금 계좌 남은 잔액
				.build();
		
		int resultRowCountHistory =  historyRepository.insert(history);
		if(resultRowCountHistory != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * 단일 계좌 거래 내역 검색(전체, 입금, 출금) 
	 * @param type = [all, deposit, withdraw]
	 * @param id (account_id) 
	 * @return 동적 쿼리 - List
	 */
	public List<CustomHistoryEntity> readHistoryListByAccount(String type, Integer id) {
		return historyRepository.findByIdHistoryType(type, id);
	}
	
	// 단일 계좌 조회 - AccountByID
	public Account readByAccountId(Integer id) {
		return accountRepository.findByAccountId(id);
	}

}
