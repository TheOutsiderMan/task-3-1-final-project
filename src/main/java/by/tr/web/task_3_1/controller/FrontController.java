package by.tr.web.task_3_1.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import by.tr.web.task_3_1.controller.command.Command;
import by.tr.web.task_3_1.controller.command.CommandProvider;

public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private CommandProvider provider = new CommandProvider();
    
	public FrontController() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String commandName = request.getParameter("command");
		Command command = provider.takeCommand(commandName);
		command.execute(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String commandName = request.getParameter("command");
		Command command = provider.takeCommand(commandName);
		command.execute(request, response);
	}

}
