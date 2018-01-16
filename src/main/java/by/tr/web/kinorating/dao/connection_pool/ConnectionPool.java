package by.tr.web.kinorating.dao.connection_pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.tr.web.kinorating.controller.FrontController;
import by.tr.web.kinorating.dao.exception.DAOException;

public final class ConnectionPool {
	
	private static final String PROBLEM_WITH_COMMITING_DB_DATA_CHANGES = "Problem with commiting DB data changes";

	private static final String PROBLEM_WITH_CONNECTION_TO_DB = "Problem with connection to DB";

	private static final String PROBLEM_WITH_LOADING_DB_DRIVER = "Problem with loading DB driver";

	private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
	
	private static ConnectionPool connectionPool = new ConnectionPool();
	
	private DBResourceManager resourceManager = DBResourceManager.getInstance();
	
	private BlockingQueue<Connection> connectionQueue;
	private BlockingQueue<Connection> givenAwayConQueue;
	
	private String driverName;
	private String url;
	private String user;
	private String password;
	private int poolSize;
	
	private ConnectionPool() {
		this.driverName = resourceManager.getValue(DBParametres.DB_DRIVER);
		this.url = resourceManager.getValue(DBParametres.DB_URL);
		this.user = resourceManager.getValue(DBParametres.DB_USER);
		this.password = resourceManager.getValue(DBParametres.DB_PASSWORD);
		this.poolSize = Integer.valueOf(resourceManager.getValue(DBParametres.DB_POOL_SIZE));
	}
	
	public static ConnectionPool getInstance() {
		return connectionPool;
	}
	
	public void initPoolData() throws DAOException {
		try {
			Class.forName(driverName);
			givenAwayConQueue = new ArrayBlockingQueue<Connection>(poolSize);
			connectionQueue = new ArrayBlockingQueue<Connection>(poolSize);
			for (int i = 0; i < poolSize; i++) {
				Connection connection = DriverManager.getConnection(url, user, password);
				connectionQueue.add(connection);
			}
		} catch (ClassNotFoundException e) {
			logger.error(PROBLEM_WITH_LOADING_DB_DRIVER, e);
			throw new DAOException(PROBLEM_WITH_LOADING_DB_DRIVER, e);
		} catch (SQLException e) {
			logger.error(PROBLEM_WITH_CONNECTION_TO_DB, e);
			throw new DAOException(PROBLEM_WITH_CONNECTION_TO_DB, e);
		}
	}
	
	public void dispose() throws DAOException {
		closeConnectionsQueue(connectionQueue);
		closeConnectionsQueue(givenAwayConQueue);
	}
	
	
	private void closeConnectionsQueue(BlockingQueue<Connection> queue) throws DAOException {
		Connection connection;
		while ((connection = queue.poll()) != null) {
			try {
				if(!connection.getAutoCommit()) {
					connection.commit();
				}
				connection.close();
			} catch (SQLException e) {
				logger.error(PROBLEM_WITH_COMMITING_DB_DATA_CHANGES, e);
				throw new DAOException(PROBLEM_WITH_COMMITING_DB_DATA_CHANGES, e);
			}
			
		}
	}
	
	public Connection takeConnection() throws InterruptedException {
		Connection connection;
		connection = connectionQueue.take();
		givenAwayConQueue.add(connection);
		return connection;
	}
	
	public void releaseConnection(Connection connection) {
		connectionQueue.add(connection);
		givenAwayConQueue.remove(connection);
	}
}
