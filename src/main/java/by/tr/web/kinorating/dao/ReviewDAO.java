package by.tr.web.kinorating.dao;

import java.util.List;

import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Review;
import by.tr.web.kinorating.domain.User;

public interface ReviewDAO {

	boolean createReview(Review review) throws DAOException;
	
	Review readReview(User user, Movie movie) throws DAOException;

	List<Review> readReviewsByUser(User user) throws DAOException;

	List<Review> readMovieReviews(Movie movie) throws DAOException;
	
	List<Review> readAllReviews() throws DAOException;
	
	boolean updateReview(Review review, String newText) throws DAOException;

	boolean deleteReview(Review review) throws DAOException;
}
