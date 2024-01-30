<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>my bank</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css">
<script
	src="https://cdn.jsdelivr.net/npm/jquery@3.7.1/dist/jquery.slim.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"></script>
<!-- 외부 스타일 시트 가져오기 -->
<link rel="stylesheet" href="/css/style.css">
</head>
<body>

	<div class="jumbotron text-center banner--img" style="margin-bottom: 0">
		<h1>my bank</h1>
		<p>최첨담 은행 관리 시스템</p>
	</div>

	<nav class="navbar navbar-expand-sm bg-dark navbar-dark">
		<a class="navbar-brand" href="#">MENU</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse"
			data-target="#collapsibleNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="collapsibleNavbar">
			<ul class="navbar-nav">
				<c:choose>
					<c:when test="${principal != null}">
						<li style="color:white; margin-top: 1rem ">
							<p>
								[<span>${principal.username}</span>] 님 환영 합니다.
							</p>
						</li>
						<li class="nav-item" style="padding-left:7px"><a class="nav-link" href="/user/sign-out" style="padding-top:16px">로그아웃</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="nav-item"><a class="nav-link" href="/user/sign-in">로그인</a>
						</li>
						<li class="nav-item"><a class="nav-link" href="/user/sign-up">회원가입</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</nav>

	<div class="container" style="margin-top: 30px">
		<div class="row">
			<div class="col-sm-4">
				<h2>About Me</h2>
				<h5>Photo of me:</h5>
				<div class="m--profile"></div>
				<p>중단기 심화 - 은행 관리 시스템 예제</p>
				<h3>Some Links</h3>
				<p>Lorem ipsum dolor sit ame.</p>
				<ul class="nav nav-pills flex-column">
					<li class="nav-item"><a class="nav-link" href="/account/save">계좌
							생성</a></li>
					<li class="nav-item"><a class="nav-link" href="/account/list">계좌 목록</a></li>
					<li class="nav-item"><a class="nav-link" href="#">출금</a></li>
					<li class="nav-item"><a class="nav-link" href="#">입금</a></li>
					<li class="nav-item"><a class="nav-link" href="#">이체</a></li>
				</ul>
				<hr class="d-sm-none">
			</div>
			<!-- end of header   -->