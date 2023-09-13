package Main;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Security.SHA;
import Server.IServer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.TitledBorder;

import Game.MainFrame;

import javax.swing.border.EtchedBorder;

public class Profile extends JFrame {

	private JPanel contentPane;
	private int port;
	private WaitRoom waitRoom;
	private SHA sha = new SHA();
	ServerSocket serverRoom;
	private IServer server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public void run() {
				try {
					ArrayList infor = new ArrayList<>();
					infor.add("username");
					infor.add(new SHA().encryptPassword("password"));
					infor.add("email");
					infor.add(0);
					infor.add(0);
					infor.add(0);
					Profile frame = new Profile(infor);
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
	public Profile(final ArrayList infor) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 549, 392);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 222, 173));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);
		
		try {
		server = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JLabel lblNewLabel_2 = new JLabel("Battleship");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setForeground(new Color(0, 0, 51));
		lblNewLabel_2.setFont(new Font("Stencil Std", Font.PLAIN, 40));
		lblNewLabel_2.setBounds(10, 11, 513, 56);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel = new JLabel("Account information");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblNewLabel.setBounds(10, 63, 255, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Username:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(10, 110, 85, 36);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Email:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(10, 157, 78, 36);
		contentPane.add(lblNewLabel_1_1);
		
		JLabel username = new JLabel(String.valueOf(infor.get(0)));
		username.setFont(new Font("Tahoma", Font.PLAIN, 15));
		username.setBounds(169, 110, 176, 36);
		contentPane.add(username);
		
		JLabel email = new JLabel(String.valueOf(infor.get(2)));
		email.setFont(new Font("Tahoma", Font.PLAIN, 15));
		email.setBounds(169, 157, 176, 36);
		contentPane.add(email);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("Number of matches: ");
		lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1_1.setBounds(10, 204, 153, 36);
		contentPane.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("Win:");
		lblNewLabel_1_1_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1_2.setBounds(10, 251, 78, 36);
		contentPane.add(lblNewLabel_1_1_2);
		
		JLabel matchCount = new JLabel(String.valueOf(infor.get(3)));
		matchCount.setFont(new Font("Tahoma", Font.PLAIN, 15));
		matchCount.setBounds(169, 204, 176, 36);
		contentPane.add(matchCount);
		
		int win = (int)infor.get(4);
		int total = (int)infor.get(3);
		double rate = Math.round((double)win*100/total*100.0)/100.0;
		JLabel winRate = new JLabel(String.valueOf(rate) + "%");
		winRate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		winRate.setBounds(169, 251, 176, 36);
		contentPane.add(winRate);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("Score: ");
		lblNewLabel_1_1_2_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1_2_1.setBounds(10, 298, 78, 36);
		contentPane.add(lblNewLabel_1_1_2_1);
		
		JLabel score = new JLabel(String.valueOf(infor.get(5)));
		score.setFont(new Font("Tahoma", Font.PLAIN, 15));
		score.setBounds(169, 298, 176, 36);
		contentPane.add(score);
		
		Random rand = new Random();
		port = rand.nextInt(9998);
		setVisible(false);
		while (true) {
			try {
				serverRoom = new ServerSocket(port);
				break;
			} catch (IOException e2) {
				port = rand.nextInt(9998);
			}
		}
		
		JButton btnCreateRoom = new JButton("Create room");
		btnCreateRoom.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					server.Addroom(port, infor.get(0)+"");
					setVisible(false);
					waitRoom = new WaitRoom(serverRoom, port, infor);
					waitRoom.setVisible(true);
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnCreateRoom.setFocusable(false);
		btnCreateRoom.setBounds(387, 166, 126, 23);
		contentPane.add(btnCreateRoom);
		
		JButton btnQuickGame = new JButton("Random match");
		btnQuickGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int port = server.FindMatchRandom(infor);
					if (port != -1) {
						Socket socket = new Socket(InetAddress.getLocalHost(), port);
						MainFrame frame = new MainFrame(socket, infor);
						frame.setVisible(true);
						setVisible(false);
					}
					else {
						JOptionPane.showConfirmDialog(null, "No match room found", "", JOptionPane.PLAIN_MESSAGE);
					}
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnQuickGame.setFocusable(false);
		btnQuickGame.setBounds(387, 78, 126, 23);
		contentPane.add(btnQuickGame);
		
		JButton btnTopPlayer = new JButton("Top players");
		btnTopPlayer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.IsRunning();
					TopPlayers frame = new TopPlayers(infor);
					frame.setVisible(true);
					setVisible(false);
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnTopPlayer.setFocusable(false);
		btnTopPlayer.setBounds(387, 213, 126, 23);
		contentPane.add(btnTopPlayer);
		
		JButton btnFindRoom = new JButton("Find room");
		btnFindRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.IsRunning();
					ListRoom frame = new ListRoom(infor);
					frame.setVisible(true);
					setVisible(false);
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnFindRoom.setFocusable(false);
		btnFindRoom.setBounds(387, 123, 126, 23);
		contentPane.add(btnFindRoom);
		
		JButton btnChangePass = new JButton("Change pass");
		btnChangePass.addActionListener(new ActionListener() {
			@SuppressWarnings({ "deprecation", "unchecked" })
			public void actionPerformed(ActionEvent e) {
				try {
					server.IsRunning();
					JPasswordField pass = new JPasswordField();
					JPasswordField newpass = new JPasswordField();
					JPasswordField verifypass = new JPasswordField();
					Object txt[] = {
						"Password",pass,
						"\nNew password", newpass,
						"\nVerify password", verifypass
					};
					int choose = 0;
					while (choose == 0) {
						choose = JOptionPane.showConfirmDialog(null, txt, "Change password", JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
						if (choose != 0) break;
						if (sha.encryptPassword(pass.getText()).equals(infor.get(1).toString())) {
							if (newpass.getText().length() > 8) {
								if (newpass.getText().equals(verifypass.getText())) {
									if (!sha.encryptPassword(newpass.getText()).equals(infor.get(1)+"")) {
										try {
											String notification = server.ChangePass(infor.get(0)+"", sha.encryptPassword(newpass.getText()));
											if (notification.contains("successfully")) {
												infor.set(1, sha.encryptPassword(newpass.getText()));
											}
											JOptionPane.showConfirmDialog(null, notification, "", JOptionPane.PLAIN_MESSAGE);
										} catch (Exception e2) {
											e2.printStackTrace();
											JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
										}
										break;
									}
									else {
										JOptionPane.showConfirmDialog(null, "Old-password and New-password are the same", "", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
									}
								}
								else {
									JOptionPane.showConfirmDialog(null, "New-password and Verify-password are not the same", "", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
								}
							}
							else {
								JOptionPane.showConfirmDialog(null, "New-password must be longer than 8 characters", "", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
							}
						}
						else {
							JOptionPane.showConfirmDialog(null, "Password is incorrect", "", JOptionPane.PLAIN_MESSAGE, JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
				}
			}
		});
		btnChangePass.setFocusable(false);
		btnChangePass.setBounds(387, 260, 126, 23);
		contentPane.add(btnChangePass);
		
		JButton btnLogOut = new JButton("Log out");
		btnLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				Home home = new Home();
				home.setVisible(true);
			}
		});
		btnLogOut.setFocusable(false);
		btnLogOut.setBounds(387, 307, 126, 23);
		contentPane.add(btnLogOut);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 222, 173));
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Toolbar", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(378, 51, 145, 294);
		contentPane.add(panel);
	}
}
