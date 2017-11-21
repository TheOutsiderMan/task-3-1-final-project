package by.tr.web.task_3_1.service;

import by.tr.web.task_3_1.service.impl.MoviesServiceImpl;
import by.tr.web.task_3_1.service.impl.ReviewsServiceImpl;
import by.tr.web.task_3_1.service.impl.UserServiceImpl;

public final class ServiceFactory {

	private static final ServiceFactory factory = new ServiceFactory();
	private final UserService userService = new UserServiceImpl();
	private final MoviesService moviesService = new MoviesServiceImpl();
	private final ReviewsService reviewsService = new ReviewsServiceImpl();

	private ServiceFactory() {

	}
	
	public static ServiceFactory getInstance() {
		return factory;
	}

	public UserService getUserService() {
		return userService;
	}

	public MoviesService getMoviesService() {
		return moviesService;
	}

	public ReviewsService getReviewsService() {
		return reviewsService;
	}
	
	
}
