package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.repository.entity.Account;

@Mapper // 반드시 확인
public interface AccountRepository {
	
	public int insert(Account account);
	public int updateById(Account account);
	public int deleteById(Integer id);
	
	// 계좌 조회 - 1 유저 , N 계좌 
	public List<Account> findAllByUserId(Integer userId);
	public Account findByNumber(String number); // 코드 수정
	public Account findByAccountId(Integer id);
}