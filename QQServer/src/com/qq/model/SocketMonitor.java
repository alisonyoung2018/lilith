package com.qq.model;

import java.net.*;
import java.io.*;
import java.util.*;

import com.qq.common.Message;

public class SocketMonitor implements Runnable {
	@SuppressWarnings("unused")
	private static HashMap<String, Socket> socketMap;
	private boolean alive;
	private ServerSocket ss;
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public SocketMonitor() {
		// TODO Auto-generated constructor stub
		socketMap = new HashMap<String, Socket>();
		alive = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ss = new ServerSocket(10000);
			while (alive) {
				s = ss.accept();
				ois = new ObjectInputStream(s.getInputStream());
				Object o = ois.readObject();
				if (o instanceof Message) {
					@SuppressWarnings("unused")
					Message mr = (Message) o;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (ois != null) {
					oos.close();
				}
				if (s != null) {
					s.close();
				}
				if (ss != null) {
					ss.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

}
