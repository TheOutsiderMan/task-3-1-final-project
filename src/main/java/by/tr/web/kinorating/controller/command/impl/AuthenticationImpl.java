package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tr.web.kinorating.controller.FrontController;
import by.tr.web.kinorating.controller.ParameterName;
import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.ServiceFactory;
import by.tr.web.kinorating.service.UserService;
import by.tr.web.kinorating.service.exception.ServiceException;

public class AuthenticationImpl implements Command{
	
	private static final String ATTR_USER = "user";
	private static final String EMAIL_TRIGGER = "@";
	private static final String EMPTY_STRING = "";
	private static final String REGEX_REPLACE = ".+/";
	private static final String AUTHENTICATED_FALSE = "no";
	private static final String AUTHENTICATED_TRUE = "yes";
	private static final String AUTHENTICATED = "authenticated";
	private static final String INPUT_CHECKED = "on";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		request.setCharacterEncoding(FrontController.UTF_8);
		
		String emailOrName = request.getParameter(ParameterName.EMAIL_OR_lOGIN);
		String password = request.getParameter(ParameterName.PASSWORD);
		String rememberUser = request.getParameter(ParameterName.REMEMBER_USER);
		String url = request.getParameter(ParameterName.URL);
		String forwardURL = url.replaceFirst(REGEX_REPLACE, EMPTY_STRING);
		
		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();
		User authenticatedUser = null;
		if (emailOrName.contains(EMAIL_TRIGGER)) {
			try {
				authenticatedUser = userService.authenticateUserByEmail(emailOrName, password);
			} catch (ServiceException e) {
				//log
				response.sendError(404);
				return;
			}
		} else {
			try {
				authenticatedUser = userService.authenticateUserByLogin(emailOrName, password);
			} catch (ServiceException e) {
				//log
				response.sendError(404);
				return;
			}
		}
		if (rememberUser == null) {
			rememberUser = EMPTY_STRING;
		}
		HttpSession session =  request.getSession(true);
		if (rememberUser.equalsIgnoreCase(INPUT_CHECKED)) {
			if (authenticatedUser != null) {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_TRUE);
				session.setMaxInactiveInterval(0);
				session.setAttribute(ATTR_USER, authenticatedUser);
			} else {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
			}
			
		} else {
			if (authenticatedUser != null) {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_TRUE);
				session.setAttribute(ATTR_USER, authenticatedUser);
			} else {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
			}
		}
		request.getRequestDispatcher(forwardURL).forward(request, response);;
	}

}
