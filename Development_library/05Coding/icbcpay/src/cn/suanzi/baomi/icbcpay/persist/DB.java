// ---------------------------------------------------------
// @author    Weiping Liu
// @version   1.0.0
// @copyright 版权所有 (c) 2013 杭州算子科技有限公司 保留所有版权
// ---------------------------------------------------------

package cn.suanzi.baomi.icbcpay.persist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * 数据库相关操作。
 * 
 * @author Weiping Liu
 * @version 1.0.0
 * @since 1.0.0
 */
public class DB {
	private static Logger log = Logger.getLogger(DB.class);
	
	private static String HOST = "jdbc:mysql://suanzi.cn:3306/icbcpay?useUnicode=true&characterEncoding=utf8&autoReconnect=true";
	private static String USER = "icbcpay_test";
	private static String PASS = "icbcpay@sz@2015";
	
	/** 获取一个mysql链接   */
	public static Connection getConn() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			log.error("DB driver not found: " + e.getMessage());
		}
		
		conn = DriverManager.getConnection(HOST, USER, PASS);
		return conn;
	}
	
	
	/**
	 * 关闭ResultSet
	 * @param rs
	 */
	public static void closeRs(ResultSet rs) {
		 if (rs != null) {
			 try {
				 rs.close();
			 } catch (SQLException ex) {
	         }
		 }
	}
	
	/**
	 * 关闭statement。
	 * @param st
	 */
	public static void closeSt(Statement st) {
		 if (st != null) {
			 try {
				 st.close();
			 } catch (SQLException ex) {
				 log.error(ex.getMessage());
	         }
		 }
	}
	

	/**
	 * 关闭statement。
	 * @param st
	 */
	public static void closeCn(Connection cn) {
		 if (cn != null) {
			 try {
				 cn.close();
				 cn = null;
			 } catch (SQLException ex) {
	         }
		 }
	}

}