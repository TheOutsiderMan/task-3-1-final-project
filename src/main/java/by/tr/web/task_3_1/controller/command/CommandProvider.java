package by.tr.web.task_3_1.controller.command;

import java.util.HashMap;
import java.util.Map;

import by.tr.web.task_3_1.controller.command.impl.AuthenticationImpl;
import by.tr.web.task_3_1.controller.command.impl.LanguageToggler;
import by.tr.web.task_3_1.controller.command.impl.RegistrationImpl;
import by.tr.web.task_3_1.controller.command.impl.SearchImpl;

public class CommandProvider {
	
	private Map<CommandName, Command> commands = new HashMap<>();

	public CommandProvider() {
		commands.put(CommandName.SEARCH, new SearchImpl());
		commands.put(CommandName.AUTHENTICATION, new AuthenticationImpl());
		commands.put(CommandName.REGISTRATION, new RegistrationImpl());
		commands.put(CommandName.CHANGE_LANGUAGE, new LanguageToggler());
	}
	
	public Command takeCommand(String name) {
		CommandName commandName = CommandName.valueOf(name.toUpperCase());
		return commands.get(commandName);
	}
	
}