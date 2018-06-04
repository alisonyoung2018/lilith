package com.qq.model;

import java.text.SimpleDateFormat;
import java.util.*;

import com.qq.view.QQChat;
import com.qq.view.QQList;

public class UserFrameManage implements Runnable {
	private static UserFrameManage instance;
	private static HashMap<String, QQList> listMap;
	private static HashMap<String, QQChat> chatMap;

	private UserFrameManage() {
		listMap = new HashMap<String, QQList>();
		chatMap = new HashMap<String, QQChat>();
	}

	private static UserFrameManage getNewManager() {
		if (instance == null) {
			System.out.println("new instance created");
			instance = new UserFrameManage();
		}
		return instance;
	}

	public static UserFrameManage getInstance() {
		return getNewManager();
	}

	public void addList(String uid, QQList ql) {
		listMap.put(uid, ql);
	}

	public void addChat(String uids, QQChat qc) {
		chatMap.put(uids, qc);
	}

	public QQList getList(String uid) {
		return listMap.get(uid);
	}

	public QQChat getChat(String uids) {
		return chatMap.get(uids);
	}

	public void removeList(String uid) {
		listMap.remove(uid);
	}

	public void removeList(QQList ql) {
		Set<String> keySet = listMap.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String uid = it.next();
			QQList temp = listMap.get(uid);
			if (temp.equals(ql)) {
				listMap.remove(uid);
				break;// 仅针对一一对应的Map
			}
		}
	}

	public void removeChat(String uids) {
		chatMap.remove(uids);
	}

	public void removeChat(QQChat qc) {
		Set<String> keySet = chatMap.keySet();
		Iterator<String> it = keySet.iterator();
		while (it.hasNext()) {
			String uids = it.next();
			QQChat temp = chatMap.get(uids);
			if (temp.equals(qc)) {
				chatMap.remove(uids);
				break;// 仅针对一一对应的Map
			}
		}
	}

	public boolean containsList(String uid) {
		return listMap.containsKey(uid);
	}

	public boolean containsList(QQList ql) {
		return listMap.containsValue(ql);
	}

	public boolean containsChat(String uids) {
		return chatMap.containsKey(uids);
	}

	public boolean containsChat(QQChat qc) {
		return chatMap.containsValue(qc);
	}

	public HashMap<String, QQList> getListMap() {
		return listMap;
	}

	public HashMap<String, QQChat> getChatMap() {
		return chatMap;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int n = 0;
		while (n < 10000) {
			try {
				Thread.sleep(5000);
				System.out.println("listSize = " + listMap.size() + " @ "
						+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			n++;
		}
	}

	public static void main(String[] args) {
		new Thread(UserFrameManage.getInstance()).start();
	}
}
