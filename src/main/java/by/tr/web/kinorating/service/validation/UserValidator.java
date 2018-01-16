package by.tr.web.kinorating.service.validation;

import by.tr.web.kinorating.domain.User;

public class UserValidator {
	
	private static final int MAX_MARK = 10;
	private static final int MIN_MARK = 0;
	private static final int MIN_RATING = -9999;
	private static final int MAX_RATING = 9999;
	private static final int PASSWORD_MAX_LENGTH = 24;
	private static final int PASSWORD_MIN_LENGTH = 6;
	private static final int EMAIL_MAX_LENGTH = 36;
	private static final int LOGIN_MAX_LENGTH = 36;
	private static final int LOGIN_MIN_LENGTH = 4;
	private static final String REGEX_PASSWORD = "[\\w^_]{6,}";
	private static final String REGEX_EMAIL = "[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]+";
	private static final String REGEX_LOGIN = "^[a-zA-Zа-яА-Я]{1}\\w{3,}$";

	
	public static boolean validateLogin(String login) {
		if (CommonValidator.isEmptyOrNull(login)) {
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

	public static boolean validateEmail(String email) {
		if (CommonValidator.isEmptyOrNull(email)) {
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

	public static boolean validatePassword(String password) {
		if (CommonValidator.isEmptyOrNull(password)) {
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

	public static boolean validateLoginEmailPassword(String login, String email, String password) {
		if (!validateLogin(login)) {
			return false;
		}
		if (!validateEmail(email)) {
			return false;
		}
		if (!validatePassword(password)) {
			return false;
		}
		return true;
	}

	public static boolean validateLoginPassword(String login, String password) {
		if (!validateLogin(login)) {
			return false;
		}
		if (!validatePassword(password)) {
			return false;
		}
		return true;
	}

	public static boolean validateEmailPassword(String email, String password) {
		if (!validateEmail(email)) {
			return false;
		}
		if (!validatePassword(password)) {
			return false;
		}
		return true;
	}

	public static boolean validateUser(User user) {
		if (user == null) {
			return false;
		}
		String login = user.getLogin();
		String email = user.getEmail();
		if (CommonValidator.isEmptyOrNull(login)) {
			return false;
		}
		if (CommonValidator.isEmptyOrNull(email)) {
			return false;
		}
		return true;
	}

	public static boolean validateRating(double rating) {
		if (rating > MAX_RATING || rating < MIN_RATING) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieMark(int mark) {
		if (mark < MIN_MARK || mark > MAX_MARK) {
			return false;
		}
		return true;
	}
}
