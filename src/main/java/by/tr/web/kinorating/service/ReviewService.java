package by.tr.web.kinorating.service;

import java.util.List;

import by.tr.web.kinorating.domain.Review;

public interface ReviewService {
	
	List<Review> getAllReviews();
	
	List<Review> getUserReview(String login);
	
	List<Review> getMovieReviws(String title);
	
	List<Review> deleteReview(String login);
	
}
