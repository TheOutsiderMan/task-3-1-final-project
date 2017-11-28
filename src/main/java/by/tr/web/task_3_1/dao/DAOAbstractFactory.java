package by.tr.web.task_3_1.dao;

public interface DAOAbstractFactory {
	
	UserDAO getUserDAO();
	MovieDAO getMovieDAO();
	ReviewDAO getReviewDAO();
		
}
