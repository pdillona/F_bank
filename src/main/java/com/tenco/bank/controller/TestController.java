package com.tenco.bank.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.handler.exception.CustomPageException;

@Controller
@RequestMapping("test") // @GetMapping 앞과 prefix 뒤에 붙는 주소 설정
public class TestController {
	
	
	@GetMapping("/")
	public String main() {
		
        //인증 검사
        //유효성 검사

        //뷰 리졸브 -> 해당하는 파일 찾아서 필요하다면 데이터도 같이 보냄
          //전체경로: /WEB-INF/view/layout/main.jsp
          //prefix: /WEB-INF/view/ 생략
          //suffix: .jsp 생략

        //예외 발생
        throw new CustomPageException("페이지 없음", HttpStatus.NOT_FOUND);

		
		//return "layout/main";
	}
	
	//

}
