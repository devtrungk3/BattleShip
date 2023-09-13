package Game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.rmi.Naming;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Main.EndGame;
import Server.IServer;

import javax.swing.border.LineBorder;

public class MainPanel extends JPanel {
	
	Socket socket;
	JLabel notification;
	JLabel notification2;
	int distanceX, distanceY;
	ArrayList<Ship> ships = new ArrayList<Ship>();
	Point cursor = new Point(0, 0);
	int map[][] = new int[10][10];
	int shot[][] = new int[10][10];
	int enemyShot[][] = new int[10][10];
	boolean first;
	Image accurateShot = new ImageIcon("Accurate.png").getImage();
	Image missShot = new ImageIcon("Miss.png").getImage();
	Image water = new ImageIcon("water.png").getImage();
	int x, y;
	boolean start = false;
	boolean enemyOK = false;
	boolean myOK = false;
	boolean myTurn = false;
	@SuppressWarnings("rawtypes")
	private ArrayList infor;
	private Thread read;
	private JFrame mf;
	private IServer server;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("rawtypes")
	public MainPanel(final Socket socket, final ArrayList infor, final JFrame mf) {
		this.mf = mf;
		this.infor = infor;
		this.socket = socket;
		this.setBounds(0, 0, 500, 500);
		addShip();
		
		try {
		server = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JPanel notification_box = new JPanel();
		notification_box.setBorder(new LineBorder(new Color(0, 0, 0)));
		notification_box.setBounds(510, 375, 365, 100);
		notification_box.setBackground(Color.WHITE);
		add(notification_box);
		
		notification = new JLabel();
		notification.setFont(new Font("Monospaced", Font.BOLD, 20));
		notification_box.add(notification);
		
		notification2 = new JLabel();
		notification2.setFont(new Font("Monospaced", Font.BOLD, 15));
		notification_box.add(notification2);
		
		Thread gameRunning = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true) {
					try {
						server.IsRunning();
					} catch(Exception e) {
						mf.setVisible(false);
						JOptionPane.showConfirmDialog(null, "Server is close", infor.get(0)+"", JOptionPane.PLAIN_MESSAGE);
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						break;
					}
				}
				
			}
		});
		gameRunning.start();
		
		
		read = new Thread(new Runnable() {
			
			@SuppressWarnings("unchecked")
			public void run() {
				
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(socket.getInputStream());
					while (true) {
						String txt = dis.readUTF();
						if (txt.equals("start")) {
							enemyOK = true;
							if (myOK) {
								notification2.setText("");
							}
						}
						if (myOK && enemyOK) {
							// end game
							if (txt.equals("Lose")) {
								try {
								String s = server.UpdateMatchResult(infor.get(0)+"", 1, infor);
								if (s.equals("OK")) {
									infor.set(3, ((int)infor.get(3))+1);
									infor.set(4, ((int)infor.get(4))+1);
									infor.set(5, ((int)infor.get(5))+10);
								}
								} catch (Exception e) {
									e.printStackTrace();
								}
								mf.setVisible(false);
								EndGame frame = new EndGame("YOU WIN", infor);
								frame.setVisible(true);
							}
							// shot
							if (txt.length() == 2) {
								notification2.setText("");
								enemyAttack(txt);
							}
							else {
								switch(txt) {
								case "true":
									shot[x][y]= 1;
									File file = new File("soundBoom.wav");
									try {
										AudioInputStream audio = AudioSystem.getAudioInputStream(file);
										Clip clip = AudioSystem.getClip();
										clip.open(audio);
										clip.start();
									} catch (Exception e1) {
										e1.printStackTrace();
									}
									repaint();
									break;
								case "false":
									shot[x][y] = -1;
									file = new File("ShotWater.wav");
									try {
										AudioInputStream audio = AudioSystem.getAudioInputStream(file);
										Clip clip = AudioSystem.getClip();
										clip.open(audio);
										clip.start();
									} catch (Exception e1) {
										e1.printStackTrace();
									}
									myTurn = false;
									repaint();
									break;
								}
							}
							if (myTurn) {
								notification.setText("Your turn");
							}
							else {
								notification.setText("Enemy's turn");
							}
							if (txt.contains("destroyed")) {
								notification2.setText(txt);
							}
						}
						
					}
				} catch (Exception e) {
					if (mf.isVisible()) {
						try {
							String s = server.UpdateMatchResult(infor.get(0)+"", 1, infor);
							if (s.equals("OK")) {
								infor.set(3, ((int)infor.get(3))+1);
								infor.set(4, ((int)infor.get(4))+1);
								infor.set(5, ((int)infor.get(5))+10);
							}
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						mf.setVisible(false);
						EndGame frame = new EndGame("YOU WIN(enemy was AFK)", infor);
						frame.setVisible(true);
					}
					e.printStackTrace();
				}
			}
		});
		read.start();
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (!start) {
					if (!first) {
						cursor = e.getPoint();
						first = true;
		                for (Ship s : ships) {
		                	s.choose = false;
			                if (s.checkEntered((int)cursor.getX(), (int)cursor.getY())) {
			                	s.choose = true;
			                }
						}
					}
					else {
		                distanceX = (int)(e.getX()-cursor.getX());
		                distanceY = (int)(e.getY()-cursor.getY());
		                cursor = e.getPoint();
		                for (Ship s : ships) {
			                if (s.checkEntered((int)cursor.getX(), (int)cursor.getY())) {
			                	repaint();
			                }
						}
					}  
				}     
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				first = false;
				for (Ship s : ships) {
					if (s.choose) {
						s.checkPosition();
						if (!s.isInsideMap()) repaint();
						s.isIntersects(ships);
					}
				}
				distanceX = 0;
				distanceY = 0;
				repaint();
			}
		});
		final JButton btnSet = new JButton();
		btnSet.setFocusable(false);
		btnSet.setIcon(new ImageIcon("Set.png"));
		btnSet.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for (Ship s : ships) {
					if (s.choose) {
						int t = s.box.width;
						s.box.width = s.box.height;
						s.box.height = t;
						s.isInsideMap();
						s.isIntersects(ships);
						repaint();
						break;
					}
				}
			}
		});
		btnSet.setBounds(510, 260, 80, 80);
		add(btnSet);
		
		final JButton btnPlay = new JButton();
		btnPlay.setFocusable(false);
		btnPlay.setIcon(new ImageIcon("Play.png"));
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					start = true;
					for (Ship s : ships) {
						s.getPosition(map);
						s.choose = false;
						s.box.x = 550 + (s.box.x/50 * 20);
						s.box.y = 25 + (s.box.y/50 * 20);
						s.box.width = s.box.width/50 * 20;
						s.box.height = s.box.height/50 * 20;	
						s.pixel = 20;
					}
					repaint();
					btnSet.setVisible(false);
					btnPlay.setVisible(false);
					Write("start");
					myOK = true;
					if (enemyOK) {
						myTurn = false;
						notification.setText("Enemy's turn");
					}
					else {
						myTurn = true;
						notification.setText("Your turn");
						notification2.setText("The enemy isn't ready, please wait...");
					}
				} catch (Exception e2) {
					JOptionPane.showConfirmDialog(null, "Please use all ships", "Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnPlay.setBounds(610, 260, 80, 80);
		add(btnPlay);
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (myOK && enemyOK && myTurn) {
					notification2.setText("");
					y = (e.getPoint().x)/50;
					x = (e.getPoint().y)/50;
					if (x < 10 && y < 10 && shot[x][y] == 0) {
						Write(x + "" + y);
					}
				}
			}
		});
	}
	
	private void addShip() {
		for (int i=2; i<6; i++) {
			Ship s = new Ship(500+50*(i-2), 0, 1, i);
			ships.add(s);
		}
	}

	private void enemyAttack(String txt) {
		int x = Integer.valueOf(txt.charAt(0)+"");
		int y = Integer.valueOf(txt.charAt(1)+"");
		if (map[x][y] != 0) {
			BrokenShip(x, y);
			enemyShot[x][y] = 1;
			CheckMap();
			Write("true");
		}
		else {
			enemyShot[x][y] = -1;
			Write("false");
			myTurn = true;
		}
		repaint();
	}
	
	private void BrokenShip(int x, int y) {
		int shipType = map[x][y];
		map[x][y] = 0;
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				if (map[i][j] == shipType) return;
			}
		}
		notification2.setText("Your ship " + shipType + " has been destroyed");
		Write("Ship " + shipType + " of enemy has been destroyed");
	}
	
	@SuppressWarnings("unchecked")
	private void CheckMap() {
		boolean lose = true;
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				if (map[i][j] != 0) lose = false;
			}
		}
		if (lose) {
			try {
				String s = server.UpdateMatchResult(infor.get(0)+"", 0, infor);
				if (s.equals("OK")) {
					infor.set(3, ((int)infor.get(3))+1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			mf.setVisible(false);
			EndGame frame = new EndGame("YOU LOSE", infor);
			frame.setVisible(true);
			Write("Lose");
		}
	}
	
	private void Write(String txt) {
		DataOutputStream dos = null;
		try {
			dos = new DataOutputStream(socket.getOutputStream());
			dos.writeUTF(txt);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		drawGrid(g2);
		for (Ship s : ships) {
			s.draw(g2, cursor, distanceX, distanceY);
		}
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				if (shot[i][j] == -1) {
					g2.drawImage(missShot,j*50, i*50, 50, 50, null);
				}
				else {
					if (shot[i][j] == 1) {
						g2.drawImage(accurateShot,j*50, i*50, 50, 50, null);
					}
				}
			}
		}
	}

	private void drawGrid(Graphics2D g2) {
		g2.drawImage(water, 0, 0, 500, 500, null);
		if (start) g2.drawImage(water, 550, 25, 200, 200, null);
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				g2.drawRect(i*50, j*50, 50, 50);
				if (start) {
					g2.drawRect(i*20+550, j*20+25, 20, 20);
					if (enemyShot[i][j] == 1) {
						g2.setColor(Color.RED);
						g2.fillRect(j*20+550+1, i*20+25+1, 20-1, 20-1);
					}
					if (enemyShot[i][j] == -1) {
						g2.setColor(Color.BLUE);
						g2.fillRect(j*20+550+1, i*20+25+1, 20-1, 20-1);
					}
					g2.setColor(Color.BLACK);
				}
			}
		}
	}
	
}
