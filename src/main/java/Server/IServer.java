package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

@SuppressWarnings("rawtypes")
public interface IServer extends Remote{
	public void IsRunning() throws RemoteException;
	public ArrayList Login(String username, String password) throws RemoteException;
	public String Signup(String username, String password, String email) throws RemoteException;
	public void Addroom(int port, String username) throws RemoteException;
	public int FindMatchRandom(ArrayList infor) throws RemoteException;
	public String ChangePass(String username, String password) throws RemoteException;
	public Vector GetStanding(int choose, String username) throws RemoteException;
	public boolean RoomExist(int id) throws RemoteException;
	public Vector GetListRoom() throws RemoteException;
	public void DeleteRoom(int id) throws RemoteException;
	public String UpdateMatchResult(String username, int win, ArrayList infor) throws RemoteException;
}
