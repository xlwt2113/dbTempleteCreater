package com.ssj.jdbcfront.model;

import com.ssj.jdbcfront.exception.AppException;
import com.ssj.util.StringUtil;

public class DataBaseView {

	/**
	 * 对应的数据库类型
	 */
	private DbTypeView dbType;
	/**
	 * 数据库别名
	 */
	private String name;
	/**
	 * 数据库的IP
	 */
	private String ip;
	/**
	 * 数据库的端口
	 */
	private String port;
	/**
	 * 数据库实例名
	 */
	private String sid;
	
	/**
	 * 如果此值不为空，则直接使用此值，不再使用IP,PORT,SID拼凑
	 */
	private String jdbcUrl;
	/**
	 * 数据库驱动在磁盘上的位置
	 */
	private String driver;
	/**
	 * 数据库用户名
	 */
	private String userName;
	/**
	 * 数据库密码
	 */
	private String password;
	
	public DataBaseView(DbTypeView dbType) throws AppException {
		super();
		if(dbType==null){
			throw new AppException("未指定数据库类型!");
		}
		this.dbType = dbType;
	}

	public String toString(){
		return name;
	}
	
	public String getJdbcUrl(){
		if(StringUtil.isNotBlank(jdbcUrl)){
			return jdbcUrl;
		}
		return dbType.getDbUrl().replace("${ip}", ip).replace("${port}", port)
		.replace("${sid}", sid);
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public DbTypeView getDbType() {
		return dbType;
	}

	public void setDbType(DbTypeView dbType) {
		this.dbType = dbType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null || !(obj instanceof DataBaseView)){
			return false;
		}
		DataBaseView that = (DataBaseView) obj;
		return this.name.equals(that.name);
	}

}
