package com.ssj.jdbcfront.model;

import com.ssj.jdbcfront.exception.AppException;
import com.ssj.util.StringUtil;

public class DataBaseView {

	/**
	 * ��Ӧ�����ݿ�����
	 */
	private DbTypeView dbType;
	/**
	 * ���ݿ����
	 */
	private String name;
	/**
	 * ���ݿ��IP
	 */
	private String ip;
	/**
	 * ���ݿ�Ķ˿�
	 */
	private String port;
	/**
	 * ���ݿ�ʵ����
	 */
	private String sid;
	
	/**
	 * �����ֵ��Ϊ�գ���ֱ��ʹ�ô�ֵ������ʹ��IP,PORT,SIDƴ��
	 */
	private String jdbcUrl;
	/**
	 * ���ݿ������ڴ����ϵ�λ��
	 */
	private String driver;
	/**
	 * ���ݿ��û���
	 */
	private String userName;
	/**
	 * ���ݿ�����
	 */
	private String password;
	
	public DataBaseView(DbTypeView dbType) throws AppException {
		super();
		if(dbType==null){
			throw new AppException("δָ�����ݿ�����!");
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
