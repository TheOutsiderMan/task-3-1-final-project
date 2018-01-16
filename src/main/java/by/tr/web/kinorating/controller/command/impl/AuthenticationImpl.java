package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.controller.ParameterName;
import by.tr.web.kinorating.controller.command.Command;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.ServiceFactory;
import by.tr.web.kinorating.service.UserService;
import by.tr.web.kinorating.service.exception.ServiceException;

public class AuthenticationImpl implements Command{
	
	private static final String PROBLEM_WITH_AUTHETICATION = "Problem with authetication";
	private static final String ATTR_USER = "user";
	private static final String EMAIL_TRIGGER = "@";
	private static final String EMPTY_STRING = "";
	private static final String REGEX_REPLACE = ".+/";
	private static final String AUTHENTICATED_FALSE = "no";
	private static final String AUTHENTICATED_TRUE = "yes";
	private static final String AUTHENTICATED = "authenticated";
	
	private static final Logger logger = LogManager.getLogger(AuthenticationImpl.class);

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
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
				logger.error(PROBLEM_WITH_AUTHETICATION, e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		} else {
			try {
				authenticatedUser = userService.authenticateUserByLogin(emailOrName, password);
			} catch (ServiceException e) {
				logger.error(PROBLEM_WITH_AUTHETICATION, e);
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
		}
		HttpSession session =  request.getSession(true);
		if (authenticatedUser.getLogin() != null) {
			session.setAttribute(AUTHENTICATED, AUTHENTICATED_TRUE);
			if (rememberUser != null) {
				session.setMaxInactiveInterval(0);
			}
			session.setAttribute(ATTR_USER, authenticatedUser);
		} else {
			session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
		}
		request.getRequestDispatcher(forwardURL).forward(request, response);
	}

}
