package by.tr.web.task_3_1.dao;

import by.tr.web.task_3_1.domain.User;

public interface UserDAO {

	void createUser(User user);

	User readUserByName(String name);

	User readUserByEmail(String email);

	void updateUser(User user);

	void delete(User user);
}
