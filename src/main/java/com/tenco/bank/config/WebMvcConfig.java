package com.tenco.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bank.handler.AuthInterceptor;

// @Configuration --> 스프링 부트 설정 클래스이다. 
@Configuration // IoC 대상 : 2개 이상에 IoC (Bean) 사용
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired // DI 
	private AuthInterceptor authInterceptor;
	/*
	 * - yml과 WebMvcConfigurer에 동시에 suffix와 preifx가 정의되어 있다면? 
	 * Spring Boot는 빈(Bean) 구성에 대한 우선순위를 적용하는데, 
	 * WebMvcConfigurer는 빈으로 등록된 구성 클래스이므로 YAML 파일에 정의된 설정보다 더 우선적으로 처리됩니다.
	 * 따라서, WebMvcConfigurer에서 설정한 suffix와 prefix가 최종적으로 뷰 리졸버(View Resolver)에 적용되어 뷰의 경로를 결정하는 데 사용됩니다.
	 */
	
	
	// 요청 올때 마다 domain URI 검사를 할 예정 
	// /account/xxx <- 으로 들어오는 도메인을 다 검사해!!! 
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor)
		        .addPathPatterns("/account/**")
		        .addPathPatterns("/auth/**");        
	}
	
	@Bean // IoC 대상 - 싱글톤 처리 
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	// 리로스 등록 처리
	// 서버 컴퓨터에 위치한 리소스를 활용하는 방법(프로젝트 외부 폴더 접근)
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 가짜 경로
		registry.addResourceHandler("/images/upload/**")
        .addResourceLocations("file:///C:/dev_workSpace/upload/");

    }
	
	
}