<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>로그인</h2>
	<h5>어서오세요 환영 합니다.</h5>
	<form action="/user/sign-in" method="post"> <!-- 자원의 요청이지만 로그인만 예외 적으로 post로 요청 -->
		<div class="form-group">
			<label for="username">username: </label> <input type="text"
				name="username" class="form-control" placeholder="Enter username"
				id="username">
		</div>
		<div class="form-group">
			<label for="pwd">password: </label> <input type="password"
				name="password" class="form-control" placeholder="Enter password"
				id="pwd">
		</div>
		<button type="submit" class="btn btn-primary">로그인</button>
	</form>
</div>
</div>
</div>


<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
