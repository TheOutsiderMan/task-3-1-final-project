<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>

	<fmt:setLocale value="${cookie.locale.value}" />
	<fmt:setBundle basename="localization.locale" var="locale" />
	<fmt:message bundle="${locale}" key="locale.navbar.link.main.page" var="navbar_link_main_page" />
	<fmt:message bundle="${locale}" key="locale.navbar.link.movies"	var="navbar_link_movies"/>
	<fmt:message bundle="${locale}" key="locale.navbar.link.users"	var="navbar_link_users"/>
	<fmt:message bundle="${locale}" key="locale.navbar.link.reviews" var="navbar_link_reviews"/>
	<fmt:message bundle="${locale}" key="locale.navbar.link.search"	var="navbar_link_search"/>
	<fmt:message bundle="${locale}" key="locale.navbar.link.log.in"	var="navbar_link_log_in"/>
	<fmt:message bundle="${locale}" key="locale.navbar.user.menu.log.off" var="user_menu_log_off"/>
	<fmt:message bundle="${locale}" key="locale.navbar.user.menu.profile" var="user_menu_profile"/>		
	<fmt:message bundle="${locale}" key="locale.page.title"	var="locale_page_title"/>
	<fmt:message bundle="${locale}"	key="locale.log.in.form.input.email.name" var="log_in_form_input_email_name"/>
	<fmt:message bundle="${locale}" key="locale.log.in.form.input.password"	var="log_in_form_input_password"/>
	<fmt:message bundle="${locale}"	key="locale.log.in.form.label.email.name" var="log_in_form_label_email_name"/>
	<fmt:message bundle="${locale}" key="locale.log.in.form.label.password" var="log_in_form_label_password"/>
	<fmt:message bundle="${locale}" key="locale.log.in.link.forgot.password" var="log_in_link_forgot_password"/>
	<fmt:message bundle="${locale}" key="locale.log.in.link.sign.up" var="log_in_link_sign_up"/>
	<fmt:message bundle="${locale}" key="locale.log.in.link.remember" var="log_in_link_remember"/>
	<fmt:message bundle="${locale}" key="locale.log.in.button.log.in" var="log_in_button_log_in"/>
	<fmt:message bundle="${locale}" key="locale.footer.text" var="footer_text"/>
	<fmt:message bundle="${locale}" key="locale.page.body.minutes" var="minutes"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.refuse" var="vote_not_ok_msg"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.accept" var="vote_ok_msg"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.user" var="user_vote"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.amount.votes" var="amount_votes"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.user.nothing" var="no_user_vote"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.delete" var="delete_vote"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.refuse.error" var="vote_error"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.delete.error" var="delete_vote_error"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.delete.succes" var="delete_vote_succes"/>
	<fmt:message bundle="${locale}" key="locale.movie.vote.rating" var="vote_rating"/>

	<title>${locale_page_title}</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
	<link rel="apple-touch-icon" sizes="57x57" href="favicon/apple-icon-57x57.png">
	<link rel="apple-touch-icon" sizes="60x60" href="favicon/apple-icon-60x60.png">
	<link rel="apple-touch-icon" sizes="72x72" href="favicon/apple-icon-72x72.png">
	<link rel="apple-touch-icon" sizes="76x76" href="favicon/apple-icon-76x76.png">
	<link rel="apple-touch-icon" sizes="114x114" href="favicon/apple-icon-114x114.png">
	<link rel="apple-touch-icon" sizes="120x120" href="favicon/apple-icon-120x120.png">
	<link rel="apple-touch-icon" sizes="144x144" href="favicon/apple-icon-144x144.png">
	<link rel="apple-touch-icon" sizes="152x152" href="favicon/apple-icon-152x152.png">
	<link rel="apple-touch-icon" sizes="180x180" href="favicon/apple-icon-180x180.png">
	<link rel="icon" type="image/png" sizes="192x192"  href="favicon/android-icon-192x192.png">
	<link rel="icon" type="image/png" sizes="32x32" href="favicon/favicon-32x32.png">
	<link rel="icon" type="image/png" sizes="96x96" href="favicon/favicon-96x96.png">
	<link rel="icon" type="image/png" sizes="16x16" href="favicon/favicon-16x16.png">
	<link rel="manifest" href="favicon/manifest.json">
	<meta name="msapplication-TileColor" content="#ffffff">
	<meta name="msapplication-TileImage" content="favicon/ms-icon-144x144.png">
	<meta name="theme-color" content="#ffffff">
	
	<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
	<link rel="stylesheet" href="css/main.css">
	<script src="js/jquery-3.2.1.min.js"></script>
	<script src="bootstrap/js/bootstrap.bundle.js"></script>
	<script src="js/rating.js"></script>
	<script src="js/main.js"></script>
</head>

<body>
	<header class="row-fluid">
		<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
			<a class="navbar-brand" href="app?action=init_view&page=main"><img alt="logo" src="img/logo.png" ></a>
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
									<c:out value="${sessionScope.user.login}" />
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
	<main role="main" class="container-fluid">
		<div class="row">
			<div class="col-6 offset-3" id="items">
			<c:set var="randomMovies" value="${requestScope.item}"></c:set>	
			<c:choose>
				<c:when test="${not empty randomMovies}">
					<ul class="list-group">
						<c:forEach items="${randomMovies}" var="movie">
							<li class="list-group-item card">
								<div class="row">
									<div class="col-7">
										<input type="hidden" name="movie" value='<c:out value="${movie.id}"/>'>
										<h3 class="card-title">
											<a href="movies/${movie.id}"><c:out value="${movie.title}"/></a>	
										</h3>
										<h6 class="card-text text-secondary"><c:out value="${movie.year}, ${movie.length} ${minutes}"/></h6>
										<h5 class="card-text text-secondary"><c:out value="${movie.director}"/></h5>
										<h5 class="card-text text-secondary">
											<c:forEach items="${movie.actors}" var="actor" varStatus="i" end="3">
										   		<c:choose>
										  			<c:when test="${i.count == 3 || i.count == movie.actors.size()}">
										   				<a href="actors/${actor.id}"><c:out value="${actor.firstName} ${actor.secondName}"/></a>
										   			</c:when>
										   			<c:otherwise>
										   				<a href="actors/${actor.id}"><c:out value="${actor.firstName} ${actor.secondName}"/></a>, 
										   			</c:otherwise>
										   		</c:choose>
										   	</c:forEach>
										</h5>
										<h6 class="card-text text-secondary"><c:out value="${movie.genre}"/></h6>
									</div>
									<div class="col-5">
										<div id="rating-movie-${movie.id}">
								            <input type="hidden" class="val" value="<c:out value="${movie.rating}"/>"/>
								        	<input type="hidden" class="votes" value="<c:out value="${movie.voteAmount}"/>"/>
										</div>
										<script type="text/javascript">
											$(function(){
												var auth = false; 
												if('${sessionScope.authenticated}' == 'yes') {
													auth = true
												}
												$('#rating-movie-${movie.id}').rating({
											        image: 'img/stars.png',
											        width: 32,
													url: 'app',
													auth: auth,
													msgOK: '${vote_ok_msg}',
													msgNotOk: '${vote_not_ok_msg}',
													login: '${sessionScope.user.login}',
													errorMsg: '${vote_error}',
													movieID: '${movie.id}'
												});
											})
										</script>
										<div class="vote-container">
											<h5 class="card-text text-secondary"><c:out value="${vote_rating}"/> <b><c:out value="${movie.rating}"/></b></h5>
											<c:choose>
												<c:when test="${user.marks.containsKey(movie.id)}">
													<h5 class="card-text text-secondary user-vote">
														<c:out value="${user_vote}"/> <b><c:out value="${user.marks.get(movie.id)}"/></b>
													</h5>
													<a href="#" data-user="${user.login}" data-movie="${movie.id}" data-error="${delete_vote_error}" data-success="${delete_vote_succes}" data-page="${pageContext.request.requestURL}" class="delete-movie btn btn-secondary"><c:out value="${delete_vote}"/></a>
												</c:when>
												<c:otherwise>
													<h6 class="card-text text-secondary">
														<c:out value="${no_user_vote}"/>
													</h6>
												</c:otherwise>
											</c:choose>
											<h5 class="card-text text-secondary">
												<c:out value="${amount_votes} ${movie.voteAmount}" />
											</h5>
										</div>
									</div>
								</div>
							</li>
						</c:forEach>
					</ul>
				</c:when>
				<c:otherwise>
					<c:redirect url="app?action=init_view&page=main"/>
				</c:otherwise>
			</c:choose>	
			</div>
		</div>
	</main>
	<footer class="footer">
		<div class="container">
			<p class="text-center">
				<c:out value="${footer_text}" />
			</p>
		</div>
	</footer>
</body>
</html>