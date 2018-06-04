package com.qq.view;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.*;

import com.qq.common.Message;
import com.qq.common.Package;
import com.qq.common.PackageSource;
import com.qq.common.PackageType;
import com.qq.model.UserFrameManage;
import com.qq.model.ClientSocketThread;
import com.qq.model.ClientSocketThreadManage;

public class QQChat extends JFrame implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7998055745470924864L;
	private String uid, uid2;
	private UserFrameManage ufm;
	private JTextArea jta;
	private JScrollPane jsp;
	private JPanel jp;
	private JTextField jtf;
	private JButton jb;

	public QQChat(String uid, String uid2) throws HeadlessException {
		// TODO Auto-generated constructor stub
		this.uid = uid;
		this.uid2 = uid2;
		ufm = UserFrameManage.getInstance();
		jta = new JTextArea();
		jsp = new JScrollPane(jta);
		jp = new JPanel();
		jtf = new JTextField(10);
		jtf.addKeyListener(this);
		jb = new JButton("send");
		jb.addActionListener(this);
		jp.add(jtf);
		jp.add(jb);
		Container c = this.getContentPane();
		c.add(jsp);
		c.add(jp, "South");
		this.setSize(300, 200);
		this.setLocation(850, 20);
		this.setIconImage(new ImageIcon("image/qqicon.png").getImage());
		this.setTitle(uid + " is chatting with " + uid2);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				ufm.removeChat((QQChat) e.getWindow());
				e.getWindow().dispose();
			}
		});
		this.setVisible(true);

	}

	public void showMessage(Message m) {
		String sender = m.getSender();
		String receiver = m.getReceiver();
		String content = m.getContent();
		String time = m.getSendTime();
		String info = "from " + sender + " to " + receiver + ":\r\n" + content + "\r\n@" + time + "\r\n";
		jta.append(info);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(jb)) {
			String info = jtf.getText();
			jta.append("from " + uid + " to " + uid2 + ":\r\n" + info + "\r\n@"
					+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()) + "\r\n");
			jtf.setText("");
			Message msg = new Message();
			msg.setSender(uid);
			msg.setReceiver(uid2);
			msg.setContent(info);
			msg.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			ClientSocketThread cst = ClientSocketThreadManage.getThread(uid);
			Package p = new Package(PackageType.message_common, PackageSource.client, msg);
			cst.sendMessage(p);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
