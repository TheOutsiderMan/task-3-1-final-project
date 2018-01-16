package by.tr.web.kinorating.service.validation;

import by.tr.web.kinorating.domain.Review;

public class ReviewValidator {

	public static boolean validateTextReview(String text) {
		if (CommonValidator.isEmptyOrNull(text)) {
			return false;
		}
		return true;
	}

	public static boolean validateReview(Review review) {
		if (review == null) {
			return false;
		}
		if (!UserValidator.validateLogin(review.getAuthor().getLogin())) {
			return false;
		}
		if (!MovieValidator.validateMovieId(review.getReviewedMovie().getId())) {
			return false;
		}
		if (!validateTextReview(review.getTextReview())) {
			return false;
		}
		return true;
	}
	
	public static boolean validateReviewID(int reviewID) {
		if (reviewID <= 0) {
			return false;
		}
		return true;
	}
}
