package Game;



import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.Socket;
import java.rmi.Naming;
import java.util.ArrayList;

import Main.Profile;
import Server.IServer;

public class MainFrame extends JFrame implements WindowListener{

	MainPanel mp;
	@SuppressWarnings("rawtypes")
	ArrayList infor;
	Socket socket = null;
	private IServer server;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("rawtypes")
	public MainFrame(Socket socket, ArrayList infor) {
		this.socket = socket;
		this.infor = infor;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle(infor.get(0)+"");
		setBounds(0, 0, 900, 540);
		addWindowListener(this);
		setLocationRelativeTo(null);
		setVisible(true);
		
		try {
		server = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mp = new MainPanel(socket, infor, this);
		mp.setLayout(null);
		setContentPane(mp);
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void windowClosing(WindowEvent e) {
		try {
			String s = server.UpdateMatchResult(infor.get(0)+"", 0, infor);
			if (s.equals("OK")) {
				infor.set(3, ((int)infor.get(3))+1);
			}
			Profile frame = new Profile(infor);
			frame.setVisible(true);
			socket.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
