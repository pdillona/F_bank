package com.tenco.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/account")
@Controller
public class AccountController {

	@Autowired // 가독성 때문에 생성자를 타는데 Autowired가 걸린 경우가 있다.
	private final HttpSession httpSession; // final을 사용시 생성자가 없으면 에러 난다.
	
	@Autowired
	private final AccountService accountService;
	
	public AccountController(HttpSession httpSession, AccountService accountService) {
		this.httpSession = httpSession;
		this.accountService = accountService;
	}
	
	
	
	/*
	 * http://localhost:80/account/save
	 * 계좌 생성 페이지 요청
	 * @return saveForm.jsp
	 * 
	 */
	@GetMapping("/save")
	public String savePage() {
		
		User principal = (User)httpSession.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해 주세요", 
					HttpStatus.UNAUTHORIZED);
		}
		log.info("ssss" + principal);
		
		return "account/saveForm";
	}
	
	
	/*
	 * 계좌 생성 처리
	 * @parma AccountSaveFormDto
	 * @return list.jsp
	 */
	@PostMapping("/save") // body --> String --> 파싱(messageConverter를 이용한 DTO 방식으로 파싱 전략을 세움)
	public String saveProc(AccountSaveFormDto dto) {
		
		// 1. 인증 검사
		User principal = (User)httpSession.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해 주세요", 
					HttpStatus.UNAUTHORIZED);
		}
		
		// 2. 유효성 검사
		if(dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfulException("계좌 번호를 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("계좌 비밀 번호를 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		if(dto.getBalance() == null || dto.getBalance() < 0) {
			throw new CustomRestfulException("잘못된 금액 입니다.", 
					HttpStatus.BAD_REQUEST);
		}
		
		// 3. 서비스 호출 (비즈니스 처리)
		accountService.createAccount(dto, principal.getId());
		
		
		// 4. 응답 처리
		return "redirect:/account/list";
	}

	/*
	 * 계좌 목록 페이지
	 * @param model - accountList
	 * @return list.jsp
	 */
	@GetMapping({"/list", "/"})
	public String listPage(Model model) {
		
		// 1. 인증 검사 , 반복 되니 필터나 인터셉터로 처리해 주는게 맞긴함 보통 인터셉터 단에서 많이 검사함
		User principal = (User)httpSession.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해 주세요", 
					HttpStatus.UNAUTHORIZED);
		}
		
		// 경우의 수: 0 or 1
		List<Account> accountList = accountService.readAccountListByUserId(principal.getId());
		
		if(accountList.isEmpty()) {
			model.addAttribute("accuntList", null);
		} else {
			model.addAttribute("accountList", accountList);
		}
		
		return "account/list";
	}
	

	
	
}
