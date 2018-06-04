package com.qq.model;

import java.io.*;
import java.net.*;
import java.util.*;
import com.qq.common.Package;
import com.qq.common.PackageSource;
import com.qq.common.PackageStatus;
import com.qq.common.PackageType;
import com.qq.common.User;

/**
 * 一个Thread类只能start一次，要使得server能反复开启和关闭，必须使用runnable
 * 
 * @author Administrator
 *
 */
public class QQServer implements Runnable {
	private static Thread t;
	private static QQServer instance;
	private static ServerSocket ss;
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	private QQServer() {

	}

	private static QQServer getNewServer() {
		if (instance == null) {
			instance = new QQServer();
		}
		return instance;
	}

	public static QQServer getInstance() {
		return QQServer.getNewServer();
	}

	public static void startServer() {
		if (instance == null) {
			QQServer.getInstance();
			t = new Thread(instance);
			t.start();
		} else if (t.isAlive()) {
			System.out.println("server is running");
		} else {
			t = new Thread(instance);
			t.start();
		}
	}

	public static void stopServer() {
		if (instance == null) {
			System.out.println("no server exists");
		} else if (t.isAlive()) {
			Package p = new Package(PackageType.logout_request, PackageSource.controller);
			try {
				Socket s = new Socket("127.255.255.254", 9999);
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(p);
				s.close();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("notified the server");
		} else {
			System.out.println("server has been stopped");
		}
	}

	public static boolean notifyAllThreads() {
		boolean b = false;
		Package p = new Package(PackageType.logout_request, PackageSource.server);
		HashMap<String, SocketThread> threadMap = SocketThreadManage.getThreadMap();
		Iterator<String> it = threadMap.keySet().iterator();
		try {
			while (it.hasNext()) {
				Socket s = threadMap.get(it.next()).getS();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(p);
				s.close();
			}
			System.out.println("notified all socket threads");
			b = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 把while放在try里面，以便在异常发生时终止线程
		try {
			ss = new ServerSocket(9999);
			System.out.println("listening port 9999");
			while (!ss.isClosed()) {
				s = ss.accept();
				ois = new ObjectInputStream(s.getInputStream());
				oos = new ObjectOutputStream(s.getOutputStream());
				Package p = (Package) ois.readObject();
				String type = p.getType();
				String source = p.getSource();
				if (type.equals(PackageType.login_request) && source.equals(PackageSource.client)) {
					User user = (User) p.getPlusObject();
					String uid = user.getUid();
					System.out.println(uid + " " + user.getPasswd());
					p.setType(PackageType.login_response);
					if (ServerUserModel.checkUser(user)) {
						if (SocketThreadManage.containsThread(uid)) {
							p.setStatus(PackageStatus.already_exist);
							p.setSource(PackageSource.server);
							oos.writeObject(p);
							s.close();
						} else {
							p.setStatus(PackageStatus.success);
							p.setSource(PackageSource.server);
							oos.writeObject(p);
							SocketThread st = new SocketThread(s);
							st.start();
							st.notifyOthersOnLine(uid);
							SocketThreadManage.addThread(uid, st);
						}
					} else {
						p.setStatus(PackageStatus.fail);
						p.setSource(PackageSource.server);
						oos.writeObject(p);
						s.close();
					}
				} else if (type.equals(PackageType.logout_request) && source.equals(PackageSource.controller)) {
					s.close();
					notifyAllThreads();
					ss.close();
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("server thread is over");
	}

	public static ServerSocket getSs() {
		return ss;
	}
}
