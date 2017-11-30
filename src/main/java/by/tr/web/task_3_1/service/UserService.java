package by.tr.web.task_3_1.service;

import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;

public interface UserService {

	User authenticateUserByLogin(String login, String password);

	User authenticateUserByEmail(String email, String password);

	boolean registerUser(String login, String email, String password);

	void banUser(User user, Status BANNED);

	void changeRating(User user, double rating);
}
