package com.qq.db;

import java.sql.*;
import java.util.*;

public class SQLHelper {
	private DbConfig dc = null;
	private Vector<String> dbnames = null;
	private Vector<Map<String, Vector<String>>> tables = null;
	private Connection ct = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	public SQLHelper() {
		try {
			dc = DbConfig.getInstance();
			dbnames = new Vector<String>();
			tables = new Vector<Map<String, Vector<String>>>();
			Class.forName(dc.getDriver()).newInstance();
			ct = DriverManager.getConnection(dc.getUrl(), dc.getUsername(), dc.getPassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 更新库名
	// 更新表名

	// 也可以通过ResultSetMetaData得到列的信息

	/**
	 * 
	 * @param databaseName
	 *            数据库名
	 * @param schema
	 *            架构名，SQLServer中为dbo，oracle中为用户名
	 * @param tablename
	 *            表名
	 * @return 没有主键会返回"''"(使得SQL语句语法正确)，多个主键则返回序列中的第一个
	 */
	public String getPrimaryKey(String databaseName, String schema, String tablename) {
		String primaryKey = "''";
		try {
			DatabaseMetaData dmd = ct.getMetaData();
			ResultSet rs = dmd.getPrimaryKeys(databaseName, schema, tablename);
			if (rs.next()) {
				primaryKey = rs.getString("COLUMN_NAME");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return primaryKey;
	}

	public String getColumnType(String databaseName, String schema, String tablename, String columnName) {
		String columnType = "";
		try {
			DatabaseMetaData dmd = ct.getMetaData();
			ResultSet rs = dmd.getColumns(databaseName, schema, tablename, columnName);
			if (rs.next()) {
				columnType = rs.getString("TYPE_NAME");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnType;
	}

	public Vector<String> getColumnNames(String databaseName, String schema, String tablename) {
		Vector<String> columnNames = new Vector<String>();
		String sql = "select name from syscolumns where id=object_id('" + databaseName + "." + schema + "." + tablename
				+ "') order by colid";
		ResultSet rs = this.query(sql, null);
		try {
			while (rs.next()) {
				columnNames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return columnNames;
	}

	// 仅针对SQLServer
	public void init() {
		String sql = "select name from sys.databases where name not in (?,?,?,?) order by name";
		String paras[] = { "master", "model", "msdb", "tempdb" };
		rs = this.query(sql, paras);
		try {
			while (rs.next()) {
				dbnames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 仅加入用户表
		for (int i = 0; i < dbnames.size(); i++) {
			String dbname = dbnames.get(i);
			sql = "select name from " + dbname + "..sysobjects where xtype='u' order by name";
			rs = this.query(sql, null);
			Hashtable<String, Vector<String>> map = new Hashtable<String, Vector<String>>();
			Vector<String> tablenames = new Vector<String>();
			try {
				while (rs.next()) {
					tablenames.add(rs.getString(1));
				}
				map.put(dbname, tablenames);
				this.tables.add(map);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void showInfo() {
		for (int i = 0; i < tables.size(); i++) {
			Map<String, Vector<String>> map = tables.get(i);
			Set<String> dbset = map.keySet();
			Iterator<String> it = dbset.iterator();
			while (it.hasNext()) {
				String dbname = it.next();
				System.out.println("数据库名：" + dbname);
				Vector<String> tablenames = map.get(dbname);
				for (int j = 0; j < tablenames.size(); j++) {
					String tablename = tablenames.get(j);
					if (j == 0) {
						System.out.print("表名：" + tablename + " ");
					} else {
						System.out.print(tablename + " ");
					}
				}
				System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		SQLHelper helper = new SQLHelper();
		helper.init();
		helper.showInfo();
		helper.close();
	}

	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (ct != null) {
				ct.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createDatabase(String databaseName) {
		try {
			String sql = "create database " + databaseName;
			ps = ct.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void dropDatabase(String databaseName) {
		try {
			String sql = "drop database " + databaseName;
			ps = ct.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void backupDatabase(String databaseName, String filePath) {
		try {
			String sql = "backup database " + databaseName + " to disk='" + filePath + "'";
			ps = ct.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void restoreDatabase(String databaseName, String filePath) {
		try {
			String sql = "restore database " + databaseName + " from disk='" + filePath + "'";
			ps = ct.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 需要三个参数，表名，列名，类型和约束
	public void createTable(String tablename) {

	}

	public void dropTable(String tablename) {

	}

	// insert delete update
	public boolean update(String sql, String[] paras) {
		try {
			ps = ct.prepareStatement(sql);
			if (paras != null) {
				for (int i = 0; i < paras.length; i++) {
					ps.setObject(i + 1, paras[i]);
				}
			}
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	// query:the result set should not be closed before use
	public ResultSet query(String sql, String[] paras) {
		try {
			ps = ct.prepareStatement(sql);
			if (paras != null) {
				for (int i = 0; i < paras.length; i++) {
					ps.setObject(i + 1, paras[i]);
				}
			}
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
