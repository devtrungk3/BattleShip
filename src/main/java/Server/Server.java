package Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;

import Connect.Connect;
import Security.SHA;

public class Server extends UnicastRemoteObject implements IServer{
	
	private Connect connect = new Connect();
	private Connection conn = connect.getConnection();

	protected Server() throws RemoteException {
		super();
	}
	
	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(9999);
			Naming.rebind("rmi://localhost:9999/server", new Server());
			JFrame frame = new JFrame();
			frame.setSize(300, 100);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			
			JLabel label = new JLabel("The server is running");
			label.setHorizontalAlignment(JLabel.CENTER);
			frame.add(label);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void IsRunning() throws RemoteException {}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ArrayList Login(String username, String password) throws RemoteException {
		ArrayList infor = new ArrayList();
		try {
			String sql = "SELECT acc.username, password, email, matchCount, win, score FROM `acc` INNER JOIN `match` ON acc.username = match.username WHERE acc.username = ? AND password = ?";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setString(1, username);
			stm.setString(2, password);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				infor.add(rs.getString(1));
				infor.add(rs.getString(2));
				infor.add(rs.getString(3));
				infor.add(rs.getInt(4));
				infor.add(rs.getInt(5));
				infor.add(rs.getInt(6));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infor;
	}
	
	@Override
	public String Signup(String username, String password, String email) throws RemoteException {
		int count = 0;
		try {
			
			String sql = "SELECT COUNT(username) FROM `acc` WHERE username = ?";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setString(1, username);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count != 0) {
				return "Username already exists";
			}
			
			
			
			
			sql = "SELECT COUNT(username) FROM `acc` WHERE email = ?";
			stm = conn.prepareStatement(sql);
			stm.setString(1, email);
			rs = stm.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count != 0) {
				return "Email already exists";
			}
			
			
			
			
			sql = "INSERT INTO `acc` VALUES (?,?,?)";
			stm = conn.prepareStatement(sql);
			stm.setString(1, username);
			stm.setString(2, password);
			stm.setString(3, email);
			stm.execute();
			sql = "INSERT INTO `match` VALUES (?,?,?,?)";
			stm = conn.prepareStatement(sql);
			stm.setString(1, username);
			stm.setInt(2, 0);
			stm.setInt(3, 0);
			stm.setInt(4, 0);
			stm.execute();
			return "Sign up success";
		} catch (Exception e) {
			e.printStackTrace();
			return "Sign up failed";
		}
	}

	@Override
	public void Addroom(int port, String username) throws RemoteException {
		try {
			String sql = "INSERT INTO `waitroom` VALUES(?,?)";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setInt(1, port);
			stm.setString(2, username + "'s room");
			stm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int FindMatchRandom(ArrayList infor) throws RemoteException {
		try {
			String sql = "SELECT ID FROM `waitroom`";
			PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public String ChangePass(String username, String password) throws RemoteException {
		try {
			String sql = "UPDATE `acc` SET password=?  WHERE username=?";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setString(1, password);
			stm.setString(2, username);
			stm.execute();
			return "Password was successfully changed";
		} catch (SQLException e) {
			e.printStackTrace();
			return "Password change failed";
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Vector GetStanding(int choose, String username) throws RemoteException {
		
		String attribute = "";
		String sort = "";
		switch(choose) {
		case 0:
			attribute = " score ";
			sort = " win DESC, matchCount DESC ";
			break;
		case 1:
			attribute = " matchCount ";
			sort = " score DESC, win DESC ";
			break;
		case 2:
			attribute = " win ";
			sort = " score DESC, matchCount DESC ";
			break;
		}
		
		Vector row = new Vector();
		
		int top = 1;
		int yourTop = 0;
		
		try {
			
			String sql = "SELECT acc.username, " + attribute;
			sql += " FROM `acc` join `match` ON acc.username = match.username";
			sql += " ORDER BY " + attribute + " DESC, " + sort + " LIMIT 10;";
			PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
				Vector val = new Vector();
				val.add(top);
				val.add(rs.getString(1));
				if (rs.getString(1).equals(username)) yourTop = top;
				val.add(rs.getString(2));
				row.add(val);
				top++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		row.add(yourTop);
		return row;
	}

	@Override
	public boolean RoomExist(int id) throws RemoteException {
		try {
			String sql = "SELECT COUNT(ID) FROM `waitroom` WHERE ID = ?";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setInt(1, id);
			ResultSet rs = stm.executeQuery();
			int count = 0;
			while (rs.next()) {
				count = rs.getInt(1);
			}
			if (count != 0) return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Vector GetListRoom() throws RemoteException {
		Vector row = new Vector<>();
		try {
			String sql = "SELECT * FROM `waitroom`";
			PreparedStatement stm = conn.prepareStatement(sql);
			ResultSet rs = stm.executeQuery();
			while (rs.next()) {
				Vector val = new Vector<>();
				val.add(rs.getString(1));
				val.add(rs.getString(2));
				row.add(val);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return row;
	}

	@Override
	public void DeleteRoom(int id) throws RemoteException {
		try {
			String sql = "DELETE FROM `waitroom` WHERE ID = ?";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setInt(1, id);
			stm.execute();
		} catch (Exception e2) {
			e2.printStackTrace();
		}	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String UpdateMatchResult(String username, int win, ArrayList infor) throws RemoteException {
		try {
			String sql = "UPDATE `match` SET matchCount = matchCount+?, win = win+?, score = score+? WHERE username = ?;";
			PreparedStatement stm = conn.prepareStatement(sql);
			stm.setInt(1, 1);
			infor.set(3, ((int)infor.get(3))+1);
			stm.setInt(2, win);
			infor.set(4, ((int)infor.get(4))+win);
			if (win == 1) {
				stm.setInt(3, 10);
				infor.set(5, ((int)infor.get(5))+10);
			}
			else {
				stm.setInt(3, 0);
			}
			stm.setString(4, username);
			stm.execute();
			return "OK";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
