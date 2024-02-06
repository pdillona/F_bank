package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mysql.cj.callback.UsernameCallback;
import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.repository.interfaces.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service // IoC 대상
public class UserService {

	// db 접근
	// 생성자 의존 주입 DI
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/*
	 * @autowired 와 같은 역할 해당 코드가 있으면 autowired필요 없음 public
	 * UserService(UserRepository userRepository) { this.userRepository =
	 * UserRepository; }
	 */

	/*
	 * 회원 가입 로직 처리
	 * 
	 * @param SignUpFormDto return void
	 */

	// 회원 가입
	@Transactional // 트랜잭션 처리 습관화 할 필요가 있다.
	public void createUser(SignUpFormDto dto) {

		User user = User.builder().username(dto.getUsername()).password(passwordEncoder.encode(dto.getPassword()))
				.fullname(dto.getFullname()).originFileName(dto.getOriginFileName())
				.uploadFileName(dto.getUploadFileName()).build();

		int result = userRepository.insert(user);

		if (result != 1) {
			throw new CustomRestfulException("회원가입 실패", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/*
	 * 로그인 처리
	 * 
	 * @param SignInFormDto
	 * 
	 * @return User
	 * 
	 */

	// 로그인 처리
	public User readUser(SignInFormDto dto) {

		// 암호화 처리 후 변경함

		// 사용자의 username만 받아서 정보를 추출
		User userEntity = userRepository.findByUsername(dto.getUsername());
		if (userEntity == null) {
			throw new CustomRestfulException("존재하지 않는 계정입니다.", HttpStatus.BAD_REQUEST);
		}

		// 비번 확인
		boolean isPwdMatched = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
		if (isPwdMatched == false) {
			throw new CustomRestfulException("틀린 비밀번호입니다.", HttpStatus.BAD_REQUEST);
		}

		/*
		 * 
		 * User user = User.builder() .username(dto.getUsername())
		 * .password(dto.getPassword()) .build(); User userEntity =
		 * userRepository.findByUsernameAndPassword(user);
		 */
	
		
		
		return userEntity;
	}
	
	//사용자 이름만 가지고 정보 조회

	public User readUserByUserName(String Username) {
			
			return userRepository.findByUsername(Username);
		}

}
