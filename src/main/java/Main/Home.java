package Main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.border.LineBorder;

import Check.CheckSignup;
import Security.SHA;
import Server.IServer;

import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.Naming;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class Home extends JFrame {

	private JPanel contentPane;
	private JTextField txtUN;	// text user name
	private JPasswordField txtPW;	// text password
	private JTextField txtUNS;	// text user name sign up
	private JPasswordField txtPWS;	// text password sign up
	private JPasswordField txtRPWU;	// text re-password sign up
	private JTextField txtEmail;
	private SHA sha = new SHA();
	private IServer server;
	private Clip clip;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
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
	public Home() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 595, 411);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setLayout(null);
		
		File file = new File("SongAOV.wav");
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audio);
			clip.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
		server = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// sign up
		final JPanel signupForm = new JPanel();
		signupForm.setBorder(new LineBorder(new Color(0, 0, 0)));
		signupForm.setBackground(SystemColor.menu);
		signupForm.setBounds(190, 75, 196, 280);
		contentPane.add(signupForm);
		signupForm.setLayout(null);
		signupForm.setVisible(false);
		
		JLabel lblNewLb = new JLabel("Sign up");
		lblNewLb.setFont(new Font("Monospaced", Font.BOLD, 20));
		lblNewLb.setBounds(0, 0, 196, 32);
		lblNewLb.setHorizontalAlignment(SwingConstants.CENTER);
		signupForm.add(lblNewLb);
		
		txtUNS = new JTextField();
		txtUNS.setColumns(10);
		txtUNS.setBounds(10, 57, 176, 20);
		signupForm.add(txtUNS);
		
		JLabel lblNewLabel_1_2 = new JLabel("Username");
		lblNewLabel_1_2.setBounds(10, 43, 176, 14);
		signupForm.add(lblNewLabel_1_2);
		
		txtPWS = new JPasswordField();
		txtPWS.setColumns(10);
		txtPWS.setBounds(10, 102, 176, 20);
		signupForm.add(txtPWS);
		
		JLabel lblNewLabel_1_3 = new JLabel("Password");
		lblNewLabel_1_3.setBounds(10, 88, 176, 14);
		signupForm.add(lblNewLabel_1_3);
		
		txtRPWU = new JPasswordField();
		txtRPWU.setColumns(10);
		txtRPWU.setBounds(10, 147, 176, 20);
		signupForm.add(txtRPWU);
		
		JLabel lblNewLabel_1_4 = new JLabel("Re-Password");
		lblNewLabel_1_4.setBounds(10, 133, 176, 14);
		signupForm.add(lblNewLabel_1_4);
		
		
		
		JButton btnSignup = new JButton("Sign up");
		btnSignup.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				CheckSignup check = new CheckSignup(txtUNS.getText(), txtPWS.getText(), txtRPWU.getText(), txtEmail.getText());
				if (check.check()) {
					try {
						String notification = server.Signup(txtUNS.getText(), sha.encryptPassword(txtPWS.getText()), txtEmail.getText());
						if (notification.length() != 0) {
							JOptionPane.showConfirmDialog(null, notification, "", JOptionPane.PLAIN_MESSAGE);
						}
					} catch (Exception e2) {
						e2.printStackTrace();
						JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
					}
				}
			}
		});
		btnSignup.setFont(new Font("Monospaced", Font.BOLD, 12));
		btnSignup.setFocusable(false);
		btnSignup.setBackground(Color.WHITE);
		btnSignup.setBounds(54, 246, 89, 23);
		signupForm.add(btnSignup);
		
		final JCheckBox show_2 = new JCheckBox("Show password");
		show_2.setFocusable(false);
		show_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (show_2.isSelected()) {
					txtPWS.setEchoChar((char)0);
					txtRPWU.setEchoChar((char)0);
				}
				else {
					txtPWS.setEchoChar('•');
					txtRPWU.setEchoChar('•');
				}
			}
		});
		show_2.setHorizontalAlignment(SwingConstants.CENTER);
		show_2.setFocusable(false);
		show_2.setBounds(37, 168, 122, 23);
		signupForm.add(show_2);
		
		JLabel lblNewLabel_1_4_1 = new JLabel("Email");
		lblNewLabel_1_4_1.setBounds(10, 194, 176, 14);
		signupForm.add(lblNewLabel_1_4_1);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(10, 208, 176, 20);
		signupForm.add(txtEmail);
		
		
		
		// Login 
		final JPanel loginForm = new JPanel();
		loginForm.setBorder(new LineBorder(new Color(0, 0, 0)));
		loginForm.setBackground(SystemColor.menu);
		loginForm.setBounds(190, 102, 196, 232);
		contentPane.add(loginForm);
		loginForm.setLayout(null);
		loginForm.setVisible(false);
		
		JLabel lblNewLabel = new JLabel("Log in");
		lblNewLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
		lblNewLabel.setBounds(0, 0, 196, 32);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loginForm.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Username");
		lblNewLabel_1.setBounds(10, 68, 176, 14);
		loginForm.add(lblNewLabel_1);
		
		txtUN = new JTextField();
		txtUN.setBounds(10, 82, 176, 20);
		loginForm.add(txtUN);
		txtUN.setColumns(10);
		
		txtPW = new JPasswordField();
		txtPW.setEchoChar('•');
		txtPW.setColumns(10);
		txtPW.setBounds(10, 127, 176, 20);
		loginForm.add(txtPW);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password");
		lblNewLabel_1_1.setBounds(10, 113, 176, 14);
		loginForm.add(lblNewLabel_1_1);
		
		JButton btnLogin = new JButton("Log in");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					@SuppressWarnings({ "rawtypes", "deprecation" })
					ArrayList infor = server.Login(txtUN.getText(), sha.encryptPassword(txtPW.getText()));
					if (infor.size() != 0) {
						clip.stop();
						setVisible(false);
						Profile frame = new Profile(infor);
						frame.setVisible(true);
					}
					else {
						if (infor.size() == 0) JOptionPane.showConfirmDialog(null, "Wrong username or password", "", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
					}
				} catch (Exception e2) {
					e2.printStackTrace();
					JOptionPane.showConfirmDialog(null, "The server is close", "",JOptionPane.PLAIN_MESSAGE, JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnLogin.setFont(new Font("Monospaced", Font.BOLD, 12));
		btnLogin.setBackground(Color.WHITE);
		btnLogin.setBounds(54, 198, 89, 23);
		btnLogin.setFocusable(false);
		loginForm.add(btnLogin);
		
		final JCheckBox show = new JCheckBox("Show password");
		show.setFocusable(false);
		show.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (show.isSelected()) {
					txtPW.setEchoChar((char)0);
				}
				else {
					txtPW.setEchoChar('•');
				}
			}
		});
		show.setHorizontalAlignment(SwingConstants.CENTER);
		show.setBounds(37, 154, 122, 23);
		loginForm.add(show);
		
		JButton btnOpenSignup = new JButton("Sign up");
		btnOpenSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!signupForm.isVisible()) {
					signupForm.setVisible(true);
					if (loginForm.isVisible()) loginForm.setVisible(false);
				}
				else signupForm.setVisible(false);
			}
		});
		btnOpenSignup.setBackground(Color.CYAN);
		btnOpenSignup.setFont(new Font("Monospaced", Font.BOLD, 12));
		btnOpenSignup.setBounds(296, 11, 89, 23);
		btnOpenSignup.setFocusable(false);
		contentPane.add(btnOpenSignup);
		
		JButton btnOpenLogin = new JButton("Log in");
		btnOpenLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!loginForm.isVisible()) {
					loginForm.setVisible(true);
					if (signupForm.isVisible()) signupForm.setVisible(false);
				}
				else loginForm.setVisible(false);
			}
		});
		btnOpenLogin.setBackground(Color.CYAN);
		btnOpenLogin.setFont(new Font("Monospaced", Font.BOLD, 12));
		btnOpenLogin.setBounds(189, 11, 89, 23);
		btnOpenLogin.setFocusable(false);
		contentPane.add(btnOpenLogin);
		
		JLabel lblNewLabel_2 = new JLabel("Battleship");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Stencil Std", Font.BOLD | Font.ITALIC, 55));
		lblNewLabel_2.setBounds(10, 160, 559, 93);
		contentPane.add(lblNewLabel_2);
		
		Background bg = new Background();
		bg.setBounds(0, 0, 579, 372);
		contentPane.add(bg);
		bg.setLayout(null);
	}
}
