<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>회원 가입</h2>
	<h5>어서오세요</h5>
	<div>
		<form action="/user/sign-up" method="post"
			enctype="multipart/form-data">
			<!-- 파일 업로드시 enctype="multipart/form-data" 필수  -->
			<div class="form-group">
				<label for="username">username:</label> <input type="text"
					class="form-control" placeholder="Enter username" id="username"
					name="username">
			</div>
			<div class="form-group">
				<label for="pwd">Password:</label> <input type="password"
					class="form-control" placeholder="Enter password" id="pwd"
					name="password">
			</div>
			<div class="form-group">
				<label for="fullname">fullname:</label> <input type="text"
					class="form-control" placeholder="Enter fullname" id="pwd"
					name="fullname">
			</div>

			<div class="custom-file">
				<input type="file" class="custom-file-input" id="customFile"
					name="customFile"> <label class="custom-file-label"
					for="customFile">Choose file</label>
			</div>
			<br>
			<br>
			<br>
			<button type="submit" class="btn btn-primary">회원 가입</button>
		</form>

	</div>
</div>
</div>
</div>

<script>
	// Add the following code if you want the name of the file appear on select
	$(".custom-file-input").on(
			"change",
			function() {
				var fileName = $(this).val().split("\\").pop();
				$(this).siblings(".custom-file-label").addClass("selected")
						.html(fileName);
			});
</script>


<%@ include file="/WEB-INF/view/layout/footer.jsp"%>