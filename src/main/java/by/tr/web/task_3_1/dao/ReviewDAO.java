package by.tr.web.task_3_1.dao;

import java.util.List;

import by.tr.web.task_3_1.domain.Movie;
import by.tr.web.task_3_1.domain.Review;
import by.tr.web.task_3_1.domain.User;

public interface ReviewDAO {

	void createReview(Review review);

	Review readReview(User user, Movie movie);

	List<Review> readReviewsByUser(User user);

	List<Review> readMovieReviews(Movie movie);

	void deleteReview(Review review);
}
