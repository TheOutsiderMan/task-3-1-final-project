package by.tr.web.task_3_1.service.util;

import by.tr.web.task_3_1.dao.connection_pool.ConnectionPool;
import by.tr.web.task_3_1.dao.exception.DAOException;
import by.tr.web.task_3_1.service.exception.ServiceException;

public final class ConnectionPoolInitializer {
	
	private final static ConnectionPoolInitializer initializer = new ConnectionPoolInitializer();
	
	private ConnectionPoolInitializer() {}
	
	public static ConnectionPoolInitializer getInitializer() {
		return initializer;
	}
	
	public void initializeConnectionPool() throws ServiceException {
		try {
			ConnectionPool.getInstance().initPoolData();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
	
	public void closeAllConnections() throws ServiceException {
		try {
			ConnectionPool.getInstance().dispose();
		} catch (DAOException e) {
			throw new ServiceException(e);
		}
	}
}
