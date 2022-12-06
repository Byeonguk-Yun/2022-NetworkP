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

public class ChatRoom2 extends JFrame {
	private JPanel contentPane;
	private JTextField txtInput;
	private String UserName;
	private JButton btnSend;
	private static final int BUF_LEN = 128; //  Windows 처占쏙옙 BUF_LEN 占쏙옙 占쏙옙占쏙옙
	private Socket socket; // 占쏙옙占쏙옙占쏙옙占�
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	private JLabel lblUserName;
	//private JTextArea textArea;
	private JTextPane textArea;
	
	/**
	 * Create the frame.
	 */
	public ChatRoom2(String username, String ip_addr, String port_no) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 392, 462);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 352, 340);
		contentPane.add(scrollPane);
		
		textArea = new JTextPane();
		textArea.setEditable(true);
		textArea.setFont(new Font("占쏙옙占쏙옙체", Font.PLAIN, 14));
		scrollPane.setViewportView(textArea);

		txtInput = new JTextField();
		txtInput.setBounds(91, 365, 185, 40);
		contentPane.add(txtInput);
		txtInput.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBounds(288, 364, 76, 40);
		contentPane.add(btnSend);
		
		lblUserName = new JLabel("Name");
		lblUserName.setFont(new Font("占쏙옙占쏙옙", Font.PLAIN, 14));
		lblUserName.setHorizontalAlignment(SwingConstants.CENTER);
		lblUserName.setBounds(12, 364, 67, 40);
		contentPane.add(lblUserName);
		setVisible(true);
	
		AppendText("User " + username + " connecting " + ip_addr + " " + port_no);
		UserName = username;
		lblUserName.setText(username+">");
		
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
			btnSend.addActionListener(action); // 占쏙옙占쏙옙클占쏙옙占쏙옙占쏙옙 占쌓쇽옙 占쏙옙占쏙옙占십몌옙 占쏙옙達占쏙옙占� 클占쏙옙占쏙옙占쏙옙
			txtInput.addActionListener(action);
			txtInput.requestFocus();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppendText("connect error");
		}

	}
	// Server Message占쏙옙 占쏙옙占쏙옙占쌔쇽옙 화占썽에 표占쏙옙
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
						}// catch占쏙옙 占쏙옙
					}
					String	msg = new String(b, "euc-kr");
					msg = msg.trim(); // 占쌌듸옙 blank NULL, \n 占쏙옙占� 占쏙옙占쏙옙
					AppendText(msg); // server 화占썽에 占쏙옙占�
				} catch (IOException e) {
					AppendText("dis.read() error");
					try {
						dos.close();
						dis.close();
						socket.close();
						break;
					} catch (Exception ee) {
						break;
					} // catch占쏙옙 占쏙옙
				} // 占쌕깍옙 catch占쏙옙占쏙옙
				
			}
		}
	}
	// keyboard enter key 치占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
	class Myaction implements ActionListener // 占쏙옙占쏙옙클占쏙옙占쏙옙占쏙옙 占쌓쇽옙 占싱븝옙트 처占쏙옙 클占쏙옙占쏙옙
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			// Send button占쏙옙 占쏙옙占쏙옙占신놂옙 占쌨쏙옙占쏙옙 占쌉뤄옙占싹곤옙 Enter key 치占쏙옙
			if (e.getSource() == btnSend || e.getSource() == txtInput) {
				String msg = null;
				msg = String.format("[%s] %s\n", UserName, txtInput.getText());
				SendMessage(msg);
				txtInput.setText(""); // 占쌨쇽옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쌨쇽옙占쏙옙 占쏙옙占쏙옙창占쏙옙 占쏙옙占쏙옙.
				txtInput.requestFocus(); // 占쌨쇽옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 커占쏙옙占쏙옙 占쌕쏙옙 占쌔쏙옙트 占십듸옙占� 占쏙옙치占쏙옙킨占쏙옙
				if (msg.contains("/exit")) // 占쏙옙占쏙옙 처占쏙옙
					System.exit(0);
			}
		}
	}
	ImageIcon icon1 = new ImageIcon("src/icon1.jpg");
	public void AppendIcon(ImageIcon icon) {
		int len = textArea.getDocument().getLength();
		textArea.setCaretPosition(len); // place caret at the end (with no selection)
		textArea.insertIcon(icon);	
	}
	// 화占썽에 占쏙옙占�
	public void AppendText(String msg) {
		//textArea.append(msg + "\n");
		AppendIcon(icon1);
		int len = textArea.getDocument().getLength(); // same value as
        textArea.setCaretPosition(len); // place caret at the end (with no selection)
 		textArea.replaceSelection(msg + "\n"); // there is no selection, so inserts at caret
 	}

	// Windows 처占쏙옙 message 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占싸븝옙占쏙옙 NULL 占쏙옙 占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쌉쇽옙
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
