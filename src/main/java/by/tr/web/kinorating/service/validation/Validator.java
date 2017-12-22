package by.tr.web.kinorating.service.validation;

import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.User;

public class Validator {

	private static final int MAX_LANG_LENGTH = 2;
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

	public static boolean validateEmail(String email) {
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

	public static boolean validatePassword(String password) {
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

	private static boolean isEmptyOrNull(String string) {
		if (string == null) {
			return true;
		}
		if (string.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean validateUser(User user) {
		String login = user.getLogin();
		String email = user.getEmail();
		if (isEmptyOrNull(login)) {
			return false;
		}
		if (isEmptyOrNull(email)) {
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

	public static boolean validateMovie(Movie movie) {
		String title = movie.getTitle();
		String director = movie.getDirector();
		int length = movie.getLength();
		if (isEmptyOrNull(title)) {
			return false;
		}
		if (isEmptyOrNull(director)) {
			return false;
		}
		if (length <= 0) {
			return false;
		}
		return true;
	}
	
	public static boolean validateLanguageName(String langName) {
		if (isEmptyOrNull(langName)) {
			return false;
		}
		if (langName.length() > MAX_LANG_LENGTH) {
			return false;
		}
		return true;
	}
	
	public static boolean validateAmountMovies(int amount) {
		if (amount <= 0) {
			return false;
		}
		return true;
	}
}
