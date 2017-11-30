package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tr.web.task_3_1.controller.FrontController;
import by.tr.web.task_3_1.controller.ParameterName;
import by.tr.web.task_3_1.controller.command.Command;
import by.tr.web.task_3_1.domain.User;
import by.tr.web.task_3_1.service.ServiceFactory;
import by.tr.web.task_3_1.service.UserService;

public class AuthenticationImpl implements Command{
	
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
			authenticatedUser = userService.authenticateUserByEmail(emailOrName, password);
		} else {
			authenticatedUser = userService.authenticateUserByLogin(emailOrName, password);
		}
		if (rememberUser == null) {
			rememberUser = EMPTY_STRING;
		}
		HttpSession session =  request.getSession(true);
		if (rememberUser.equalsIgnoreCase(INPUT_CHECKED)) {
			if (authenticatedUser != null) {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_TRUE);
				session.setMaxInactiveInterval(0);
				session.setAttribute("user", authenticatedUser);
			} else {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
			}
			
		} else {
			if (authenticatedUser != null) {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_TRUE);
				session.setAttribute("user", authenticatedUser);
			} else {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
			}
		}
		request.getRequestDispatcher(forwardURL).forward(request, response);;
	}

}
