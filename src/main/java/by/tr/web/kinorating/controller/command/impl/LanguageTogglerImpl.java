package by.tr.web.kinorating.controller.command.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.kinorating.controller.ParameterName;
import by.tr.web.kinorating.controller.command.Command;

public class LanguageTogglerImpl implements Command {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		String lang = request.getParameter(ParameterName.LOCALE);
		String url = request.getParameter(ParameterName.URL);
		
		Cookie cookie =  new Cookie(ParameterName.LOCALE, lang);
		response.addCookie(cookie);
		response.sendRedirect(url);
	}

}
