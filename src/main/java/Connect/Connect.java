package Connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
	
	private String host = "localhost";
	private String userName = "root";
	private String password = "";
	private String DatabaseName = "battleship";
	private Connection conn = null;
	
	public Connect() {
		try{ 
			   String url = "jdbc:mysql://"+host+"/" + DatabaseName;
			   Class.forName("com.mysql.cj.jdbc.Driver");
			   conn = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return conn;
	}
	
}
