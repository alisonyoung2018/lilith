package com.qq.model;

import java.io.*;
import java.net.*;
import com.qq.common.User;
import com.qq.common.Package;
import com.qq.common.PackageSource;
import com.qq.common.PackageStatus;
import com.qq.common.PackageType;

public class ServerConnector {
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	public int checkUser(User user) {
		int status = 0;
		try {
			s = new Socket("127.0.0.1", 9999);
			oos = new ObjectOutputStream(s.getOutputStream());
			Package p = new Package(PackageType.login_request, PackageSource.client, user);
			oos.writeObject(p);
			ois = new ObjectInputStream(s.getInputStream());
			p = (Package) ois.readObject();
			if (p.getStatus() == PackageStatus.success) {
				status = 1;
				ClientSocketThread st = new ClientSocketThread(s);
				st.start();
				String uid = ((User) p.getPlusObject()).getUid();
				ClientSocketThreadManage.addThread(uid, st);
			} else {
				if (p.getStatus() == PackageStatus.already_exist) {
					status = 0;
				} else {
					status = 2;
				}
				s.close();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = -1;
		} catch (ConnectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = -1;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = -1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = -1;
		}
		return status;
	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}
}
