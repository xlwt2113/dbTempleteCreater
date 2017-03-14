package com.ssj.jdbcfront.model;

import java.util.List;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.util.ConfigUtil;

/**
* 创建人：宋帅杰
* 创建日期：2010-11-25
* 创建时间：下午09:39:28
* 功能描述：数据库类型模型
* ==============================================
* 修改历史
* 修改人		修改时间		修改原因
*
* ==============================================
*/
public class DbTypeView {

	/**
	 * 数据库类型名称
	 */
	private String name;
	/**
	 * 数据库驱动类名
	 */
	private String driverClass;
	/**
	 * 数据库驱动文件名
	 */
	private String driverPath;
	/**
	 * 数据库JDBC连接URL
	 */
	private String dbUrl;
	/**
	 * 数据库默认IP
	 */
	private String ip;
	/**
	 * 数据库默认端口
	 */
	private String port;
	/**
	 * 数据库默认名称
	 */
	private String sid;
	/**
	 * 数据库默认用户名
	 */
	private String username;
	/**
	 * 数据库默认密码
	 */
	private String password;
	/**
	 * 数据库访问对象
	 */
	private DaoAccess dao;
	
	/**
	 * 支持的数据类型
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
