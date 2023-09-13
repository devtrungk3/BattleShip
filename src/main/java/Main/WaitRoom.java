package Main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Game.MainFrame;
import Server.IServer;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.util.ArrayList;

public class WaitRoom extends JFrame implements WindowListener{

	private JPanel contentPane;
	private ServerSocket server;
	@SuppressWarnings("rawtypes")
	private ArrayList infor;
	private int port;
	private IServer iserver;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public void run() {
				try {
					ArrayList infor = new ArrayList<>();
					infor.add("username");
					infor.add("password");
					infor.add("email");
					infor.add(0);
					infor.add(0);
					ServerSocket server = new ServerSocket(0);
					WaitRoom frame = new WaitRoom(server, 0, infor);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("rawtypes")
	public WaitRoom(final ServerSocket server, final int port, final ArrayList infor) {
		
		this.server = server;
		this.infor = infor;
		this.port = port;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 178);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		addWindowListener(this);
		setLocationRelativeTo(null);
		
		try {
		iserver = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JLabel lblNewLabel = new JLabel("Room " + port);
		lblNewLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 414, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Waiting for another player...");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Monospaced", Font.BOLD, 23));
		lblNewLabel_1.setBounds(10, 58, 414, 70);
		contentPane.add(lblNewLabel_1);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Socket socket = server.accept();
					MainFrame frame = new MainFrame(socket, infor);
					frame.setVisible(true);
					
					try {
					iserver.DeleteRoom(port);
					} catch (Exception e) {
						e.printStackTrace();
					}
					setVisible(false);

				} catch (IOException ex) {
					ex.printStackTrace();
					try {
						server.close();
						System.out.println("Server is close!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		t.start();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			iserver.DeleteRoom(port);
			server.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Profile frame = new Profile(infor);
		frame.setVisible(true);
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
