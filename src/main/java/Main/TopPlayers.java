package Main;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Server.IServer;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

public class TopPlayers extends JFrame implements WindowListener{

	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	private ArrayList infor;
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
					infor.add("password");
					infor.add("email");
					infor.add(0);
					infor.add(0);
					infor.add(0);
					TopPlayers frame = new TopPlayers(infor);
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TopPlayers(final ArrayList infor) {
		
		this.infor = infor;
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 398, 477);
		setLocationRelativeTo(null);
		addWindowListener(this);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		try {
		server = (IServer) Naming.lookup("rmi://localhost:9999/server");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JLabel lblNewLabel = new JLabel("STANDINGS");
		lblNewLabel.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 20));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(10, 11, 362, 45);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 91, 362, 282);
		contentPane.add(scrollPane);
		
		final JTable standing = new JTable();
		scrollPane.setViewportView(standing);
		
		
		final JComboBox comboBox = new JComboBox();
		Vector val = new Vector();
		val.add("Score");
		val.add("Match");
		val.add("Win");
		comboBox.setModel(new DefaultComboBoxModel(val));
		comboBox.setBounds(126, 51, 131, 22);
		contentPane.add(comboBox);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 387, 362, 22);
		contentPane.add(scrollPane_1);

		final JTable yourRank = new JTable();
		scrollPane_1.setViewportView(yourRank);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setTable(comboBox.getSelectedIndex(), standing, yourRank);
			}
		});
		
		setTable(0, standing, yourRank);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void setTable(int choose, JTable standing, JTable yourRank) {
		
		String attribute = "";
		Vector col = new Vector();
		col.add("TOP");
		col.add("USERNAME");
		switch(choose) {
		case 0:
			col.add("SCORE");
			attribute = infor.get(5)+"";
			break;
		case 1:
			col.add("MATCH");
			attribute = infor.get(3)+"";
			break;
		case 2:
			col.add("WIN");
			attribute = infor.get(4)+"";
		}
		
		Vector row = new Vector();
		int top = 0;
		try {
			row = server.GetStanding(choose, infor.get(0)+"");
			top = (int) row.get(row.size()-1);
			row.remove(row.size()-1);
		} catch (RemoteException e) {
			JOptionPane.showConfirmDialog(null, "The server is close", "", JOptionPane.PLAIN_MESSAGE);
		}
		standing.setModel(new DefaultTableModel(row, col) {
			
			@Override
			public boolean isCellEditable(int row, int column) {       
				return false;		// disable editing table
			}
		});
		
		Vector val = new Vector();
		if (top == 0) {
			val.add("No rank");
		}
		else {
			val.add(top);
		}
		val.add(infor.get(0));
		val.add(attribute);
		
		yourRank.setModel(new DefaultTableModel(null, val) {
			
			@Override
			public boolean isCellEditable(int row, int column) {       
				return false;		// disable editing table
			}
		});
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
