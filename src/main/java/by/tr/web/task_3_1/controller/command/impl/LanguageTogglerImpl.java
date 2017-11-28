package by.tr.web.task_3_1.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.FrontController;
import by.tr.web.task_3_1.controller.ParameterName;
import by.tr.web.task_3_1.controller.command.Command;

public class LanguageTogglerImpl implements Command {

	private static final String REGEX_REPLACE_OUT = "";
	private static final String REGEX_REPLACE_IN = ".+/";
	

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding(FrontController.UTF_8);
		
		String lang = request.getParameter(ParameterName.LOCALE);
		String url = request.getParameter(ParameterName.URL);
		String forwardUrl = url.replaceFirst(REGEX_REPLACE_IN, REGEX_REPLACE_OUT);
		
		request.getSession(true).setAttribute(ParameterName.LOCALE, lang);
		request.getRequestDispatcher(forwardUrl).forward(request, response);
	}

}
