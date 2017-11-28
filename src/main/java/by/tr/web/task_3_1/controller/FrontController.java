package by.tr.web.task_3_1.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;

import by.tr.web.task_3_1.controller.command.Command;
import by.tr.web.task_3_1.controller.command.CommandProvider;

public class FrontController extends HttpServlet {
	
	public static final String UTF_8 = "utf-8";

	private static final long serialVersionUID = 1L;
      
	private CommandProvider provider = new CommandProvider();
    
	public FrontController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(UTF_8);
		
		String commandName = request.getParameter(ParameterName.COMMAND);
		Command command = null;
		try {
			command = provider.takeCommand(commandName);
			command.execute(request, response);
		} catch (ClassNotFoundException e) {
			response.sendError(404);
		} catch (InstantiationException e) {
			response.sendError(404);
		} catch (IllegalAccessException e) {
			response.sendError(404);
		} catch (SAXException e) {
			response.sendError(404);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding(UTF_8);
		
		String commandName = request.getParameter(ParameterName.COMMAND);
		Command command = null;
		try {
			command = provider.takeCommand(commandName);
			command.execute(request, response);
		} catch (ClassNotFoundException e) {
			response.sendError(404);
		} catch (InstantiationException e) {
			response.sendError(404);
		} catch (IllegalAccessException e) {
			response.sendError(404);
		} catch (SAXException e) {
			response.sendError(404);
		}
	}

}
