package com.qq.view;

import javax.swing.*;

import com.qq.model.QQServer;
import com.qq.tool.Const;
import com.qq.tool.ImagePanel;

import java.awt.*;
import java.awt.event.*;

public class ServerManageFrame extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6921682905134726753L;
	private JPanel jp1, jp2;
	private JScrollPane jsp1;
	private JButton jp1_jb1, jp1_jb2;

	public ServerManageFrame() throws HeadlessException {
		// TODO Auto-generated constructor stub
		jp1 = new JPanel();
		jp1_jb1 = new JButton("启动服务器");
		jp1_jb1.setFont(Const.FONT1);
		jp1_jb1.addActionListener(this);
		jp1_jb2 = new JButton("关闭服务器");
		jp1_jb2.setFont(Const.FONT1);
		jp1_jb2.addActionListener(this);
		jp1.add(jp1_jb1);
		jp1.add(jp1_jb2);

		jp2 = new ImagePanel("image/yanghuan.jpg");
		jsp1 = new JScrollPane(jp2);

		Container c = this.getContentPane();
		c.add(jp1, "North");
		c.add(jsp1, "Center");

		this.setSize(800, 600);
		this.setTitle("服务器管理界面");
		this.setLocationRelativeTo(null);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				// 在dispose前，应当执行stop操作
				QQServer.stopServer();
				e.getWindow().dispose();
			}

		});
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(jp1_jb1)) {
			QQServer.startServer();
		}
		if (e.getSource().equals(jp1_jb2)) {
			QQServer.stopServer();
		}
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ServerManageFrame smf = new ServerManageFrame();
	}
}
