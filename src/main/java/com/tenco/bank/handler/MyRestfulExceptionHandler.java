package com.tenco.bank.handler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;

@Order(1)
@RestControllerAdvice
public class MyRestfulExceptionHandler {
	
	// 모든 예외 클래스 설정 
	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println("--------------");
		System.out.println(e.getClass().getName());
		System.out.println(e.getMessage());
		System.out.println("--------------");
	}
	
	@ExceptionHandler(CustomRestfulException.class)
	public String basicException(CustomRestfulException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('" + e.getMessage() + "');");
		sb.append("window.history.back()");
		sb.append("</script>");
		return sb.toString();
	}
	
	@ExceptionHandler(UnAuthorizedException.class)
	public String unAuthrizedException(UnAuthorizedException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('" + e.getMessage() + "');");
		sb.append("location.href='/user/sign-in';");  // 기본 세션 유지 기간이 30분 이라 유저가 아무것도 안하고 30분 지나서 하면 계속 로딩이 돌 수 있어서 location을 사용
		sb.append("</script>");
		return sb.toString();
	}
	
	
}