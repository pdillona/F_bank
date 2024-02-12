package com.tenco.bank.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bank.dto.KakaoProfile;
import com.tenco.bank.dto.OAuthToken;
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
		
		
		// 파일 업로드
		MultipartFile file = dto.getCustomFile(); 
		if(file.isEmpty() == false) {
			// 사용자가 이미지를 업로드 했다면 기능 구현
			// 파일 사이즈 체크
			// 20MB 
			if(file.getSize() > Define.MAX_FILE_SIZE) {
				throw new CustomRestfulException("파일의 크기는 20MB보다 클 수 없습니다.",
						HttpStatus.BAD_REQUEST);
			}
			
			// 서버 컴퓨터에 파일 넣을 디렉토리가 있는지 검사해 줘야 함.
			String saveDirectory =  Define.UPLOAD_FILE_DIRECTORY;
			// 폴더가 없다면 오류 발생(파일 생성시)
			File dir = new File(saveDirectory); // 경로 설정
			if(dir.exists() == false) {
				dir.mkdir(); // 폴더가 없을시 폴더를 생성해 준다.
				
			}
			// oriName과 uploadName이 달라야 하는 이유는 여러 클라이언트가 같은 파일명으로 올리면 덮어 써버리기 때문이다.
			// 파일 이름(중복처리 예방)
			UUID uuid = UUID.randomUUID();
			String fileName = uuid + "_" + file.getOriginalFilename();
			System.out.println("fileName: " + fileName);
			// C:\\dev_workSpace\\upload\ab.png 같은 형식으로 만들기 위해
			String uploadPath = Define.UPLOAD_FILE_DIRECTORY + File.separator + fileName;
			File destination = new File(uploadPath);
			
			try {
				file.transferTo(destination);  // 파일 생성해 줌
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// 객체 상태 변경
			dto.setOriginFileName(file.getOriginalFilename());
			dto.setUploadFileName(fileName);
			
		}
		System.out.println("dto.OriginFileName: " + dto.getOriginFileName());
		System.out.println("dto.UploadFileName: " + dto.getUploadFileName());
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
	
	/* @RequestMapping("/user") 때문에 /user 가 달려있다.
	 * 카카오 디벨로퍼에서 redirect key 를 줄바꿈으로 10개까지 등록 가능하다. 해서
	 *  http://localhost:80/user/kakao-callback?code="xxxxxxxxxx" 처럼 만들어준다. 즉 /user대문을 달아도 요청 가능하게 해주자. 
	 */
	// http://localhost:80/user/kakao-callback?code="xxxxxxxxx"
	//@ResponseBody // <-- 데이터를 반환 // 테스트 종료되어 view리졸버 타게 제거 
	@GetMapping("/kakao-callback")
	public String kakaoCallback(@RequestParam String code) {
		
		// POST 방식 , Header 구성, body 구성 
		System.out.println("code :  " + code);
		
		RestTemplate rt1 = new RestTemplate();
		// 헤더 구성 
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// body 구성 
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "aeff9f33b9d74346008005686767c513");
		params.add("redirect_uri", "http://localhost/user/kakao-callback");
		params.add("code", code);
		
		System.out.println("params:  "+params.toString());
		
		
		// 헤더 + 바디 결합 
		HttpEntity<MultiValueMap<String, String>> reqMsg 
			= new HttpEntity<>(params, headers1);
		
		System.out.println("reqMsg:   "+reqMsg.toString());
		
		
		ResponseEntity<OAuthToken> response = rt1.exchange("https://kauth.kakao.com/oauth/token", 
				HttpMethod.POST, reqMsg, OAuthToken.class);
		
		System.out.println("response:  "+response.toString());
		
		System.out.println("================================================");
		
		
		// 다시 요청하기 -- 인증 토큰 -- 사용자 정보 요청
		RestTemplate rt2 = new RestTemplate();
		// 헤더
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + response.getBody().getAccessToken());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// 바디 x
		// 결합
		HttpEntity<MultiValueMap<String, String>> kakaoInfo = new HttpEntity<>(headers2);
		ResponseEntity<KakaoProfile> response2 
		= rt2.exchange("https://kapi.kakao.com/v2/user/me", 
				HttpMethod.POST, kakaoInfo, KakaoProfile.class);

		System.out.println(response2.getBody());

		/* 로그인 처리
		 * 단 최초 요청 사용자라면 회원 가입 처리 후 로그인 처리
		 * 
		 * 현재 로그인시 signUpFormDto와 
		 */
		
		
		// 최초 사용자 판단 여부 -- 사용자 username 존재 여부 확인
		// 로컬의 유저네임과 카카오의 유저네임이 동일 할 수 있음(문제 발생)
		KakaoProfile kakaoProfile = response2.getBody();
		
		SignUpFormDto dto = SignUpFormDto.builder()
				.username("OAuth_" + kakaoProfile.getProperties().getNickname())
				.fullname("Kakao")
				.password("asd1234").build();       //소셜 로그인시 모든 사용자의 비번이 같음 그래서 털리지 않게 관리 해야 하며 소셜 로그인 유저는 비밀번호 변경이 불가 해야 한다.
				
		
		User oldUser = userService.readUserByUserName(dto.getUsername());
		
		/*
		 * 
		 * 최초 가입이라면 oldUser에는 null이 담겨 있다.
		 * 해서 아래 두가지 처리
		 * oldUser.setUsername(dto.getUsername());
		 *	oldUser.setFullname(dto.getFullname());
		 *
		 * */ 
		if(oldUser == null) {
			userService.createUser(dto);
			
			/*
			 * 267라인 oldUser가 null 값이 들어올 때
			 * java.lang.NullPointerException
			 * Cannot invoke "com.tenco.bank.repository.entity.User.setUsername(String)" because "oldUser" is null
			 *  
			 */
			oldUser= new User();
			 
			oldUser.setUsername(dto.getUsername());
			oldUser.setFullname(dto.getFullname());
		}
		
		oldUser.setPassword(null);  // 보안적 이유로 password는 null로 내림
		
		// 로그인 처리
		httpSession.setAttribute(Define.PRINCIPAL, oldUser);
		
		
		// 단 최초 요청 사용자라면 회원 가입 처리 후 로그인 처리
		return "redirect:/account/list";
	}
	

	
	
}
