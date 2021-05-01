package it.polito.tdp.nobel.db;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnect 
{
	static private final String jdbcUrl = "jdbc:mariadb://localhost/esamitriennale";
	static private final String username = "root";
	static private final String password = "root";
	static private final HikariDataSource dataSource;
	
	static 
	{
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(username);
		config.setPassword(password);
		
		config.addDataSourceProperty("cachePrepStmts", true);
		config.addDataSourceProperty("prepStmtChacheSize", 250);
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		dataSource = new HikariDataSource(config);
	}
	
	public static Connection getConnection() 
	{
		try 
		{
			Connection connection = dataSource.getConnection();
			return connection;
		} 
		catch (SQLException sqle) 
		{
			sqle.printStackTrace();
			throw new RuntimeException("Cannot get a connection to: " + jdbcUrl, sqle);
		}
	}
}