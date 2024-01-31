<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- haeder.jsp -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<div class="bg-light p-md-5">
		<h2>이체 페이지(인증)</h2>
		<h5>어서오세요, 환영합니다.</h5>
		<form action="/account/transfer" method="post">
			<div class="form-group">
				<label for="amount">이체 금액:</label> <input type="text" name="amount"
					class="form-control" placeholder="Enter amount" id="amount"
					value="1000">
			</div>
			<div class="form-group">
				<label for="wAccountNumber">출금 계좌 번호:</label> 
				<input type="text"
					name="wAccountNumber" class="form-control" placeholder="출금 계좌번호 입력"
					id="wAccountNumber" value="6666">
			</div>
			<div class="form-group">
				<label for="password">출금 계좌 비밀 번호:</label> 
				<input type="password"
					name="password" class="form-control" placeholder="출금 계좌 비밀번호 입력"
					id="password" value="1234">
			</div>
			<div class="form-group">
				<label for="dAccountNumber">입금(이체) 계좌번호:</label> <input type="text"
					name="dAccountNumber" class="form-control" placeholder="입금 계좌번호 입력"
					id="dAccountNumber" value="1111">
			</div>
			<button type="submit" class="btn btn-primary">이체하기</button>
		</form>
	</div>
</div>

</br>
</div>
</div>


<!-- footer.jsp -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>