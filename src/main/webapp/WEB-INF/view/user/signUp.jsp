<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>회원 가입</h2>
	<h5>어서오세요 환영 합니다.</h5>
	<form action="/user/sign-up" method="post">
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
		<div class="form-group">
			<label for="fullname">fullname: </label> <input type="text"
				name="fullname" class="form-control" placeholder="Enter fullname"
				id="fullname">
		</div>
		<!-- 이벤트 전파 속성 - 버블링이란? 캡처링이란? -->
		<button type="submit" class="btn btn-primary">회원 가입</button>
	</form>
</div>
</div>
</div>


<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
