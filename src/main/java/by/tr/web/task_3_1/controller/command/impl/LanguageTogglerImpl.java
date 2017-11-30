package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.FrontController;
import by.tr.web.task_3_1.controller.ParameterName;
import by.tr.web.task_3_1.controller.command.Command;

public class LanguageTogglerImpl implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding(FrontController.UTF_8);
		
		String lang = request.getParameter(ParameterName.LOCALE);
		String url = request.getParameter(ParameterName.URL);
		
		Cookie cookie =  new Cookie(ParameterName.LOCALE, lang);
		response.addCookie(cookie);
		response.sendRedirect(url);
	}

}
