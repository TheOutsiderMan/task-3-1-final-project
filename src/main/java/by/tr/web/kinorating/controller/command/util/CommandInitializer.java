package by.tr.web.kinorating.controller.command.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import by.tr.web.kinorating.controller.command.Command;

public class CommandInitializer {

	private static final String TAG_CLASS = "class";
	private static final String TAG_NAME = "name";
	private static final String TAG_COMMAND = "command";
	private static final String PATH_COMMANDS_XML = "./by/tr/web/kinorating/controller/command/config/commands.xml";

	public Map<String, Command> initializeCommands() throws SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		DOMParser parser = new DOMParser();
		InputStream stream = getClass().getClassLoader().getResourceAsStream(PATH_COMMANDS_XML);
		InputSource inputSource = new InputSource(stream);
		parser.parse(inputSource);

		Document document = parser.getDocument();
		Element root = document.getDocumentElement();
		Map<String, Command> map = new HashMap<>();

		NodeList commandsList = root.getElementsByTagName(TAG_COMMAND);
		for (int i = 0; i < commandsList.getLength(); i++) {
			Element thisNode = (Element) commandsList.item(i);
			Element nameCommand = (Element) thisNode.getElementsByTagName(TAG_NAME).item(0);
			String command = nameCommand.getTextContent();
			Element className = (Element) thisNode.getElementsByTagName(TAG_CLASS).item(0);
			String classToLoad = className.getTextContent();
			Class<?> classLoad = Class.forName(classToLoad);
			Command commandLoad = (Command) classLoad.newInstance();
			map.put(command, commandLoad);
		}

		return map;
	}

}
