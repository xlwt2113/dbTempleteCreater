package com.ssj.jdbcfront.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ssj.jdbcfront.template.TransUtil;
import com.ssj.util.DateUtil;

public class GenCodeModel extends DbTableType {
	
	/** ����Ϊ�ļ�ʱҪ����������� */
	private String[] savePropts = "basePackage,targetPath,url,urlPre,basePackage,develpor,comment,querySql,keyList,columnList".split(",");
	
	
	private String slName;  //Сд��ĸ����
	private String slNames; //Сд��ĸ����
	private String objName; //����ĸ��д����
	private String objNames; //����ĸ��д����
	private String tableDsName;//��������ģʽ
	private String tableName;//���ݱ�����
	


	public String getSlName() {
		slName = this.getBasePackage();
		return slName;
	}

	public String getSlNames() {
		slNames = this.getUrl();
		return slNames;
	}

	public String getObjName() {
		objName = this.getBasePackage().substring(0, 1).toUpperCase()+this.getBasePackage().substring(1);
		return objName;
	}

	public String getObjNames() {
		objNames = this.getUrl().substring(0, 1).toUpperCase()+this.getUrl().substring(1);
		return objNames;
	}

	public String getTableDsName() {
		tableDsName = this.getComment();
		return tableDsName;
	}

	public String getTableName() {
		tableName = this.getDevelpor();
		return tableName;
	}

	/*** ָ����URLǰ׺*/
	private String urlPre;
	/*** ָ����URL*/
	private String url;

	/*** ָ���Ļ�����·��*/
	private String templatePath;
	
	/*** ָ���Ļ�����·��*/
	private String basePackage;
	/** Ŀ��Ŀ¼ */
	private String targetPath;
	
	/** ���������� */
	private String develpor;
	
	private String genDate = DateUtil.format(new Date(),"yyyy-MM-dd HH:mm:ss");
	
	private String dbUser;
	
	/** ��ѯ�õ�SQL */
	private String querySql;
	
	private GenCodeModel subTable;

	private List<GenCodeColumn> keyList;

	private List<GenCodeColumn> columnList;
	
	public GenCodeColumn getColumn(String name){
		if(name==null){
			return null;
		}
		List<GenCodeColumn> list = getAllColumn();
		for(GenCodeColumn col:list){
			if(col.getName().toUpperCase().equals(name.toUpperCase())){
				return col;
			}
		}
		return null;
	}
	
	public String getClassPre(){
		return url.substring(0,1).toUpperCase()+url.substring(1);
	}

	public String getBasePackagePath() {
		return basePackage.replace(".", "/");
	}
	
	public List<GenCodeColumn> getAllColumn(){
		List<GenCodeColumn> list = new ArrayList<GenCodeColumn>();
		if(keyList!=null){
			list.addAll(keyList);
		}
		list.addAll(columnList);
		return list;
	}
	
	public String getOrderField(){
		List<GenCodeColumn> list = getAllColumn();
		if(list==null||list.size()==0){
			return "";
		}
		for(GenCodeColumn col:list){
			if("java.util.Date".equals(col.getFieldType())){
				return col.getName()+","+list.get(0).getName();
			}
		}
		return list.get(0).getName();
	}

	public String getQuerySql() {
		return querySql;
	}

	public void setQuerySql(String charSet) {
		this.querySql = charSet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String getDevelpor() {
		return develpor;
	}

	public void setDevelpor(String develpor) {
		this.develpor = develpor;
	}

	public List<GenCodeColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<GenCodeColumn> columnList) {
		this.columnList = columnList;
	}

	public List<GenCodeColumn> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<GenCodeColumn> keyList) {
		this.keyList = keyList;
	}

	public String getGenDate() {
		return genDate;
	}

	public void setGenDate(String genDate) {
		this.genDate = genDate;
	}
	
	public GenCodeModel getSubTable() {
		return subTable;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getUrlPre() {
		return urlPre;
	}

	public void setUrlPre(String urlPre) {
		this.urlPre = urlPre;
	}

	public String[] getSavePropts() {
		return savePropts;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public void setSubTable(DbTableType tbl) {
		this.subTable = new GenCodeModel();
		subTable.setName(tbl.getName());
		subTable.setUrl(TransUtil.getPropertyName(tbl.getName()));
		subTable.setComment(tbl.getComment());
		subTable.setPkName(tbl.getPkName());
		List<GenCodeColumn> keys = new ArrayList<GenCodeColumn>();
		List<GenCodeColumn> cols = new ArrayList<GenCodeColumn>();
		subTable.setKeyList(keys);
		subTable.setColumnList(cols);
		for(DbColumnType col:tbl.getColumns()){
			if(col.isIsPri()){
				keys.add(new GenCodeColumn(col));
			}
			else{
				cols.add(new GenCodeColumn(col));
			}
		}
	}

}
