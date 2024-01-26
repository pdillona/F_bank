package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.CustomPageException;

/**
 * View 렌더링을 위해 
 * ModelView 객체를 반환 하도록 설정 되어 있다.
 * 예외처리 Page 를 리턴 할 때 사용 한다.  
 */

@ControllerAdvice
public class MyPageExceptionHandler {
	
	// CustomPageException <-- 발생 되면 이 함수를 동작 시켜!
	@ExceptionHandler(CustomPageException.class)
	public ModelAndView handlerRuntionException(CustomPageException e) {
		ModelAndView modelAndView = new ModelAndView("errorPage");
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
		modelAndView.addObject("message", e.getMessage());
		return modelAndView; //페이지 반환 + 데이터 내려 줌 
	}
}