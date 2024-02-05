package com.tenco.bank.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

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
	
}
