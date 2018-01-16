package by.tr.web.kinorating.service.validation;

import by.tr.web.kinorating.domain.Movie;

public class MovieValidator {

	private static final int MIN_RELEASE_YEAR = 1899;

	public static boolean validateNewMovie(Movie newMovie) {
		if (newMovie == null) {
			return false;
		}
		String title = newMovie.getTitle();
		String director = newMovie.getDirector();
		int length = newMovie.getLength();
		if (CommonValidator.isEmptyOrNull(title)) {
			return false;
		}
		if (CommonValidator.isEmptyOrNull(director)) {
			return false;
		}
		if (length <= 0) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieTranslation(Movie translation) {
		if (translation == null) {
			return false;
		}
		int movieID = translation.getId();
		String title = translation.getTitle();
		String director = translation.getDirector();
		if (!validateMovieId(movieID)) {
			return false;
		}
		if (CommonValidator.isEmptyOrNull(title)) {
			return false;
		}
		if (CommonValidator.isEmptyOrNull(director)) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieId(int movieID) {
		if (movieID <= 0) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieTitle(String title) {
		if (CommonValidator.isEmptyOrNull(title)) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieDirector(String director) {
		if (CommonValidator.isEmptyOrNull(director)) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieYear(int year) {
		if (year <= MIN_RELEASE_YEAR) {
			return false;
		}
		return true;
	}

	public static boolean validateMovieGenre(String genre) {
		if (CommonValidator.isEmptyOrNull(genre)) {
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

	public static boolean validateMovieLength(int length) {
		if (length <= 0) {
			return false;
		}
		return true;
	}
}
