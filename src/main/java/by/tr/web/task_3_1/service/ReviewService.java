package by.tr.web.task_3_1.service;

import java.util.List;

import by.tr.web.task_3_1.domain.Review;

public interface ReviewService {
	
	List<Review> getAllReviews();
	
	List<Review> getUserReview(String login);
	
	List<Review> getMovieReviws(String title);
	
	List<Review> deleteReview(String login);
	
}
