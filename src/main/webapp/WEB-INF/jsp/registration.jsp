<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
	
	<fmt:setLocale value="${cookie.locale.value}" />
	<fmt:setBundle basename="localization.locale" var="locale"/>
	<fmt:message bundle="${locale}" key="locale.navbar.link.main.page" var="navbar_link_main_page"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.navbar.link.movies" var="navbar_link_movies"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.navbar.link.reviews" var="navbar_link_reviews"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.navbar.link.search" var="navbar_link_search"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.navbar.link.log.in" var="navbar_link_log_in"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.page.title" var="locale_page_title"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.page.title.registration" var="locale_page_title_registration"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.form.input.email.name" var="log_in_form_input_email_name"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.form.input.password" var="log_in_form_input_password"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.form.label.email.name" var="log_in_form_label_email_name"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.form.label.password" var="log_in_form_label_password"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.link.forgot.password" var="log_in_link_forgot_password"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.link.sign.up" var="log_in_link_sign_up"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.link.remember" var="log_in_link_remember"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.log.in.button.log.in" var="log_in_button_log_in"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.footer.text" var="footer_text"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.button.submit" var="locale_reg_form_button_submit"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.input.email" var="locale_reg_form_input_email"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.input.login" var="locale_reg_form_input_login"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.input.password" var="locale_reg_form_input_password"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.label.email" var="locale_reg_form_label_email"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.label.login" var="locale_reg_form_label_login"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.label.password" var="locale_reg_form_label_password"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.label.password.check" var="locale_reg_form_label_password_check"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.input.password.check" var="locale_reg_form_input_password_check"></fmt:message>
	<fmt:message bundle="${locale}" key="locale.reg.form.checkbox.label" var="locale_reg_form_checkbox_label"></fmt:message>
	
	<title>${locale_page_title_registration}</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="../css/main.css">
</head>

<body>
	<header class="row-fluid">
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
			<a class="navbar-brand" href="app?action=init_view&page=main"><img alt="logo" src="../img/logo.png" ></a>
			<button class="navbar-toggler" type="button" data-toggle="collapse"
				data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
				aria-expanded="false" aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarSupportedContent">
				<ul class="navbar-nav mr-auto">
					<li class="nav-item">
						<a class="nav-link" href="app?action=init_view&page=main">
							<c:out value="${navbar_link_main_page}" />
							<span class="sr-only">(current)</span>
						</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="app?action=init_view&page=movies">
							<c:out value="${navbar_link_movies}" />
						</a>
					</li>
					<li class="nav-item">
						<a class="nav-link" href="app?action=init_view&page=reviews">
							<c:out value="${navbar_link_reviews}" />
						</a>
					</li>
					<c:choose>
						<c:when test="${sessionScope.user.role.toString() == 'ADMIN' }">
							<li class="nav-item">
								<a class="nav-link" href="app?action=init_view&page=users">
									<c:out value="${navbar_link_users}" />
								</a>
							</li>
						</c:when>
					</c:choose>
				</ul>
				<form class="form-inline my-2 my-lg-0" method="get"	action="app">
					<input class="form-control mr-sm-2" type="search" aria-label="Search" name="searchText">
					<button class="btn btn-dark my-2 my-sm-0" type="submit">
						<c:out value="${navbar_link_search}" />
					</button>
					<input type="hidden" name="action" value="search">
				</form>
				<ul class="navbar-nav mx-right">
					<li class="nav-item dropdown">
						<c:choose>
							<c:when test="${sessionScope.authenticated != 'yes' }">
								<a class="nav-link dropdown-toggle" href="#" id="logIn"
									role="button" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false">
									<c:out value="${navbar_link_log_in}" />
								</a>
								<div class="dropdown-menu dropdown-menu-right" aria-labelledby="logIn">
									<form class="dropdown-item" method="post" action="app">
										<div class="form-group">
											<label for="emailOrlogin">
												<c:out value="${log_in_form_label_email_name}" />
											</label>
											<input type="text" class="form-control" name="emailOrlogin"	placeholder="<c:out value="${log_in_form_input_email_name}"/>">
										</div>
										<div class="form-group">
											<label for="password">
												<c:out value="${log_in_form_label_password}" />
											</label>
											<input type="password" class="form-control" name="password" placeholder="<c:out value="${log_in_form_label_password}"/>">
										</div>
										<div class="form-check">
											<label class="form-check-label">
												<input type="checkbox" class="form-check-input" name="rememberUser">
												<c:out value="${log_in_link_remember}" />
											</label>
										</div>
										<input type="hidden" name="action" value="authentication">
										<input type="hidden" name="url" value="${pageContext.request.requestURI}">
										<button type="submit" class="btn btn-primary">
											<c:out value="${log_in_button_log_in}" />
										</button>
									</form>
									<div class="dropdown-divider"></div>
									<a class="dropdown-item" href="registration">
										<c:out value="${log_in_link_sign_up}" />
									</a>
									<a class="dropdown-item" href="#">
										<c:out value="${log_in_link_forgot_password}" />
									</a>
								</div>
							</c:when>
							<c:otherwise>
								<a class="nav-link dropdown-toggle" href="#" id="navbar-user-menu"
									role="button" data-toggle="dropdown" aria-haspopup="true"
									aria-expanded="false">
									<c:out value="${sessionScope.user.login }" />
								</a>
								<div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbar-user-menu">
									<a class="dropdown-item" href="#">
										<c:out value="${user_menu_profile}" />
									</a>
									<div class="dropdown-divider"></div>	
									<a class="dropdown-item" href="app?action=log_off" >  
										<c:out value="${user_menu_log_off}" />
									</a>
								</div>
							</c:otherwise>
						</c:choose>
					</li>
					<form class="nav-item" method="post" action="app">
						<input type="hidden" name="action" value="change_language">
							<div class="btn-group-vertical">
							<button class="btn btn-dark btn-sm py-0 languages" name="locale" type="submit" value="en_US">EN</button>
							<button class="btn btn-dark btn-sm py-0 languages" name="locale" type="submit" value="ru">RU</button>
							<input type="hidden" name="url"	value="${pageContext.request.requestURI}">
						</div>
					</form>
				</ul>
			</div>
		</nav>
	</header>
	<main class="container-fluid">
	<div class="row">
		<div class="col-3"></div>
		<div class="col-6">
			<form method="post" action="app">
				<div class="form-group">
					<div class="form-group">
						<label class="col-form-label" for="loginInput"><c:out value="${locale_reg_form_label_login}"/></label>
						<input type="text" class="form-control" name="login" id="loginInput" placeholder="<c:out value="${locale_reg_form_input_login}"/>">
					</div>
				</div>
				<div class="form-group">
					<label for="emailInput"><c:out value="${locale_reg_form_label_email}"/></label>
					<input type="email" class="form-control" name="email" id="emailInput" placeholder="<c:out value="${locale_reg_form_input_email}"/>">
				</div>
				<div class="form-group">
					<label for="passwordInput"><c:out value="${locale_reg_form_label_password}"/></label>
					<input type="password" class="form-control" name="password" id="passwordInput" placeholder="<c:out value="${locale_reg_form_input_password}"/>">
				</div>
				<div class="form-group">
					<label for="passwordControlInput"><c:out value="${locale_reg_form_label_password_check}"/></label>
					<input type="password" class="form-control" name="passwordControl" id="passwordControlInput" placeholder="<c:out value="${locale_reg_form_input_password_check}"/>">
				</div>
				<div class="form-check">
				    <label class="form-check-label">
				      <input type="checkbox" class="form-check-input" name="agreement">
				      <c:out value="${locale_reg_form_checkbox_label}"></c:out>
				    </label>
				</div>
				<button type="submit" class="btn btn-primary"><c:out value="${locale_reg_form_button_submit}"/></button>
				<input  type="hidden" name="action" value="registration">
				
			</form>
		</div>
		<div class="col-3"></div>
	</div>
	</main>
	<footer class="footer">
		<div class="container">
			<p class="text-center">
				<c:out value="${footer_text}" />
			</p>
		</div>
	</footer>

	<script src="js/jquery-3.2.1.min.js"></script>
	<script src="bootstrap/js/bootstrap.bundle.js"></script>
</body>
</html>