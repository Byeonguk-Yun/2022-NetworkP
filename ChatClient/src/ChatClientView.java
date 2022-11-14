import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.Panel;
import java.awt.Button;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;

import java.awt.BorderLayout;
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

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.DropMode;

public class ChatClientView extends JFrame {

	private JPanel contentPane;

	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final  int BUF_LEN = 128; //  Windows 처럼 BUF_LEN 을 정의
	private Socket socket; // 연결소켓
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JLabel lblUserName;
	//private JTextArea textArea;
	private JTextPane textArea;
	private JTextField textFd;
	private JTextField textMy;
	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */
	public ChatClientView(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 550);
		contentPane = new JPanel();
		contentPane.setToolTipText("");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Panel panel_1 = new Panel();
		panel_1.setBackground(new Color(255, 255, 255));
		panel_1.setBounds(71, 0, 313, 511);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		textFd = new JTextField();
		textFd.setHorizontalAlignment(SwingConstants.CENTER);
		textFd.setForeground(new Color(0, 0, 0));
		textFd.setFont(new Font("굴림", Font.BOLD, 24));
		textFd.setText("\uCE5C\uAD6C");
		textFd.setBounds(12, 41, 58, 25);
		textFd.setBorder(null);
		panel_1.add(textFd);
		textFd.setColumns(10);
		
		textMy = new JTextField();
		textMy.setFont(new Font("굴림", Font.BOLD, 16));
		textMy.setText("\uB0B4 \uD504\uB85C\uD544");
		textMy.setHorizontalAlignment(SwingConstants.CENTER);
		textMy.setBounds(12, 87, 80, 21);
		textMy.setBorder(null);
		panel_1.add(textMy);
		textMy.setColumns(10);
		
		JButton userBtn = new JButton("");
		userBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		userBtn.setBounds(12, 28, 50, 50);
		contentPane.add(userBtn);
		
		JButton fdBtn = new JButton("");
		fdBtn.setBounds(12, 112, 50, 50);
		contentPane.add(fdBtn);
		
		try {
			socket = new Socket(ip_addr, Integer.parseInt(port_no));
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			SendMessage("/login " + UserName);
			ListenNetwork net = new ListenNetwork();
			net.start();
			Myaction action = new Myaction();

		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}
	}
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
						}// catch문 끝
					}
					String	msg = new String(b, "euc-kr");
					msg = msg.trim(); // 앞뒤 blank NULL, \n 모두 제거
					AppendText(msg); // server 화면에 출력
				} catch (IOException e) {
					AppendText("dis.read() error");
					try {
						dos.close();
						dis.close();
						socket.close();
						break;
					} catch (Exception ee) {
						break;
					} // catch문 끝
				} // 바깥 catch문끝
				
			}
		}
	}
	class Myaction implements ActionListener // 내부클래스로 액션 이벤트 처리 클래스
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button을 누르거나 메시지 입력하고 Enter key 치면
		}
	}
	public void AppendText(String msg) {
		//textArea.append(msg + "\n");
		//AppendIcon(icon1);
		//int len = textArea.getDocument().getLength(); // same value as
        //textArea.setCaretPosition(len); // place caret at the end (with no selection)
 		//textArea.replaceSelection(msg + "\n"); // there is no selection, so inserts at caret
 	}
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

	// Server에게 network으로 전송
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
