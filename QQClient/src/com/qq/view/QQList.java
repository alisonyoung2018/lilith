package com.qq.view;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import com.qq.model.ClientSocketThread;
import com.qq.model.ClientSocketThreadManage;
import com.qq.model.UserFrameManage;
import com.qq.tool.Const;

public class QQList extends JFrame implements ActionListener, MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1980999643340602873L;
	private String uid;
	private UserFrameManage ufm;
	private CardLayout cl;
	private JPanel jp, jp1, jp2, jp3, jp1_jp1, jp1_jp2, jp2_jp1, jp2_jp2, jp3_jp1, jp3_jp2;
	private JButton jp1_jb1, jp1_jb2, jp1_jb3, jp2_jb1, jp2_jb2, jp2_jb3, jp3_jb1, jp3_jb2, jp3_jb3;
	private JScrollPane jp1_jsp, jp2_jsp, jp3_jsp;
	private JLabel[] jp1_jls, jp2_jls, jp3_jls;

	public QQList(String uid) throws HeadlessException {
		// TODO Auto-generated constructor stub
		this.uid = uid;
		ufm = UserFrameManage.getInstance();
		cl = new CardLayout();
		jp = new JPanel(cl);
		jp1 = new JPanel(new BorderLayout());
		jp2 = new JPanel(new BorderLayout());
		jp3 = new JPanel(new BorderLayout());

		jp1_jb1 = new JButton("我的好友");
		jp1_jb1.setFont(Const.FONT2);
		jp1_jb1.addActionListener(this);
		jp1_jb2 = new JButton("陌生人");
		jp1_jb2.setFont(Const.FONT2);
		jp1_jb2.addActionListener(this);
		jp1_jb3 = new JButton("黑名单");
		jp1_jb3.setFont(Const.FONT2);
		jp1_jb3.addActionListener(this);

		jp2_jb1 = new JButton("我的好友");
		jp2_jb1.setFont(Const.FONT2);
		jp2_jb1.addActionListener(this);
		jp2_jb2 = new JButton("陌生人");
		jp2_jb2.setFont(Const.FONT2);
		jp2_jb2.addActionListener(this);
		jp2_jb3 = new JButton("黑名单");
		jp2_jb3.setFont(Const.FONT2);
		jp2_jb3.addActionListener(this);

		jp3_jb1 = new JButton("我的好友");
		jp3_jb1.setFont(Const.FONT2);
		jp3_jb1.addActionListener(this);
		jp3_jb2 = new JButton("陌生人");
		jp3_jb2.setFont(Const.FONT2);
		jp3_jb2.addActionListener(this);
		jp3_jb3 = new JButton("黑名单");
		jp3_jb3.setFont(Const.FONT2);
		jp3_jb3.addActionListener(this);

		jp1_jp1 = new JPanel(new GridLayout(2, 1));

		jp1_jls = new JLabel[50];
		jp1_jp2 = new JPanel(new GridLayout(jp1_jls.length, 1, 3, 3));
		for (int i = 0; i < jp1_jls.length; i++) {
			jp1_jls[i] = new JLabel(uid + "的好友" + (i + 1008611), new ImageIcon("image/icon1.png"), JLabel.LEFT);
			if (jp1_jls[i].getText() != uid) {
				jp1_jls[i].setEnabled(false);
			}
			jp1_jls[i].addMouseListener(this);
			jp1_jp2.add(jp1_jls[i]);
		}
		jp1_jsp = new JScrollPane(jp1_jp2);
		jp1_jp1.add(jp1_jb2);
		jp1_jp1.add(jp1_jb3);
		jp1.add(jp1_jb1, "North");
		jp1.add(jp1_jsp, "Center");
		jp1.add(jp1_jp1, "South");

		jp2_jp1 = new JPanel(new GridLayout(2, 1));

		jp2_jls = new JLabel[30];
		jp2_jp2 = new JPanel(new GridLayout(jp2_jls.length, 1, 3, 3));
		for (int i = 0; i < jp2_jls.length; i++) {
			jp2_jls[i] = new JLabel(uid + "的陌生人" + (i + 1), new ImageIcon("image/icon2.png"), JLabel.LEFT);
			jp2_jls[i].addMouseListener(this);
			jp2_jp2.add(jp2_jls[i]);
		}
		jp2_jsp = new JScrollPane(jp2_jp2);
		jp2_jp1.add(jp2_jb1);
		jp2_jp1.add(jp2_jb2);
		jp2.add(jp2_jp1, "North");
		jp2.add(jp2_jsp, "Center");
		jp2.add(jp2_jb3, "South");

		jp3_jp1 = new JPanel(new GridLayout(3, 1));

		jp3_jls = new JLabel[20];
		jp3_jp2 = new JPanel(new GridLayout(jp3_jls.length, 1, 3, 3));
		for (int i = 0; i < jp3_jls.length; i++) {
			jp3_jls[i] = new JLabel(uid + "的黑名单" + (i + 1), new ImageIcon("image/icon3.png"), JLabel.LEFT);
			jp3_jls[i].addMouseListener(this);
			jp3_jp2.add(jp3_jls[i]);
		}
		jp3_jsp = new JScrollPane(jp3_jp2);
		jp3_jp1.add(jp3_jb1);
		jp3_jp1.add(jp3_jb2);
		jp3_jp1.add(jp3_jb3);
		jp3.add(jp3_jp1, "North");
		jp3.add(jp3_jsp, "Center");

		jp.add(jp1, "1");
		jp.add(jp2, "2");
		jp.add(jp3, "3");

		Container c = this.getContentPane();
		c.add(jp);

		this.setSize(250, 600);
		this.setTitle(uid);
		this.setLocation(Const.SCREENWIDTH - 255, 10);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// 关闭socket并通知服务器端
				ClientSocketThread cst = ClientSocketThreadManage.getThread(uid);
				cst.sendLogoutMessage();
				// 销毁所有子QQChat窗口
				HashMap<String, QQChat> chatMap = ufm.getChatMap();
				Iterator<String> it = chatMap.keySet().iterator();
				while (it.hasNext()) {
					QQChat qc = chatMap.get(it.next());
					qc.dispose();
				}
				// 将QQList从管理组中移除
				ufm.removeList((QQList) e.getWindow());
				e.getWindow().dispose();
			}
		});
		this.setVisible(true);

	}

	public void upgradeFriendList(String[] arr, String type) {
		if (type.equals("add")) {
			for (int i = 0; i < arr.length; i++) {
				jp1_jls[Integer.parseInt(arr[i]) - 1008611].setEnabled(true);
			}
		} else if (type.equals("minus")) {
			for (int i = 0; i < arr.length; i++) {
				jp1_jls[Integer.parseInt(arr[i]) - 1008611].setEnabled(false);
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new QQList("1");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(jp1_jb1) || e.getSource().equals(jp2_jb1) || e.getSource().equals(jp3_jb1)) {
			cl.show(jp, "1");
		}
		if (e.getSource().equals(jp1_jb2) || e.getSource().equals(jp2_jb2) || e.getSource().equals(jp3_jb2)) {
			cl.show(jp, "2");
		}
		if (e.getSource().equals(jp1_jb3) || e.getSource().equals(jp2_jb3) || e.getSource().equals(jp3_jb3)) {
			cl.show(jp, "3");
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource()) instanceof JLabel) {
			if (e.getClickCount() == 2) {
				JLabel jl = (JLabel) e.getSource();
				String jlInfo = jl.getText();
				if (jlInfo.indexOf("好友") != -1) {
					String arr[] = jlInfo.split("的好友");
					String no = arr[1];
					String uids = uid + " " + no;
					System.out.println(uids);
					if (ufm.containsChat(uids)) {
						QQChat qc = ufm.getChat(uids);
						qc.setState(JFrame.NORMAL);
						qc.toFront();
					} else {
						QQChat qc = new QQChat(uid, no);
						ufm.addChat(uids, qc);
					}
				} else if (jlInfo.indexOf("陌生人") != -1) {
					String arr[] = jlInfo.split("的陌生人");
					String no = arr[1];
					JOptionPane.showMessageDialog(this, "你希望跟编号为" + no + "的陌生人聊天");
				} else if (jlInfo.indexOf("黑名单") != -1) {
					JOptionPane.showMessageDialog(this, "不允许跟黑名单聊天");
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource()) instanceof JLabel) {
			JLabel jl = (JLabel) e.getSource();
			jl.setForeground(Color.RED);
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if ((e.getSource()) instanceof JLabel) {
			JLabel jl = (JLabel) e.getSource();
			jl.setForeground(Color.BLACK);
		}
	}

}
