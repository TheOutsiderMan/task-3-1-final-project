package by.tr.web.task_3_1.service.validation;

public class Validator {
	
	private static final int PASSWORD_MAX_LENGTH = 24;
	private static final int PASSWORD_MIN_LENGTH = 6;
	private static final int EMAIL_MAX_LENGTH = 36;
	private static final int LOGIN_MAX_LENGTH = 36;
	private static final int LOGIN_MIN_LENGTH = 6;
	private static final String REGEX_PASSWORD = "[\\w^_]{6;}";
	private static final String REGEX_EMAIL = "[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+";
	private static final String REGEX_LOGIN = "^[a-zA-Zа-яА-Я]{1}\\w{5;}$";

	public boolean validateLogin(String login) {
		if (isEmptyOrNull(login)) {
			return false;
		}
		if (login.length() < LOGIN_MIN_LENGTH || login.length() > LOGIN_MAX_LENGTH) {
			return false;
		}
		if (!login.matches(REGEX_LOGIN)) {
			return false;
		} 
		return true;
	}
	
	public boolean validateEmail(String email) {
		if (isEmptyOrNull(email)) {
			return false;
		}
		if (email.length() > EMAIL_MAX_LENGTH) {
			return false;
		}
		if (!email.matches(REGEX_EMAIL)) {
			return false;
		}
		return true;
	}
	
	public boolean validatePassword(String password) {
		if (isEmptyOrNull(password)) {
			return false;
		}
		if (password.length() < PASSWORD_MIN_LENGTH || password.length() > PASSWORD_MAX_LENGTH) {
			return false;
		}
		if (!password.matches(REGEX_PASSWORD)) {
			return false;
		}
		return true;
	}
	
	private static boolean isEmptyOrNull(String string) {
		if (string == null) {
			return true;
		}
		if (string.isEmpty()) {
			return true;
		}
		return false;
	}
	
}
