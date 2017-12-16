package by.tr.web.kinorating.dao.mysql.impl;

import java.util.List;

import by.tr.web.kinorating.dao.ReviewDAO;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Review;
import by.tr.web.kinorating.domain.User;

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
	public List<Review> readAllReviews() {

		return null;
	}

	@Override
	public void deleteReview(Review review) {
		
	}
	
	

}
