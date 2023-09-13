package Main;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Game.MainFrame;
import Server.IServer;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

public class ListRoom extends JFrame implements WindowListener{

	private JPanel contentPane;
	private JTable table;
	private JButton btnRefresh;
	private JButton btnSearch;
	private Socket socket;
	@SuppressWarnings("rawtypes")
	private ArrayList infor;
	private IServer server;

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("rawtypes")
	public ListRoom(ArrayList infor) {
		this.infor = infor;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 311, 411);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.addWindowListener(this);
		
		try {
		server = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 52, 275, 309);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFont(new Font("Monospaced", Font.PLAIN, 15));
		scrollPane.setViewportView(table);
		table.setRowHeight(30);
		setTable();
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (server == null) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
				else {
					if (e.getClickCount() == 2) {
						int port = Integer.valueOf((String)table.getValueAt(table.getSelectedRow(), 0));
						
						try {
							if (server.RoomExist(port)) {
								socket = new Socket(InetAddress.getLocalHost(), port);
								startGame();
							}
							else {
								showError();
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			}

			private void showError() {
				setTable();
				JOptionPane.showConfirmDialog(null, "Not found", "",JOptionPane.CLOSED_OPTION);
			}
		});
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTable();
			}
		});
		btnRefresh.setFocusable(false);
		btnRefresh.setBounds(10, 11, 95, 33);
		contentPane.add(btnRefresh);
		
		btnSearch = new JButton("Enter ID");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.IsRunning();
					String txt = JOptionPane.showInputDialog(null, "Enter the number of room: ", "", JOptionPane.INFORMATION_MESSAGE);
					if (txt != null) {
						int port = Integer.valueOf(txt);
						try {
							if (server.RoomExist(port)) {
								socket = new Socket(InetAddress.getLocalHost(), port);
								startGame();
							}
							else {
								showError();
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
			}

			private void showError() {
				setTable();
				JOptionPane.showConfirmDialog(null, "Not found", "",JOptionPane.CLOSED_OPTION);
			}
		});
		btnSearch.setFocusable(false);
		btnSearch.setBounds(190, 11, 95, 33);
		contentPane.add(btnSearch);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setTable() {
		Vector col = new Vector<>();
		col.add("ID");
		col.add("Name");
		
		Vector row = new Vector<>();
		try {
			row = server.GetListRoom();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		table.setModel(new DefaultTableModel(row, col) {
			
			@Override
			public boolean isCellEditable(int row, int column) {       
				return false;		// disable editing table
			}
		});	
		
	}
	
	private void startGame() {
		setVisible(false);
		MainFrame frame = new MainFrame(socket, infor);
		frame.setVisible(true);
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
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
