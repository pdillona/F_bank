package com.tenco.bank.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenco.bank.dto.BoardDto;

@RestController  //data를 내려 줄 때 사용한다.
public class RestControllerTest2 {
	// POST 방식과 exchange 메서드 사용 
	@GetMapping("/exchange-test")
	public ResponseEntity<?> restTemplateTeset2() {
		// 자원 등록 요청 --> POST 방식 사용법
		// 1. URI 객체 만들기
		// https://jsonplaceholder.typicode.com/posts
		URI uri = UriComponentsBuilder
				.fromUriString("https://jsonplaceholder.typicode.com")
				.path("/posts")
				.path("/1")
				.encode()
				.build()
				.toUri();
				
		// 2 객체 생성 
		RestTemplate restTemplate = new RestTemplate();
		
		// exchange 사용 방법 
		// 1. HttpHeaders 객체를 만들고 Header 메세지 구성 
		// 2. body 데이터를 key=value 구조로 만들기 
		// 3. HttpEntity 객체를 생성해서 Header 와 결합 후 요청 
		
		// 헤더 구성 
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");
		
		// 바디 구성
		// MultiValueMap<K, V> = {"title" : "[블로그 포스트1]"} 
		// {"title" : "블로그 포스트1"}
		Map<String, String> params = new HashMap<>();
		params.put("title", "foo");
		params.put("body", "bar");
		params.put("userId", "1");
		params.put("id", "1");
		
		// 헤더와 바디 결합 
		HttpEntity<Map<String, String>> requestMessage 
			= new HttpEntity<>(params, headers);

		
		// HTTP 요청 처리 
		// 파싱 처리 해야 한다. 
		ResponseEntity<BoardDto> response 
				=  restTemplate.exchange(uri, HttpMethod.PUT, requestMessage, 
												BoardDto.class);
		BoardDto boardDto = response.getBody();
		System.out.println("TEST : BDTO " + boardDto.toString());
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}
}