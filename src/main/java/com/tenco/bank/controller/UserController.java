package com.tenco.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {
	
	@Autowired
	private HttpSession httpSession;
	
	
	@Autowired 
	private UserService userService;
	
	/*
	 *  회원 가입 페이지요청
	 *  @return SignUp.jsp 파일 리턴
	 *  
	 */
	
	//화면을 반환
	//http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUpPage() {
		// prefix: /WEB-INF/view/
		// suffix: .jsp
		
		return "user/signUp";
	}
	

	
	/*
	 * 회원 가입 요청 처리
	 * 주소 설계  http://localhost:80/user/sign-up
	 *
	 */
	@PostMapping("/sign-up")
	public String singUpProc(SignUpFormDto dto) {
		
		// 1. 인증검사 X
		// 2. 유효성 검사 필요
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("password를 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new CustomRestfulException("fullname을 입력 하세요", 
					HttpStatus.BAD_REQUEST);
		}
		
		// 유효성 검사 이후 service로 넘겨주기
		
		userService.createUser(dto);
		
		
		// todo 로그인 페이지로 변경 예정
		return "redirect:/user/sign-up";
	}
	
	
	/*
	 * 로그인 페이지 요청
	 * @return
	 * 
	 * 기능과 상관없이 구조나 주석 등을 다는 행위를 리팩토링 이라고 한다.
	 * 
	 */
	@GetMapping("/sign-in")
	public String signInPage() {
		
		
		return "/user/signIn";
	}

	/*
	 *  로그인 요청 처리
	 *  @param SignInFormDto
	 *  @return 추후 account/list 페이지로 이동 예정 (todo)
	 * 
	 */
	
	
	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {
		
		// 1. 유효성 검사
		if(dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력 하시오", HttpStatus.BAD_REQUEST);
		}
		
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("Password를 입력 하시오", HttpStatus.BAD_REQUEST);
		}
		
		//서비스 호출 예정
		
		User user = userService.readUser(dto);
		
		httpSession.setAttribute(Define.PRINCIPAL, user); //"principal" 로 가면 데이터 일관성 유지가 힘들다. 상수인 Define의 PRINCIPAL로 데이터의 일관성을 유지 관리 한다.
		
		return "redirect:/account/list";
	}
	
	@GetMapping("/sign-out")
	public String signOutProc() {
		
		httpSession.invalidate();
		
		return "redirect:/user/sign-in";
	}
	
}
