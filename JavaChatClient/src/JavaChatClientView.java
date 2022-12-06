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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.ScrollPaneConstants;

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
	private int userBtnIsClicked = 0;
	private JButton chatBtn;
	private JButton addFriendBtn;
	private JButton newChat;
	private JTextField textFd;
	private JTextField textMy;
	private JScrollPane friendPanel;
	private Panel userInfo;
	
	private Panel chatPanel; // 채팅 목록 패널
	
	private ArrayList<String> friendList = new ArrayList<String>(); // 친구 이름 리스트
	private ArrayList<Panel> friendPanels = new ArrayList<Panel>(); // 각 친구 패널을 저장하는 리스트
	private ArrayList<JButton> friendPics = new ArrayList<JButton>(); // 각 친구의 프로필 사진 리스트
	private ArrayList<JButton> friendNames = new ArrayList<JButton>(); // 각 친구의 이름을 보여주는 텍스트필드 리스트
	private Panel eachPanel = new Panel();
	private JButton friendBtn = new JButton();
	
	private int friendNum = 0;
	private int panelPointY = 0;
	
	private String ip_addr;
	private String port_no;
	
	
	private JTextField txtUserName;
	private JTextField txtIpAddress;
	private JTextField txtPortNumber;
	/**
	 * Create the frame.
	 */
	public JavaChatClientView(String username, String ip_addr, String port_no) {
		this.ip_addr = ip_addr;
		this.port_no = port_no;
		this.UserName = username;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		
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
		
		friendPanel = new JScrollPane(); // 친구 목록을 보여주는 스크롤 팬
		friendPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		friendPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		friendPanel.setBounds(0, 156, 313, 357);
		friendPanel.getViewport().setBackground(Color.white);
		friendPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		myInfoPanel.add(friendPanel);
		
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
		
		txtUserName = new JTextField();
		txtUserName.setHorizontalAlignment(SwingConstants.CENTER);
		txtUserName.setBounds(101, 39, 116, 33);
		//contentPane.add(txtUserName);
		txtUserName.setColumns(10);
		
		txtIpAddress = new JTextField();
		txtIpAddress.setHorizontalAlignment(SwingConstants.CENTER);
		txtIpAddress.setText("127.0.0.1");
		txtIpAddress.setColumns(10);
		txtIpAddress.setBounds(101, 100, 116, 33);
		//contentPane.add(txtIpAddress);
		
		
		txtPortNumber = new JTextField();
		txtPortNumber.setText("30000");
		txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtPortNumber.setColumns(10);
		txtPortNumber.setBounds(101, 163, 116, 33);
		//contentPane.add(txtPortNumber);
		
		txtUserName.addActionListener(action);
		txtIpAddress.addActionListener(action);
		txtPortNumber.addActionListener(action);
		
		/*chatBtn.addActionListener(new ActionListener() { // Chat Button Click
			public void actionPerformed(ActionEvent e) {
				//scrollPaneFriendList.setVisible(false);
				myInfoPanel.setVisible(false);
				//scrollPaneChat.setVisible(true);
				//profileLabel.setText("채팅");
				String username = txtUserName.getText().trim();
				String ip_addr = txtIpAddress.getText().trim();
				String port_no = txtPortNumber.getText().trim();
				ChatRoom view3 = new ChatRoom(username, ip_addr, port_no);
				view3.setVisible(true);
				//setVisible(false);
			}
		});*/
		
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
		
		chatPanel = new Panel(); // 내 프로필 창
		chatPanel.setBackground(new Color(255, 255, 255));
		chatPanel.setBounds(73, 0, 313, 513);
		contentPane.add(chatPanel);
		chatPanel.setLayout(null);
		
		newChat = new JButton("");
		newChat.setBounds(265, 25, 30, 30);	
		newChat.setBackground(new Color(255, 255, 255));
		newChat.setBorder(new EmptyBorder(0, 0, 0, 0));
		chatPanel.add(newChat);
		newChat.setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/newChat.png")));
		
		/*
		 * for (int i = 0; i < friendNum; i++) { friendPics.get(i).setBounds(12, 0, 43,
		 * 43); friendPics.get(i).setIcon(new ImageIcon(JavaChatClientView.class.
		 * getResource("/images/smallBasicProfile .png")));
		 * friendPics.get(i).setBorder(new EmptyBorder(0, 0, 0, 0));
		 * friendPics.get(i).setBackground(new Color(255, 255, 255));
		 * 
		 * friendNames.get(i).setBounds(66, 12, 133, 22); friendNames.get(i).setFont(new
		 * Font("굴림", Font.PLAIN, 18)); friendNames.get(i).setBackground(new Color(255,
		 * 255, 255)); friendNames.get(i).setBorder(new EmptyBorder(0, 0, 0, 0));
		 * friendNames.get(i).setText(friendList.get(i));
		 * friendNames.get(i).setEditable(false); System.out.println(friendList.get(i));
		 * 
		 * friendPanels.get(i).add(friendNames.get(i));
		 * friendPanels.get(i).add(friendPics.get(i));
		 * friendPanels.get(i).setBackground(new Color(255, 255, 255));
		 * friendPanels.get(i).setBounds(0, panelPointY, 293, 50);
		 * 
		 * friendPanels.get(i).add(friendPics.get(i));
		 * friendPanels.get(i).add(friendNames.get(i));
		 * 
		 * friendPanel.add(friendPanels.get(i)); panelPointY += 50; }
		 */
		
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
		private JButton friendPic = new JButton("");
		private JTextField friendNameField = new JTextField("");
		
		
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
				friendPics.add(friendPic);
				friendNames.add(friendBtn);
				friendPanels.add(eachPanel);
				System.out.print("friend added " + friendList.size());
				friendNum++;
				dispose();
				
				friendPics.get(friendNum - 1).setBounds(12, 0, 43, 43);
				friendPics.get(friendNum - 1).setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/smallBasicProfile .png")));
				friendPics.get(friendNum - 1).setBorder(new EmptyBorder(0, 0, 0, 0));
				friendPics.get(friendNum - 1).setBackground(new Color(255, 255, 255));
				
				friendNames.get(friendNum - 1).setBounds(47, 7, 60, 30);
				friendNames.get(friendNum - 1).setHorizontalTextPosition(SwingConstants.LEFT);
				friendNames.get(friendNum - 1).setFont(new Font("굴림", Font.PLAIN, 18));
				friendNames.get(friendNum - 1).setBackground(new Color(255, 255, 255));
				friendNames.get(friendNum - 1).setBorder(new EmptyBorder(0, 0, 0, 0));
				friendNames.get(friendNum - 1).setText(friendList.get(friendNum - 1));
				//friendBtn.setEditable(false);
				
				friendPanels.get(friendNum - 1).add(friendBtn);
				friendPanels.get(friendNum - 1).add(friendPics.get(friendNum - 1));
				friendPanels.get(friendNum - 1).setBackground(new Color(255, 255, 255));
				friendPanels.get(friendNum - 1).setBounds(0, panelPointY, 293, 50);
				
				friendPanels.get(friendNum - 1).add(friendPics.get(friendNum - 1));
				friendPanels.get(friendNum - 1).add(friendBtn);
				
				friendPanel.add(friendPanels.get(friendNum - 1));
				panelPointY += 50;
			}
		}
	}
	
	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == userBtn) {
				panelPointY = 0;
				
				myInfoPanel.setEnabled(true);
				myInfoPanel.setVisible(true);
				friendPanel.setEnabled(true);
				friendPanel.setVisible(true);
				chatPanel.setVisible(false);
				
				for (int i = 0; i < friendNum; i++) {
					friendPics.get(friendNum - 1).setBounds(12, 0, 43, 43);
					friendPics.get(friendNum - 1).setIcon(new ImageIcon(JavaChatClientView.class.getResource("/images/smallBasicProfile .png")));
					friendPics.get(friendNum - 1).setBorder(new EmptyBorder(0, 0, 0, 0));
					friendPics.get(friendNum - 1).setBackground(new Color(255, 255, 255));
					
					friendNames.get(friendNum - 1).setBounds(0, 0, 150, 40);
					friendNames.get(friendNum - 1).setFont(new Font("굴림", Font.PLAIN, 18));
					friendNames.get(friendNum - 1).setHorizontalAlignment(SwingConstants.LEFT);
					friendNames.get(friendNum - 1).setBackground(new Color(255, 255, 255));
					friendNames.get(friendNum - 1).setBorder(new EmptyBorder(0, 0, 0, 0));
					friendNames.get(friendNum - 1).setText(friendList.get(friendNum - 1));
					friendNames.get(friendNum - 1).addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							//new ChatRoom(UserName, ip_addr, port_no);
						}
					});
					//friendBtn.setEditable(false);
					
					friendPanels.get(friendNum - 1).add(friendBtn);
					friendPanels.get(friendNum - 1).add(friendPics.get(friendNum - 1));
					friendPanels.get(friendNum - 1).setBackground(new Color(255, 255, 255));
					friendPanels.get(friendNum - 1).setBounds(0, panelPointY, 293, 50);
					
					friendPanels.get(friendNum - 1).add(friendPics.get(friendNum - 1));
					friendPanels.get(friendNum - 1).add(friendBtn);
					
					friendPanel.add(friendPanels.get(friendNum - 1));
					panelPointY += 50;
				}
				
				//repaint();
			}
			else if (e.getSource() == chatBtn) {
				String username = txtUserName.getText().trim();
				String ip_addr = txtIpAddress.getText().trim();
				String port_no = txtPortNumber.getText().trim();
				ChatRoom view3 = new ChatRoom(username, ip_addr, port_no);
				//view3.setVisible(true);
				setVisible(false);
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
