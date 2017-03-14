package com.ssj.jdbcfront.model;

import java.util.List;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.util.ConfigUtil;

/**
* �����ˣ���˧��
* �������ڣ�2010-11-25
* ����ʱ�䣺����09:39:28
* �������������ݿ�����ģ��
* ==============================================
* �޸���ʷ
* �޸���		�޸�ʱ��		�޸�ԭ��
*
* ==============================================
*/
public class DbTypeView {

	/**
	 * ���ݿ���������
	 */
	private String name;
	/**
	 * ���ݿ���������
	 */
	private String driverClass;
	/**
	 * ���ݿ������ļ���
	 */
	private String driverPath;
	/**
	 * ���ݿ�JDBC����URL
	 */
	private String dbUrl;
	/**
	 * ���ݿ�Ĭ��IP
	 */
	private String ip;
	/**
	 * ���ݿ�Ĭ�϶˿�
	 */
	private String port;
	/**
	 * ���ݿ�Ĭ������
	 */
	private String sid;
	/**
	 * ���ݿ�Ĭ���û���
	 */
	private String username;
	/**
	 * ���ݿ�Ĭ������
	 */
	private String password;
	/**
	 * ���ݿ���ʶ���
	 */
	private DaoAccess dao;
	
	/**
	 * ֧�ֵ���������
	 */
	private List<String> dataType;
	
	public static DbTypeView getInstance(String name) {
		return ConfigUtil.getDbTypeView(name);
	}

	public String toString(){
		return name;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DaoAccess getDao() {
		return dao;
	}

	public void setDao(DaoAccess dao) {
		this.dao = dao;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDriverPath() {
		return driverPath;
	}

	public void setDriverPath(String driverPath) {
		this.driverPath = driverPath;
	}

	public List<String> getDataType() {
		return dataType;
	}

	public void setDataType(List<String> dataType) {
		this.dataType = dataType;
	}

}
