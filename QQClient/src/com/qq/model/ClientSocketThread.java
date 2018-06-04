package com.qq.model;

import java.io.*;
import java.net.Socket;
import com.qq.common.Message;
import com.qq.common.Package;
import com.qq.common.PackageSource;
import com.qq.common.PackageType;
import com.qq.view.QQChat;
import com.qq.view.QQList;

public class ClientSocketThread extends Thread {
	private Socket s;
	private UserFrameManage ufm;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ClientSocketThread(Socket s) {
		this.s = s;
		ufm = UserFrameManage.getInstance();
	}

	public boolean sendMessage(Package p) {
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(p);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendLogoutMessage() {
		Package p = new Package(PackageType.logout_request, PackageSource.client);
		try {
			oos = new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(p);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void run() {
		try {
			while (true) {
				ois = new ObjectInputStream(s.getInputStream());
				Package p = (Package) ois.readObject();
				String type = p.getType();
				String source = p.getSource();
				if (type.equals(PackageType.message_common)) {
					Message m = (Message) p.getPlusObject();
					System.out.println(m.getSender() + " " + m.getReceiver() + " " + m.getContent());
					String sender = m.getSender();
					String receiver = m.getReceiver();
					String uids = null;
					QQChat qc = null;
					if (source.equals(PackageSource.server)) {
						uids = sender + " " + receiver;
						if (ufm.containsChat(uids)) {
							qc = ufm.getChat(uids);
						} else {
							// 强行帮对面点开聊天框，但如果对面不在同一主机，又不在线呢？
							qc = new QQChat(sender, receiver);
							ufm.addChat(uids, qc);
						}
					} else {
						uids = receiver + " " + sender;
						if (ufm.containsChat(uids)) {
							qc = ufm.getChat(uids);
						} else {
							// 强行帮对面点开聊天框，但如果对面不在同一主机，又不在线呢？
							qc = new QQChat(receiver, sender);
							ufm.addChat(uids, qc);
						}
					}
					// 显示信息的任务应该交给界面本身去完成
					qc.showMessage(m);
				} else if (type.equals(PackageType.logout_request) && source.equals(PackageSource.server)) {
					System.out.println("server stopped,quitting now");
					s.close();
					// 可能存在并发问题
					synchronized (s) {
						ClientSocketThreadManage.removeThread(this);
					}
					break;
				} else if (type.equals(PackageType.friendList_response)) {
					Message m = (Message) p.getPlusObject();
					String mtype = m.getType();
					String uid = m.getReceiver();
					String onLineFriendsId = m.getContent();
					String arr[] = onLineFriendsId.trim().split(" ");
					QQList ql = ufm.getList(uid);
					String sender = m.getSender();
					System.out.println(sender + " " + mtype + " " + uid);
					if (ql != null)
						ql.upgradeFriendList(arr, mtype);
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