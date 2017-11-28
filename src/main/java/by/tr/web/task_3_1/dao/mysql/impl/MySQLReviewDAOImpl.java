package by.tr.web.task_3_1.dao.mysql.impl;

import java.util.List;

import by.tr.web.task_3_1.dao.ReviewDAO;
import by.tr.web.task_3_1.domain.Movie;
import by.tr.web.task_3_1.domain.Review;
import by.tr.web.task_3_1.domain.User;

public class MySQLReviewDAOImpl implements ReviewDAO {

	@Override
	public void createReview(Review review) {
		
	}

	@Override
	public Review readReview(User user, Movie movie) {

		return null;
	}

	@Override
	public List<Review> readReviewsByUser(User user) {

		return null;
	}

	@Override
	public List<Review> readMovieReviews(Movie movie) {

		return null;
	}

	@Override
	public void deleteReview(Review review) {
		
	}

}
