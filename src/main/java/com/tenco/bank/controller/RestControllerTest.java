package com.tenco.bank.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenco.bank.dto.BoardDto;

@RestController // data를 내려 줄 때 사용한다.
public class RestControllerTest {

	// 클라이언트 에서 접근 하는 주소 설계 해보기.
	@GetMapping("my-test1")
	public ResponseEntity<?> myTest1() {

		// 여기서 다른 서버로 자원을 요청한 다음 다시 클라이언트 에게 자원을 내려주려고 한다.

		// http 통신을 첫번째로 위해 URI 객체 만들기
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/todos").encode()
				.build().toUri();

		// http 통신을 위한 객체 생성 과정
		RestTemplate restTemplate = new RestTemplate();

		// http 통신 -> http 메세지에 header, body를(필요에 따라) 구성해서 보내주어야 한다.

		// 헤더 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");

		// 바디 구성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("title", "블로그 포스트 1");
		params.add("body", "후미진 어느 언덕에서 도시락 소풍");
		params.add("userId", "1");

		// 헤더와 바디 결합
		HttpEntity<MultiValueMap<String, String>> requestMessage = new HttpEntity<>(params, headers);

		// 바디와 헤더를 결합해서 55번 라인으로 보내어 처리

		// HTTP 요청 처리
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestMessage, String.class);
		// http://localhost:80/exchange-test
		// 해당 주소로 요청하면 보낸 데이터가 그대로 다시 돌아오도록 하는 주소.
		System.out.println("headers " + response.getHeaders());
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}

	// 주소 localhost:80/todos/{id}
	// 우리 서버에 요청해서 다른 서버를 들러 다시 데이터를 요청한 뒤 해당 데이터를 가지고 와서 뿌려준다.
	@GetMapping("/todos/{id}")
	public ResponseEntity<?> test2(@PathVariable Integer id) {

		// todos에 관한 데이터는 다른 서버에 있기 때문에 요청 해야함.

		// URI urid = new URI("https://jsonplaceholder.typicode.com/" + id);
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/todos")
				.path("/" + id).encode().build().toUri();

		RestTemplate restTemplate = new RestTemplate();

		// ResponseEntity<String> response = restTemplate.getForEntity(uri,
		// String.class); // GET방식 요청, 응답은 ResponsEntity<String>
		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class); // GET방식 요청, 응답은
																						// ResponsEntity<String>

		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}

	// 주소 localhost:80/todos/{id}
	// 우리 서버에 요청해서 다른 서버를 들러 다시 데이터를 요청한 뒤 해당 데이터를 가지고 와서 뿌려준다.
	@GetMapping("/todos/put/{id}")
	public ResponseEntity<?> test3(@PathVariable Integer id) {

		BoardDto dto;
		
		// todos에 관한 데이터는 다른 서버에 있기 때문에 요청 해야함.

		// URI urid = new URI("https://jsonplaceholder.typicode.com/" + id);
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/todos")
				.path("/" + id).encode().build().toUri();

		RestTemplate restTemplate = new RestTemplate();

		// 헤더 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");

		// 바디 구성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("title", "블로그 포스트 1");
		params.add("body", "후미진 어느 언덕에서 도시락 소풍");
		params.add("userId", "1");

		HttpEntity<MultiValueMap<String, String>> requestMessage = new HttpEntity<>(params, headers);

		// ResponseEntity<String> response = restTemplate.getForEntity(uri,
		// String.class); // GET방식 요청, 응답은 ResponsEntity<String>
		//ResponseEntity<BoardDto> response = restTemplate.exchange(uri, HttpMethod.PUT, requestMessage  ); // GET방식
			String response = "";																									// 요청,
																												// 응답은
																												// ResponsEntity<String>

		//return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
			return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}