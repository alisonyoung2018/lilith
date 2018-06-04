package com.qq.db;

import java.io.*;
//import java.net.URISyntaxException;
import java.util.*;

public class DbConfig {
	private static final String ACTIONPATH = "database.properties";
	private static DbConfig instance = null;

	private String dbtype = null;
	private String driver = null;
	private String url = null;
	private String username = null;
	private String password = null;

	public static DbConfig getInstance() {
		if (instance == null) {
			instance = new DbConfig().getNewDbConfig();
		}
		return instance;
	}

	private DbConfig() {
		// TODO Auto-generated constructor stub
	}

	private DbConfig getNewDbConfig() {
		DbConfig dc = new DbConfig();
		Properties prop = new Properties();
		// String path = null;
		FileInputStream fis = null;

		try {
			// path =
			// DbConfig.class.getClassLoader().getResource("").toURI().getPath();
			fis = new FileInputStream(new File(ACTIONPATH));
			prop.load(fis);
			dc.setDbtype(prop.getProperty("dbtype"));
			dc.setDriver(prop.getProperty("driver"));
			dc.setUrl(prop.getProperty("url"));
			dc.setUsername(prop.getProperty("username"));
			dc.setPassword(prop.getProperty("password"));
			// } catch (URISyntaxException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return dc;
	}

	public String getDbtype() {
		return dbtype;
	}

	public void setDbtype(String dbtype) {
		this.dbtype = dbtype;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DbConfig dc = DbConfig.getInstance();
		System.out.println(dc.getUrl());
	}

}
