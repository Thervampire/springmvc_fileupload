package com.sxt.utils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据库工具类
 * @author Administrator
 */
public class DBUtils {
	
	private static ComboPooledDataSource  ds=new ComboPooledDataSource("mysql");
	
	/**
	 * 封装一个方法返回一个连接对象
	 */
	public static Connection getConnection(){
		Connection conn=null;
		try {
			conn=ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 封装一个根据conn得到一个操作数据库的对象
	 */
	public static PreparedStatement  prepareStatement(Connection conn,String sql,Object[] objs){
		System.out.println("执行:"+sql);
		PreparedStatement pstmt=null;
		try {
			pstmt=conn.prepareStatement(sql);
			//设置占位符
			if(null!=objs){
				for (int i = 0; i < objs.length; i++) {
					pstmt.setObject(i+1, objs[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pstmt;
	}
	
	/**
	 * 封装一个返回结果集
	 */
	public static ResultSet executeQuery(PreparedStatement pstmt){
		ResultSet rs=null;
		try {
			rs=pstmt.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	/**
	 * 封装一个可以执行DML语句的方法
	 */
	public static void executeUpdate(String sql,Object[] objs){
		Connection conn=getConnection();
		PreparedStatement pstmt=prepareStatement(conn, sql,objs);
		//执行
		try {
			int i = pstmt.executeUpdate();
			if(i>0){
				System.out.println("操作成功");
			}else{
				System.out.println("操作失败");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close(pstmt, conn);
		}
	}
	
	
	/**
	 * 封装一个可以根据id查询一个对象的方法
	 * 
	 */
	public static <T> T getBean(String sql,Object[] objs,Class<T> cls){
		T bean=null;
		Connection conn=DBUtils.getConnection();
		PreparedStatement pstmt=DBUtils.prepareStatement(conn, sql, objs);
		ResultSet rs=DBUtils.executeQuery(pstmt);
		try {
			if(rs.next()){
				//通过反射的方法创建一个对象
				bean=cls.newInstance();
				//得到数据库的结构
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 0; i < md.getColumnCount(); i++) {
					//取出数据库的字段名  注意转成小写
					String columnName = md.getColumnName(i+1).toLowerCase();
					//根据字段名对出对应字段的值
					Object columnValue=rs.getObject(columnName);
					//如果值为空  就不用给java的对象的属性去设置值
					if(null==columnValue){
						continue;//结束当前循环
					}
					//如果不为空，就得到值的数据类型
					String type=columnValue.getClass().getSimpleName();
					switch (type) {
					case "int":
					case "Integer":
					case "BigDecimal":
						columnValue=rs.getInt(columnName);//处理数据库返回的类型不一样的问题
						break;
					case "Date":
						columnValue=rs.getDate(columnName);
						break;
					case "Timestamp":
						columnValue=rs.getTimestamp(columnName);
						break;
					}
					//根据数据库里面的字段名columnName得到bean对象里面的属性
					Field field=bean.getClass().getDeclaredField(columnName);
					//暴力解除属性的访问权限
					field.setAccessible(true);
					//给属性赋值
					field.set(bean, columnValue);
					//System.out.println(columnName+" "+columnValue+"  "+columnValue.getClass().getSimpleName());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtils.close(rs, pstmt, conn);
		}
		return bean;
	}
	
	
	/**
	 * 创建一个DQL的查询返回集合的方法
	 */
	public static <T> List<T> getList(String sql,Object[] objs,Class<T> cls){
		List<T> list=new ArrayList<>();
		//得到连接
		Connection conn=getConnection();
		//得到发送sql的对象
		PreparedStatement pstmt=prepareStatement(conn, sql, objs);
		//得到结果集
		ResultSet rs=executeQuery(pstmt);
		try {
			while(rs.next()){
				//创建一个对象
				T t=cls.newInstance();
				//得到数据库的结构
				ResultSetMetaData md = rs.getMetaData();
				for (int i = 0; i < md.getColumnCount(); i++) {
					//得到字段名 并转成小写
					String columnName = md.getColumnName(i+1).toLowerCase();
					//根据字段名得到字段值
					Object columnValue = rs.getObject(columnName);
					//判断值是否为空
					if(null==columnValue){
						continue;//结束本次循环
					}
					//如果不空，得到数据类型
					String type=columnValue.getClass().getSimpleName();
					switch (type) {
					case "int":
					case "Integer":
					case "BigDecimal":
						columnValue=rs.getInt(columnName);
						break;
					}
					//根据字段名利到类里面的属性
					Field field=cls.getDeclaredField(columnName);
					//暴力解除访问权限
					field.setAccessible(true);
					//给这个属性赋值
					field.set(t, columnValue);
				}
				//把对象加入到集合
				list.add(t);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			close(rs, pstmt, conn);
		}
		return list;
	}
	
	/**
	 * 关闭的方法1
	 * 
	 */
	public static void close(ResultSet rs,PreparedStatement pstmt,Connection conn){
		close(rs);
		close(pstmt, conn);
	}
	
	/**
	 * 关闭的方法2
	 * 
	 */
	public static void close(PreparedStatement pstmt,Connection conn){
		close(pstmt);
		close(conn);
	}
	
	/**
	 * 关闭的方法3
	 * 
	 */
	public static void close(AutoCloseable closeable){
		if(null!=closeable){
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
