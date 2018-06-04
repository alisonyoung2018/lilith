package com.qq.model;

import java.util.*;

public class ClientSocketThreadManage implements Runnable {
	private static HashMap<String, ClientSocketThread> threadMap = new HashMap<String, ClientSocketThread>();
	private boolean alive = true;

	public static HashMap<String, ClientSocketThread> getThreadMap() {
		return threadMap;
	}

	public static void addThread(String uid, ClientSocketThread st) {
		threadMap.put(uid, st);
	}

	public static ClientSocketThread getThread(String uid) {
		return threadMap.get(uid);
	}

	public static void removeThread(String uid) {
		threadMap.remove(uid);
	}

	public static void removeThread(ClientSocketThread cst) {
		if (threadMap.containsValue(cst)) {
			Set<String> uidSet = threadMap.keySet();
			Iterator<String> it = uidSet.iterator();
			while (it.hasNext()) {
				String uid = it.next();
				ClientSocketThread temp = threadMap.get(uid);
				if (temp.equals(cst)) {
					threadMap.remove(uid);
					break;
				}
			}
		}
	}

	public static boolean containsThread(String uid) {
		return threadMap.containsKey(uid);
	}

	public static boolean containsThread(ClientSocketThread cst) {
		return threadMap.containsValue(cst);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}
