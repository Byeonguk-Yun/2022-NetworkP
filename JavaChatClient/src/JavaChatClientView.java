// JavaChatClientView.java
// �������� ä�� â
import java.awt.Panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class JavaChatClientView extends JFrame {
	private JPanel contentPane;
	private String UserName;
	private static final int BUF_LEN = 128; //  Windows ó�� BUF_LEN �� ����
	private Socket socket; // �������
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private Panel myInfoPanel; // 내 프로필, 친구 목록 패널
	private JButton userBtn;
	private JButton chatBtn;
	private JButton addFriendBtn;
	private JTextField textFd;
	private JTextField textMy;
	private JScrollPane friendPanel;
	private Panel userInfo;
	
	private Panel chatPanel; // 채팅 목록 패널
	
	private ArrayList<String> friendList = new ArrayList<String>();
	
	/**
	 * Create the frame.
	 */
	public JavaChatClientView(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		myInfoPanel = new Panel(); // 내 프로필 창
		myInfoPanel.setBackground(new Color(255, 255, 255));
		myInfoPanel.setBounds(73, 0, 313, 513);
		contentPane.add(myInfoPanel);
		myInfoPanel.setLayout(null);
		
		textFd = new JTextField(); // 상단 '친구' 텍스트
		textFd.setHorizontalAlignment(SwingConstants.CENTER);
		textFd.setForeground(new Color(0, 0, 0));
		textFd.setFont(new Font("굴림", Font.BOLD, 24));
		textFd.setText("친구");
		textFd.setBounds(12, 30, 58, 25);
		textFd.setBorder(null);
		myInfoPanel.add(textFd);
		textFd.setColumns(10);
		
		textMy = new JTextField(); // 상단 '내 프로필' 텍스트
		textMy.setFont(new Font("굴림", Font.BOLD, 16));
		textMy.setText("내 프로필");
		textMy.setHorizontalAlignment(SwingConstants.CENTER);
		textMy.setBounds(12, 70, 80, 21);
		textMy.setBorder(null);
		myInfoPanel.add(textMy);
		textMy.setColumns(10);
		
		userBtn = new JButton(""); // 사용자탭 버튼
		userBtn.setBackground(new Color(240, 240, 240));
		userBtn.setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/user1 (2).png")));
		userBtn.setBounds(10, 20, 50, 50);
		userBtn.setBorderPainted(false);
		MyActionListener action = new MyActionListener();
		userBtn.addActionListener(action);
		contentPane.add(userBtn);
		
		chatBtn = new JButton(""); // 채팅방탭 버튼
		chatBtn.setBackground(new Color(240, 240, 240));
		chatBtn.setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/chaticon.png")));
		chatBtn.setBounds(10, 90, 50, 50);
		chatBtn.setBorderPainted(false);
		chatBtn.addActionListener(action);
		contentPane.add(chatBtn);
		setVisible(true);
		
		addFriendBtn = new JButton(""); // 친구 추가 버튼
		addFriendBtn.setBackground(new Color(255, 255, 255));
		addFriendBtn.setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/addFriend.png")));
		addFriendBtn.setBounds(265, 25, 30, 30);
		addFriendBtn.setBorderPainted(false);
		addFriendBtn.addActionListener(action);
		myInfoPanel.add(addFriendBtn);
		
		JButton myProfPic = new JButton("");
		myProfPic.setBackground(new Color(255, 255, 255));
		myProfPic.setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/smallBasicProfile .png")));
		myProfPic.setBounds(12, 100, 43, 43);
		myProfPic.setBorder(null);
		myInfoPanel.add(myProfPic);
		
		JLabel lblNewLabel = new JLabel(username);
		lblNewLabel.setFont(new Font("굴림", Font.PLAIN, 18));
		lblNewLabel.setBounds(66, 111, 133, 22);
		myInfoPanel.add(lblNewLabel);
		
		friendPanel = new JScrollPane(); // 친구 목록을 보여주는 스크롤 팬
		friendPanel.setBounds(73, 156, 313, 357);
		friendPanel.setBackground(new Color(255, 255, 255));
		contentPane.add(friendPanel);
		
		chatPanel = new Panel(); // 내 프로필 창
		chatPanel.setBackground(new Color(255, 255, 255));
		chatPanel.setBounds(73, 0, 313, 513);
		contentPane.add(chatPanel);
		chatPanel.setLayout(null);
		
		/*
		userInfo = new Panel();
		friendPanel.setLayout(new BoxLayout(friendPanel, BoxLayout.Y_AXIS));
		
		ArrayList<JButton> friendButton = new ArrayList<JButton>();
		for (String friendname : friendList) {
			JButton friendBtn = new JButton();
			//friendBtn.setIcon(null)
			friendPanel.add(userInfo);
		}*/
	
		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			SendMessage("/login " + UserName);
			ListenNetwork net = new ListenNetwork();
			net.start();
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}
	
	class addFriend extends JFrame implements ActionListener {
		private JPanel mainPane = new JPanel();
		private Panel addFriendPanel;
		private JLabel txt;
		private JTextField name;
		private JButton addName;
		
		public addFriend() {
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setContentPane(mainPane);
			setBounds(130, 130, 300, 350);
			mainPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			addFriendPanel = new Panel();
			addFriendPanel.setLayout(null);
			mainPane.add(addFriendPanel);
			mainPane.setLayout(null);
			//setSize(300, 300);
			
			txt = new JLabel("이름 입력");
			txt.setFont(new Font("굴림", Font.BOLD, 22));
			txt.setBounds(98, 90, 110, 30);
			
			name = new JTextField(); // 친구 이름을 입력하는 필드
			//name.setHorizontalAlignment(JTextField.CENTER);
			name.setForeground(new Color(0, 0, 0));
			name.setFont(new Font("굴림", Font.BOLD, 20));
			name.setBounds(83, 200, 130, 30);
			name.setBorder(new LineBorder(new Color(0, 0, 0)));
			
			addName = new JButton("추가"); // 친구 이름 입력 후 추가하는 버튼
			addName.setBackground(new Color(255, 255, 200));
			addName.setFont(new Font("굴림", Font.BOLD, 20));
			addName.setBounds(120, 240, 50, 35);
			addName.setBorder(new LineBorder(new Color(0, 0, 0)));
			addName.addActionListener(this);
			
			mainPane.add(txt);	
			mainPane.add(name);
			mainPane.add(addName);
			setVisible(true);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == addName) {
				friendList.add(name.getText());
				System.out.print("friend added");
				dispose();
			}
		}
	}
	
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == userBtn) {
				myInfoPanel.setEnabled(true);
				myInfoPanel.setVisible(true);
				friendPanel.setEnabled(true);
				friendPanel.setVisible(true);
			}
			else if (e.getSource() == chatBtn) {
				myInfoPanel.setEnabled(false);
				myInfoPanel.setVisible(false);
				friendPanel.setEnabled(false);
				friendPanel.setVisible(false);
			}
			else if (e.getSource() == addFriendBtn) {
				addFriend af = new addFriend();
			}
		}
	}
	
	// Server Message�� �����ؼ� ȭ�鿡 ǥ��
	class ListenNetwork extends Thread {
		public void run() {
			while (true) {
				try {
					// String msg = dis.readUTF();
					byte[] b = new byte[BUF_LEN];
					int ret;
					ret = dis.read(b);
					if (ret < 0) {
						AppendText("dis.read() < 0 error");
						try {
							dos.close();
							dis.close();
							socket.close();
							break;
						} catch (Exception ee) {
							break;
						}// catch�� ��
					}
					String	msg = new String(b, "euc-kr");
					msg = msg.trim(); // �յ� blank NULL, \n ��� ����
					AppendText(msg); // server ȭ�鿡 ���
				} catch (IOException e) {
					AppendText("dis.read() error");
					try {
						dos.close();
						dis.close();
						socket.close();
						break;
					} catch (Exception ee) {
						break;
					} // catch�� ��
				} // �ٱ� catch����
				
			}
		}
	}

	public void AppendText(String msg) {
		//textArea.append(msg + "\n");
 	}

	// Windows ó�� message ������ ������ �κ��� NULL �� ����� ���� �Լ�
	public byte[] MakePacket(String msg) {
		byte[] packet = new byte[BUF_LEN];
		byte[] bb = null;
		int i;
		for (i = 0; i < BUF_LEN; i++)
			packet[i] = 0;
		try {
			bb = msg.getBytes("euc-kr");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		for (i = 0; i < bb.length; i++)
			packet[i] = bb[i];
		return packet;
	}

	// Server���� network���� ����
	public void SendMessage(String msg) {
		try {
			// dos.writeUTF(msg);
			byte[] bb;
			bb = MakePacket(msg);
			dos.write(bb, 0, bb.length);
		} catch (IOException e) {
			AppendText("dos.write() error");
			try {
				dos.close();
				dis.close();
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.exit(0);
			}
		}
	}
}
