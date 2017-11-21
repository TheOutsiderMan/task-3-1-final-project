package by.tr.web.task_3_1.service;

import by.tr.web.task_3_1.domain.Status;
import by.tr.web.task_3_1.domain.User;

public interface UserService {

	boolean authenticateUserByLogin(String login, String password);

	boolean authenticateUserByEmail(String email, String password);

	void registerUser(User user);

	void banUser(User user, Status BANNED);

	void changeRating(User user, double rating);
}
