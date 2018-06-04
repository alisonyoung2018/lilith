package com.qq.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

import com.qq.common.Message;
import com.qq.common.Package;
import com.qq.common.PackageSource;
import com.qq.common.PackageType;

public class SocketThread extends Thread {
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public SocketThread(Socket s) {
		this.s = s;
	}

	public void notifyOthersOnLine(String myId) {
		HashMap<String, SocketThread> hm = SocketThreadManage.getThreadMap();
		Iterator<String> it = hm.keySet().iterator();
		Message m = new Message();
		m.setType("add");
		m.setContent(myId);
		m.setSender("friendList update");
		while (it.hasNext()) {
			String uid = it.next();
			try {
				oos = new ObjectOutputStream(hm.get(uid).getS().getOutputStream());
				m.setReceiver(uid);
				Package p = new Package(PackageType.friendList_response, PackageSource.server, m);
				oos.writeObject(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void notifyOthersOffLine(String myId) {
		HashMap<String, SocketThread> hm = SocketThreadManage.getThreadMap();
		Iterator<String> it = hm.keySet().iterator();
		Message m = new Message();
		m.setType("minus");
		m.setContent(myId);
		m.setSender("friendList update");
		while (it.hasNext()) {
			String uid = it.next();
			try {
				oos = new ObjectOutputStream(hm.get(uid).getS().getOutputStream());
				m.setReceiver(uid);
				Package p = new Package(PackageType.friendList_response, PackageSource.server, m);
				oos.writeObject(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void run() {
		try {
			while (true) {
				ois = new ObjectInputStream(s.getInputStream());
				Package p = (Package) ois.readObject();
				String type = p.getType();
				String source = p.getSource();
				if (type.equals(PackageType.message_common) && source.equals(PackageSource.client)) {
					Message m = (Message) p.getPlusObject();
					String sender = m.getSender();
					String receiver = m.getReceiver();
					System.out.println(sender + " " + receiver + " " + m.getContent());
					// 这里要考虑对方不在线的情况
					if (SocketThreadManage.containsThread(receiver)) {
						SocketThread st = SocketThreadManage.getThread(m.getReceiver());
						oos = new ObjectOutputStream(st.s.getOutputStream());
						oos.writeObject(p);
					} else {// 不在线就返回去
						p.setSource(PackageSource.server);
						m.setSender("server");
						m.setReceiver(sender);
						m.setContent(receiver + " is not available now");
						m.setSendTime(
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
						oos = new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(p);
					}
				} else if (type.equals(PackageType.logout_request) && source.equals(PackageSource.client)) {
					System.out.println("client socket closing,quitting");
					// 通知所有客户端更新好友列表
					String uid = SocketThreadManage.getKeyId(this);
					this.notifyOthersOffLine(uid);
					// 给client返回一个包通知其关闭socket
					p = new Package(PackageType.logout_request, PackageSource.server);
					oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(p);
					s.close();
					synchronized (s) {
						// 退出前记得将自身从thread组中移除
						SocketThreadManage.removeThread(this);
					}
					break;
				} else if (type.equals(PackageType.friendList_request)) {
					System.out.println("friendList request accepted");
					String uid = p.getPlusObject().toString();
					String onLineFriendsId = SocketThreadManage.getOnLineFriendsId(uid);
					p.setType(PackageType.friendList_response);
					p.setSource(PackageSource.server);
					Message m = new Message();
					m.setType("add");
					m.setSender("friendList response");
					m.setReceiver(uid);
					m.setContent(onLineFriendsId);
					p.setPlusObject(m);
					oos = new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(p);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Socket getS() {
		return s;
	}
}