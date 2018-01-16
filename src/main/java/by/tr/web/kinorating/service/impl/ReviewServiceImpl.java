package by.tr.web.kinorating.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.dao.DAOAbstractFactory;
import by.tr.web.kinorating.dao.ReviewDAO;
import by.tr.web.kinorating.dao.exception.DAOException;
import by.tr.web.kinorating.dao.mysql.MySQLDAOFactory;
import by.tr.web.kinorating.domain.Movie;
import by.tr.web.kinorating.domain.Review;
import by.tr.web.kinorating.domain.User;
import by.tr.web.kinorating.service.ReviewService;
import by.tr.web.kinorating.service.exception.ServiceException;
import by.tr.web.kinorating.service.validation.MovieValidator;
import by.tr.web.kinorating.service.validation.ReviewValidator;
import by.tr.web.kinorating.service.validation.UserValidator;

public class ReviewServiceImpl implements ReviewService {

	private static final String PROBLEM_WITH_DELETING_REVIEW = "Problem with deleting review";
	private static final String PROBLEM_WITH_EDITING_REVIEW = "Problem with editing review";
	private static final String PROBLEM_WITH_READING_REVIEWS = "Problem with reading reviews";
	private static final String PROBLEM_WITH_ADDING_REVIEW = "Problem with adding review";

	private static final Logger logger = LogManager.getLogger(ReviewServiceImpl.class);

	@Override
	public boolean addReview(String userLogin, int movieID, String text) throws ServiceException {
		if (!UserValidator.validateLogin(userLogin) || !MovieValidator.validateMovieId(movieID)) {
			return false;
		}
		if (!ReviewValidator.validateTextReview(text)) {
			return false;
		}
		Review review = new Review();
		User author = new User();
		author.setLogin(userLogin);
		Movie movie = new Movie();
		movie.setId(movieID);
		review.setAuthor(author);
		review.setReviewedMovie(movie);
		review.setTextReview(text);
		Date additionDate = new Date();
		review.setAdditionDate(additionDate);
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ReviewDAO reviewDAO = factory.getReviewDAO();
		boolean added = false;
		try {
			added = reviewDAO.createReview(review);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_ADDING_REVIEW, e);
			throw new ServiceException(PROBLEM_WITH_ADDING_REVIEW, e);
		}
		return added;
	}

	@Override
	public List<Review> showAllReviews() throws ServiceException {
		List<Review> reviews = null;
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ReviewDAO reviewDAO = factory.getReviewDAO();
		try {
			reviews = reviewDAO.readAllReviews();
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_REVIEWS, e);
			throw new ServiceException(PROBLEM_WITH_READING_REVIEWS, e);
		}
		return reviews;
	}

	@Override
	public List<Review> showUserReview(String login) throws ServiceException {
		List<Review> reviews = null;
		if (!UserValidator.validateLogin(login)) {
			return Collections.emptyList();
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ReviewDAO reviewDAO = factory.getReviewDAO();
		User user = new User();
		user.setLogin(login);
		try {
			reviewDAO.readReviewsByUser(user);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_REVIEWS, e);
			throw new ServiceException(PROBLEM_WITH_READING_REVIEWS, e);
		}
		return reviews;
	}

	@Override
	public List<Review> showMovieReviws(int movieID) throws ServiceException {
		List<Review> reviews = null;
		if (!MovieValidator.validateMovieId(movieID)) {
			return Collections.emptyList();
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ReviewDAO reviewDAO = factory.getReviewDAO();
		Movie movie = new Movie();
		movie.setId(movieID);
		try {
			reviewDAO.readMovieReviews(movie);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_READING_REVIEWS, e);
			throw new ServiceException(PROBLEM_WITH_READING_REVIEWS, e);
		}
		return reviews;
	}

	@Override
	public boolean editReview(int reviewID, String newText) throws ServiceException {
		if (!ReviewValidator.validateReviewID(reviewID) || !ReviewValidator.validateTextReview(newText)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ReviewDAO reviewDAO = factory.getReviewDAO();
		boolean updated = false;
		Review review = new Review();
		review.setId(reviewID);
		try {
			updated = reviewDAO.updateReview(review, newText);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_EDITING_REVIEW, e);
			throw new ServiceException(PROBLEM_WITH_EDITING_REVIEW, e);
		}
		return updated;
	}

	@Override
	public boolean deleteReview(int reviewID) throws ServiceException {
		if (!ReviewValidator.validateReviewID(reviewID)) {
			return false;
		}
		DAOAbstractFactory factory = MySQLDAOFactory.getInstance();
		ReviewDAO reviewDAO = factory.getReviewDAO();
		Review review = new Review();
		review.setId(reviewID);
		boolean deleted = false;
		try {
			deleted = reviewDAO.deleteReview(review);
		} catch (DAOException e) {
			logger.error(PROBLEM_WITH_DELETING_REVIEW, e);
			throw new ServiceException(PROBLEM_WITH_DELETING_REVIEW, e);
		}
		return deleted;
	}

}
