package by.tr.web.task_3_1.dao.connection_pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import by.tr.web.task_3_1.dao.exception.DAOException;

public final class ConnectionPool {
	
	private static ConnectionPool connectionPool = new ConnectionPool();
	private BlockingQueue<Connection> connectionQueue;
	private BlockingQueue<Connection> givenAwayConQueue;
	
	private String driverName;
	private String url;
	private String user;
	private String password;
	private int poolSize;
	
	private ConnectionPool() {
		DBResourceManager resourceManager = DBResourceManager.getInstance();
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
			throw new DAOException(e);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	public void dispose() throws SQLException {
		closeConnectionsQueue(connectionQueue);
		closeConnectionsQueue(givenAwayConQueue);
	}
	
	
	private void closeConnectionsQueue(BlockingQueue<Connection> queue) throws SQLException {
		Connection connection;
		while ((connection = queue.poll()) != null) {
			if(!connection.getAutoCommit()) {
				connection.commit();
			}
			connection.close();
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
