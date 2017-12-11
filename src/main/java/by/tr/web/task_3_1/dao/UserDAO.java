package by.tr.web.task_3_1.dao;

import by.tr.web.task_3_1.domain.User;

public interface UserDAO {

	boolean createUser(User user);

	User readUserByLogin(String login);

	User readUserByEmail(String email);

	void updateUser(User user);

	void delete(User user);
}
