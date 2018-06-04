package com.qq.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.qq.common.User;
import com.qq.db.SQLHelper;

public class ServerUserModel {

	public static boolean checkUser(User user) {
		boolean b = false;
		String sql = "select * from qq.dbo.users where accountid=? and accountpw=?";
		String paras[] = new String[] { user.getUid(), user.getPasswd() };
		SQLHelper helper = new SQLHelper();
		ResultSet rs = helper.query(sql, paras);
		try {
			if (rs.next()) {
				b = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			helper.close();
		}
		return b;
	}
}
