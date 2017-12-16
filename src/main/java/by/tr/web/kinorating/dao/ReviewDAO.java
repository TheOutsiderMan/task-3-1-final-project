package by.tr.web.kinorating.dao;

import java.util.List;

import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Review;
import by.tr.web.kinorating.domain.User;

public interface ReviewDAO {

	void createReview(Review review);

	Review readReview(User user, Movie movie);

	List<Review> readReviewsByUser(User user);

	List<Review> readMovieReviews(Movie movie);
	
	List<Review> readAllReviews();

	void deleteReview(Review review);
}
