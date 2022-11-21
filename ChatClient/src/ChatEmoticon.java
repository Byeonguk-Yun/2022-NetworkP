import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ChatEmoticon extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatEmoticon frame = new ChatEmoticon();
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
	public ChatEmoticon() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 413, 250);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton emoBtn1 = new JButton("");
		emoBtn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		emoBtn1.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE301.png")));
		emoBtn1.setBounds(0, 0, 90, 98);
		emoBtn1.setContentAreaFilled(false);
		emoBtn1.setBorderPainted(false);
		contentPane.add(emoBtn1);
		
		JButton emoBtn2 = new JButton("");
		emoBtn2.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE302.png")));
		emoBtn2.setBounds(102, 0, 90, 98);
		emoBtn2.setContentAreaFilled(false);
		emoBtn2.setBorderPainted(false);
		contentPane.add(emoBtn2);
		
		JButton emoBtn3 = new JButton("");
		emoBtn3.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE303.png")));
		emoBtn3.setBounds(201, 0, 90, 98);
		emoBtn3.setContentAreaFilled(false);
		emoBtn3.setBorderPainted(false);
		contentPane.add(emoBtn3);
		
		JButton emoBtn5 = new JButton("");
		emoBtn5.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE305.png")));
		emoBtn5.setBounds(0, 108, 90, 98);
		emoBtn5.setContentAreaFilled(false);
		emoBtn5.setBorderPainted(false);
		contentPane.add(emoBtn5);
		
		JButton emoBtn6 = new JButton("");
		emoBtn6.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE306.png")));
		emoBtn6.setBounds(99, 108, 90, 98);
		emoBtn6.setContentAreaFilled(false);
		emoBtn6.setBorderPainted(false);
		contentPane.add(emoBtn6);
		
		JButton emoBtn7 = new JButton("");
		emoBtn7.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE307.png")));
		emoBtn7.setBounds(201, 108, 90, 98);
		emoBtn7.setContentAreaFilled(false);
		emoBtn7.setBorderPainted(false);
		contentPane.add(emoBtn7);
		
		JButton emoBtn4 = new JButton("");
		emoBtn4.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE304.png")));
		emoBtn4.setBounds(303, 0, 90, 98);
		emoBtn4.setContentAreaFilled(false);
		emoBtn4.setBorderPainted(false);
		contentPane.add(emoBtn4);
		
		JButton emoBtn8 = new JButton("");
		emoBtn8.setIcon(new ImageIcon(ChatEmoticon.class.getResource("/images/\uC0C1\uC0C1\uBD80\uAE308.png")));
		emoBtn8.setBounds(303, 108, 90, 98);
		emoBtn8.setContentAreaFilled(false);
		emoBtn8.setBorderPainted(false);
		contentPane.add(emoBtn8);
	}
}
