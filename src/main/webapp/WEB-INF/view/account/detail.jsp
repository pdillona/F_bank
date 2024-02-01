<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>
<!-- 여기 아래 부분 부터 main 영역으로 사용 예정  -->
<div class="col-md-8">
	<h2>나의 계좌 목록</h2>
	<h5>어서오세요! 환영합니다.</h5>
	<!-- 만약 accountList null or not null  -->
	<div class="bg-light text-center" style="padding-bottom: 50px">
		<div style="padding: 50px">
			<table class=table style="border: 1px solid #dee2e6">
				<thead>
					<tr>
						<th>${principal.username} 님의계좌</th>
						<th>계좌번호 : ${account.number}</th>
						<th>잔액 : ${account.balance}</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td><a href="/account/detail/${account.id}">전체 조회</a></td>
						<td><a href="/account/detail/${account.id}?type=deposit">입금
								조회</a></td>
						<td><a href="/account/detail/${account.id}?type=withdraw">출금
								조회</a></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div style="padding-left: 50px; padding-right: 50px;">
			<table class=table>
				<thead>
					<tr>
						<th>날짜</th>
						<th>보낸이</th>
						<th>받은이</th>
						<th>입출금 금액</th>
						<th>계좌 잔액</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="history" items="${historyList}">
						<tr>
							<td>${history.formatCreatedAt()}</td>
							<td>${history.sender}</td>
							<td>${history.receiver}</td>
							<td>${history.amount}</td>
							<td>${history.formatBalance()}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</div>

<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>