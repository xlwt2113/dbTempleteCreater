package com.ssj.jdbcfront.dao;

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ssj.jdbcfront.exception.AppException;
import com.ssj.jdbcfront.model.DataBaseView;
import com.ssj.util.LogUtil;
import com.ssj.util.clazz.MyClassLoader;

public class DbUtil {

	public static void main(String[] args) throws Exception{
		Connection con = null;
		con = getConnection("E:/workspace/csmis/webroot/WEB-INF/lib/mysql-connector-java-5.0.5.jar",
				"org.gjt.mm.mysql.Driver","jdbc:mysql://127.0.0.1:3306/dbblog",
				"root","root");
		con = getConnection("E:/workspace/csmis/webroot/WEB-INF/lib/classes12.jar",
				"oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@127.0.0.1:1521:csmisdb",
				"xfcsmis","csmis");
//		showDbInfo(con);
		con.setAutoCommit(false);
//		PreparedStatement st = con.prepareStatement("insert into bbbb(bbb) values(?)");
//		ByteArrayInputStream in = new ByteArrayInputStream(new byte[]{0x23,0x35,0x24,0x10});
//		st.setAsciiStream(1, in,3);
//		st.execute();
		Statement st = con.createStatement();
//		st.executeUpdate("delete from friend");
//		for(int i=0;i<1000000;i++){
//			st.executeUpdate("insert into friend values('"+i+"_1','id','name',null,0)");
//			if(i%100==0){
//				con.commit();
//				System.out.println("insert "+i+" rows");
//			}
//		}
		ResultSet rs = st.executeQuery("select bbb from bbbb");
		rs.next();
		Object obj = rs.getObject(1);
		Clob c = (Clob)obj;
		Reader in = c.getCharacterStream();
		char[] buff = new char[800];
		int len = in.read(buff);
		String s = new String(buff,0,len);
		System.out.println(s);
		rs.close();
		st.close();
		con.close();
	}
	
	static void showDbInfo(Connection con) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SQLException{
		DatabaseMetaData md = con.getMetaData();
		for(Method m:DatabaseMetaData.class.getDeclaredMethods()){
			if(Modifier.isPublic(m.getModifiers())){
				m.getAnnotations();
				if(m.getParameterTypes().length==0){
					System.out.println(m.getName()+"\t:"+m.invoke(md, new Object[0]));
				}
			}
		}
	}
	
	static MyClassLoader cl = new MyClassLoader();

	static Connection mainConn;
	public static boolean connected(){
		return mainConn!=null;
	}
	public static Connection getConnection() throws Exception{
		if(mainConn==null||mainConn.isClosed()){
			mainConn = createConnection();
		}
		return mainConn;
	}
	public static DataBaseView dbv;
	public static Connection createConnection() throws Exception{
		if(dbv==null){
			throw new Exception("δѡ�����ݿ⣡");
		}
		String driver = dbv.getDriver();
		String url = dbv.getJdbcUrl();
		String user = dbv.getUserName();
		String pwd = dbv.getPassword();
		String driverClass = dbv.getDbType().getDriverClass();
		return getConnection(driver,driverClass,url,user,pwd);
	}
	
	public static Connection getConnection(String dbUrl,String driveName,String userName,String userPwd) throws Exception{

        return getConnection(null,driveName,dbUrl,userName,userPwd);
	}

	public static Class<?> loadClass(String className) throws Exception{
		return loadClass(className,null);
	}
	
	public static Class<?> loadClass(String className,String drivePath) throws Exception{
		Class<?> cla = null;
		try {
			cla = System.class.getClassLoader().loadClass(className);
		} catch (Exception e) {
		}
		if(cla==null){
	        //Load JDBC driver
			if(drivePath!=null){
				cl.addPath(drivePath);
			}
	        cla = cl.loadClass(className);
		}
		if(cla==null){
			if(cla==null){
				throw new Exception("�Ҳ�����["+className+"]");
			}
		}
		return cla;
	}

	public static Connection getConnection(String drivePath,String driveName,String dbUrl,String userName,String userPwd) throws Exception{
		Class<?> cla = loadClass(driveName,drivePath);
        Driver d = (Driver) cla.newInstance();
        Properties p = new Properties();
        p.setProperty("user", userName);
        p.setProperty("password", userPwd);
        Connection con = d.connect(dbUrl, p);
        return con;
	}

	/**
	* �����ˣ���˧��
	* ��������: 2007-8-20  ����10:49:25
	* ��������: ��SQL����ѯ��������������RowMapper�ӿڰ�Object[]����ת����ָ������
	* �����Ĳ����ͷ���ֵ: 
	* @param sql		��ѯ��SQL���
	* @param mapper		ӳ�������ӿ�
	* @return
	*/
	public static List<?> executeSqlQuery(String sql,RowMapper mapper){

		List<Object> list = new ArrayList<Object>();
		Connection con = null;
		try{
			con = createConnection();
			if(con==null){
				throw new AppException("���ݿ���δ���ӣ�");
			}
			Statement st = con.createStatement();
			LogUtil.logSql(sql);
			ResultSet rs = st.executeQuery(sql);
			int i=0;
			while(rs.next()){
				list.add(mapper.mapRow(rs,++i));
			}
			rs.close();
			st.close();
		}catch(Exception e){
			LogUtil.logError(e);
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		return list;
	}

	/**
	* �����ˣ���˧��
	* ��������: 2007-8-20  ����10:57:30
	* ��������: ��ѯSQL������Object[]��List����
	* �����Ĳ����ͷ���ֵ: 
	* @param sql
	* @return		List����Object[]����
	*/
	public static List<?> executeSqlQuery(String sql){

		List<Object> list = new ArrayList<Object>();
		Connection con = null;
		try{
			con = createConnection();
			if(con==null){
				throw new AppException("���ݿ���δ���ӣ�");
			}
			Statement st = con.createStatement();
			LogUtil.logSql(sql);
			ResultSet rs = st.executeQuery(sql);
			int cols = rs.getMetaData().getColumnCount();
			int i=0;
			Object[] row = null;
			while(rs.next()){
				if(cols>1){
					row = new Object[cols];
					for(i=0;i<cols;i++)
						row[i] = rs.getObject(i+1);
					list.add(row);
				}
				else{
					list.add(rs.getObject(1));
				}
			}
			rs.close();
			st.close();
		}catch(Exception e){
			LogUtil.logError(e);
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		return list;
	}

	/**
	* �����ˣ���˧��
	* ��������: 2007-8-20  ����10:57:30
	* ��������: ��ѯSQL�����ؽ�����е�һ�е�һ�е��ַ���
	* �����Ĳ����ͷ���ֵ: 
	* @param sql
	* @return		������е�һ�е�һ�е��ַ���
	*/
	public static String executeSqlString(String sql){

		String result = null;
		Connection con = null;
		try{
			con = createConnection();
			if(con==null){
				throw new AppException("���ݿ���δ���ӣ�");
			}
			Statement st = con.createStatement();
			LogUtil.logSql(sql);
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()){
				result = rs.getString(1);
			}
			rs.close();
			st.close();
		}catch(Exception e){
			LogUtil.logError(e);
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		return result;
	}

	public static List<String> SQL_PREFIX = Arrays.asList(new String[]{"CREATE","DROP","ALTER","UPDATE","DELETE","INSERT","GRANT","COMMIT"});
	public static boolean isSql(String sql){
		Pattern pat = Pattern.compile("^(\\w+)\\s+");
		Matcher mat = pat.matcher(sql);
		if(mat.find()){
			return SQL_PREFIX.contains(mat.group(1).toUpperCase());
		}
		return false;
	}

	/**
	* �����ˣ���˧��
	* ��������: 2007-8-20  ����10:57:30
	* ��������: ��ѯSQL�����ؽ�����е�һ�е�һ�е��ַ���
	* �����Ĳ����ͷ���ֵ: 
	* @param sql
	* @return		������е�һ�е�һ�е�����
	*/
	public static int executeSqlInt(String sql){

		int result = 0;
		try {
			result = Integer.parseInt(executeSqlString(sql));
		} catch (NumberFormatException e) {
			LogUtil.logError(e);
		}
		return result;
	}
	/**
	* �����ˣ���˧��
	* ��������: 2007-8-20  ����10:59:35
	* ��������: ִ��SQL���ĸ���
	* �����Ĳ����ͷ���ֵ: 
	* @param sql
	* @return`		���³ɹ�����
	*/
	public static int executeSqlUpdate(String sql){

		int result = 0;
		Connection con = null;
		try{
			con = createConnection();
			if(con==null){
				throw new AppException("���ݿ���δ���ӣ�");
			}
			Statement st = con.createStatement();
			LogUtil.logSql(sql);
			result = st.executeUpdate(sql);
			st.close();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (SQLException e1) {
				LogUtil.logError(e1);
			}
			LogUtil.logError(e);
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				LogUtil.logError(e);
			}
		}
		return result;
	}
	
}
