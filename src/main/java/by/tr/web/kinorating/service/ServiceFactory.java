package by.tr.web.kinorating.service;

import by.tr.web.kinorating.service.impl.MovieServiceImpl;
import by.tr.web.kinorating.service.impl.ReviewServiceImpl;
import by.tr.web.kinorating.service.impl.UserServiceImpl;

public final class ServiceFactory {

	private static final ServiceFactory factory = new ServiceFactory();
	private final UserService userService = new UserServiceImpl();
	private final MovieService moviesService = new MovieServiceImpl();
	private final ReviewService reviewsService = new ReviewServiceImpl();

	private ServiceFactory() {

	}

	public static ServiceFactory getInstance() {
		return factory;
	}

	public UserService getUserService() {
		return userService;
	}

	public MovieService getMoviesService() {
		return moviesService;
	}

	public ReviewService getReviewsService() {
		return reviewsService;
	}

}