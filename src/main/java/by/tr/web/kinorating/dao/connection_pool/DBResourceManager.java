package by.tr.web.kinorating.dao.connection_pool;

import java.util.ResourceBundle;

public class DBResourceManager {

	private static final String DB_PROPERTIES = "by.tr.web.kinorating.dao.connection_pool.db";

	private static final DBResourceManager resourceManager = new DBResourceManager();

	private ResourceBundle resourceBundle = ResourceBundle.getBundle(DB_PROPERTIES);

	private DBResourceManager() {
	}

	public static DBResourceManager getInstance() {
		return resourceManager;
	}

	public String getValue(String key) {
		return resourceBundle.getString(key);
	}
}