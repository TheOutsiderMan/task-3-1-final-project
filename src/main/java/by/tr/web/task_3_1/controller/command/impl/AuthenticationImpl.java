package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import by.tr.web.task_3_1.controller.FrontController;
import by.tr.web.task_3_1.controller.ParameterName;
import by.tr.web.task_3_1.controller.command.Command;
import by.tr.web.task_3_1.service.ServiceFactory;
import by.tr.web.task_3_1.service.UserService;

public class AuthenticationImpl implements Command{
	
	private static final String AUTHENTICATED_FALSE = "false";
	private static final String AUTHENTICATED_TRUE = "true";
	private static final String AUTHENTICATED = "authenticated";
	private static final String INPUT_CHECKED = "on";

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding(FrontController.UTF_8);
		
		String emailOrName = request.getParameter(ParameterName.EMAIL_OR_lOGIN);
		String password = request.getParameter(ParameterName.PASSWORD);
		String rememberUser = request.getParameter(ParameterName.REMEMBER_USER);
		String url = request.getParameter(ParameterName.URL);
		
		ServiceFactory factory = ServiceFactory.getInstance();
		UserService userService = factory.getUserService();
		boolean authenticated = false;
		if (emailOrName.contains("@")) {
			authenticated = userService.authenticateUserByEmail(emailOrName, password);
		} else {
			authenticated = userService.authenticateUserByLogin(emailOrName, password);
		}
		if (rememberUser.equalsIgnoreCase(INPUT_CHECKED)) {
			if (authenticated) {
				Cookie cookie =  new Cookie(AUTHENTICATED, AUTHENTICATED_TRUE);
				response.addCookie(cookie);
			} else {
				Cookie cookie =  new Cookie(AUTHENTICATED, AUTHENTICATED_FALSE);
				response.addCookie(cookie);
			}
			
		} else {
			HttpSession session =  request.getSession(true);
			if (authenticated) {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_TRUE);
			} else {
				session.setAttribute(AUTHENTICATED, AUTHENTICATED_FALSE);
			}
		}
		response.sendRedirect(url);
		
	}

}
