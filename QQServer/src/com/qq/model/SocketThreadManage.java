package com.qq.model;

import java.util.*;

public class SocketThreadManage {

	private static HashMap<String, SocketThread> threadMap = new HashMap<String, SocketThread>();

	public static HashMap<String, SocketThread> getThreadMap() {
		return threadMap;
	}

	public static void addThread(String uid, SocketThread st) {
		threadMap.put(uid, st);
	}

	public static SocketThread getThread(String uid) {
		return threadMap.get(uid);
	}

	public static void removeThread(String uid) {
		threadMap.remove(uid);
	}

	public static void removeThread(SocketThread st) {
		if (threadMap.containsValue(st)) {
			Set<String> uidSet = threadMap.keySet();
			Iterator<String> it = uidSet.iterator();
			while (it.hasNext()) {
				String uid = it.next();
				SocketThread temp = threadMap.get(uid);
				if (temp.equals(st)) {
					threadMap.remove(uid);
					break;
				}
			}
		}
	}

	public static boolean containsThread(String uid) {
		return threadMap.containsKey(uid);
	}

	public static boolean containsThread(SocketThread st) {
		return threadMap.containsValue(st);
	}

	public static String getOnLineFriendsId(String uid) {
		String str = "";
		Iterator<String> it = threadMap.keySet().iterator();
		while (it.hasNext()) {
			str += it.next() + " ";
		}
		return str;
	}

	public static String getKeyId(SocketThread st) {
		String str = "";
		if (containsThread(st)) {
			Iterator<String> it = threadMap.keySet().iterator();
			while (it.hasNext()) {
				String uid = it.next();
				SocketThread temp = threadMap.get(uid);
				if (temp.equals(st)) {
					str = uid;
					break;
				}
			}
		}
		return str;
	}

}
