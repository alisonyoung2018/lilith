package com.qq.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.qq.common.User;
import com.qq.common.Package;
import com.qq.common.PackageSource;
import com.qq.common.PackageType;
import com.qq.model.ClientSocketThread;
import com.qq.model.ClientSocketThreadManage;
import com.qq.model.ServerConnector;
import com.qq.model.UserFrameManage;
import com.qq.tool.StringCheck;

public class QQLogin extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8482670516802703067L;
	private UserFrameManage ufm;
	private JLabel jl1, jl2, jl3, jl4, jl5 = null;
	private JButton jb1, jb2, jb3, jb4 = null;
	private JTextField jtf = null;
	private JPasswordField jpf = null;
	private JCheckBox jcb1, jcb2 = null;
	private JPanel jp1, jp2, jp3, jp4, jp5 = null;
	private JTabbedPane jtp = null;

	public QQLogin() {
		ufm = UserFrameManage.getInstance();
		jp1 = new JPanel();
		jl1 = new JLabel(new ImageIcon("image/jl1.png"), JLabel.CENTER);
		jp1.add(jl1);

		jp5 = new JPanel();
		jb1 = new JButton(new ImageIcon("image/jb1.png"));
		jb1.addActionListener(this);
		jb2 = new JButton(new ImageIcon("image/jb2.png"));
		jb2.addActionListener(this);
		jb3 = new JButton(new ImageIcon("image/jb3.png"));
		jb3.addActionListener(this);
		jp5.add(jb1);
		jp5.add(jb2);
		jp5.add(jb3);

		jtp = new JTabbedPane();

		jp2 = new JPanel(new GridLayout(3, 3, 10, 10));

		jl2 = new JLabel("QQ∫≈¬Î", JLabel.CENTER);
		jtf = new JTextField(10);
		jb4 = new JButton(new ImageIcon("image/jb4.png"));
		jb4.addActionListener(this);
		jl3 = new JLabel("QQ√‹¬Î", JLabel.CENTER);
		jpf = new JPasswordField(10);
		jl4 = new JLabel("Õ¸º«√‹¬Î", JLabel.CENTER);
		jl4.setFont(new Font("ÀŒÃÂ", Font.PLAIN, 15));
		jl4.setForeground(Color.BLUE);
		jcb1 = new JCheckBox("“˛…Ìµ«¬º");
		jcb2 = new JCheckBox("º«◊°√‹¬Î");
		jl5 = new JLabel("<html><a href='www.baidu.com'>…Í«Î√‹¬Î±£ª§</a></html>", JLabel.CENTER);
		jl5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		jp2.add(jl2);
		jp2.add(jtf);
		jp2.add(jb4);
		jp2.add(jl3);
		jp2.add(jpf);
		jp2.add(jl4);
		jp2.add(jcb1);
		jp2.add(jcb2);
		jp2.add(jl5);

		jp3 = new JPanel();
		jp3.setBackground(new Color(255, 0, 0));
		jp4 = new JPanel();
		jp4.setBackground(new Color(0, 0, 255));

		jtp.add("QQ∫≈¬Î", jp2);
		jtp.add(" ÷ª˙∫≈¬Î", jp3);
		jtp.add("µÁ◊”” œ‰", jp4);

		this.add(jp1, BorderLayout.NORTH);
		this.add(jtp, BorderLayout.CENTER);
		this.add(jp5, BorderLayout.SOUTH);

		this.setSize(400, 300);
		this.setResizable(false);
		this.setTitle("Tencent QQ");
		this.setLocationRelativeTo(null);
		this.setIconImage(new ImageIcon("image/qqicon.png").getImage());
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				e.getWindow().dispose();
			}
		});
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new QQLogin();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(jb1)) {
			String uid = jtf.getText().trim();
			if (!StringCheck.check(uid)) {
				JOptionPane.showMessageDialog(this, "QQ∫≈¬Î∏Ò Ω¥ÌŒÛ");
				return;
			} else {
				String passwd = new String(jpf.getPassword());
				User user = new User();
				user.setUid(uid);
				user.setPasswd(passwd);
				ServerConnector sc = new ServerConnector();
				int status = sc.checkUser(user);
				if (status == 1) {
					QQList ql = new QQList(uid);
					ufm.addList(uid, ql);
					Package p = new Package(PackageType.friendList_request, PackageSource.client, uid);
					ClientSocketThread cst = ClientSocketThreadManage.getThread(uid);
					cst.sendMessage(p);
					this.dispose();
				} else if (status == 2) {
					JOptionPane.showMessageDialog(this, "’À∫≈ªÚ√‹¬Î¥ÌŒÛ");
				} else if (status == -1) {
					JOptionPane.showMessageDialog(this, "¡¨Ω”¥ÌŒÛ");
				} else if (status == 0) {
					JOptionPane.showMessageDialog(this, "ƒ„“—æ≠µ«¬º");
					System.out.println(ufm.containsList(uid));
				}
			}
		}
		if (e.getSource().equals(jb2)) {

		}
		if (e.getSource().equals(jb3)) {

		}
		if (e.getSource().equals(jb4)) {
			jtf.setText("");
		}
	}

}
