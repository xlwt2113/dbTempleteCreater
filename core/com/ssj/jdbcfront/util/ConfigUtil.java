package com.ssj.jdbcfront.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.InvalidXPathException;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.ssj.jdbcfront.dao.DaoAccess;
import com.ssj.jdbcfront.model.DataBaseView;
import com.ssj.jdbcfront.model.DbObject;
import com.ssj.jdbcfront.model.DbTypeView;
import com.ssj.jdbcfront.model.GenCodeModel;
import com.ssj.util.LogUtil;
import com.ssj.util.StringUtil;

public class ConfigUtil {
	public static String ROOT_PATH = "";
	
	private static Properties propt;;
	private static Properties skins;;
	private static Document dbtype;;
	private static Document dbNames;
	private static List<DbObject> recentObjs;
	private static String CONFIG_INI = "conf/config.ini";
	private static String SKIN_INI = "conf/skins.ini";
	private static String DBNAMES_XML = "conf/dbnames.xml";

	static{
		try {
			File f = new File("conf/");
			if(!f.exists()){
				f.mkdirs();
			}
			System.out.println(new File("conf/config.ini").getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Properties getPropt(){
		if(propt==null){
			try {
				propt = new Properties();
				propt.load(new FileInputStream(ROOT_PATH+CONFIG_INI));
			} catch (Exception e) {
				LogUtil.logError(e);
			}
		}
		return propt;
	}

	private static Properties getSkins(){
		if(skins==null){
			try {
				skins = new Properties();
				skins.load(new FileInputStream(ROOT_PATH+SKIN_INI));
			} catch (Exception e) {
				LogUtil.logError(e);
			}
		}
		return skins;
	}
	
	private static Document getDbType(){
		if(dbtype==null){
			try {
		        SAXReader reader = new SAXReader();
				dbtype = reader.read(ConfigUtil.class.getClassLoader().getResourceAsStream("dbtype.xml"));
			} catch (Exception e) {
				LogUtil.logError(e);
			}
		}
		return dbtype;
	}

	private static Document getDbNames(){
		if(dbNames==null){
			try {
				if(new File(ROOT_PATH+DBNAMES_XML).exists()){
			        SAXReader reader = new SAXReader();
			        InputStreamReader in = new InputStreamReader(new FileInputStream(ROOT_PATH+DBNAMES_XML),"GBK");
			        dbNames = reader.read(in);
				}
				else{
					dbNames = DocumentHelper.createDocument();
					dbNames.addElement("config");
				}
			} catch (Exception e) {
				LogUtil.logError(e);
			}
		}
		return dbNames;
	}

	private static List<DbTypeView> dbTypeList; 
	
	@SuppressWarnings("unchecked")
	public static List<DbTypeView> getDefineDbType(){
		if(dbTypeList == null){
			dbTypeList = new ArrayList<DbTypeView>();
			DbTypeView view = null;
			try {
				Document doc = getDbType();
				Element root = doc.getRootElement();
				for(Iterator<Element> i=root.elementIterator("dbtype");i.hasNext();){
					Element e = (Element) i.next();
					view = new DbTypeView();
					dbTypeList.add(view);
					view.setName(e.attributeValue("name"));
					view.setDbUrl(e.attributeValue("url"));
					view.setDriverClass(e.attributeValue("driver"));
					view.setDriverPath(e.attributeValue("driverPath"));
					view.setDao((DaoAccess) Class.forName(e.attributeValue("dbModel")).newInstance());
					view.setIp(e.attributeValue("ip"));
					view.setPort(e.attributeValue("port"));
					view.setSid(e.attributeValue("sid"));
					view.setUsername(e.attributeValue("username"));
					view.setPassword(e.attributeValue("password"));
					List<String> dataType = new ArrayList<String>();
					Element e1 = e.element("datatype");
					if(e1!=null){
						for(Iterator<Element> it=e1.elementIterator("type");it.hasNext();){
							Element el = (Element) it.next();
							dataType.add(el.getText());
						}
					}
					view.setDataType(dataType);
				}
			} catch (Exception e) {
				
				LogUtil.logError(e);
			}
		}
		return dbTypeList;
	}
	
	public static DbTypeView getDbTypeView(String name){
		List<DbTypeView> list = getDefineDbType();
		if(list==null || name==null)
			return null;
		for(int i=0;i<list.size();i++){
			if(name.equals(list.get(i).getName()))
				return list.get(i);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static List<DataBaseView> getTnsViewList(){
			List<DataBaseView> tnsViewList = new ArrayList<DataBaseView>();
			DataBaseView view = null;
			try {
				Document doc = getDbNames();
				Element root = doc.getRootElement();
		        if(root!=null){
					Element tns = root.element("TNSs");
					for(Iterator<Element> i=tns.elementIterator("TNS");i.hasNext();){
						Element e = (Element) i.next();
						view = new DataBaseView(getDbTypeView(e.attributeValue("dbtype")));
						tnsViewList.add(view);
						view.setName(e.attributeValue("name"));
						view.setIp(e.attributeValue("ip"));
						view.setPort(e.attributeValue("port"));
						view.setDriver(e.attributeValue("driver"));
						view.setSid(e.attributeValue("sid"));
						view.setJdbcUrl(e.attributeValue("jdbcUrl"));
						view.setUserName(e.attributeValue("username"));
						view.setPassword(e.attributeValue("password"));
					}
		        }
			}
			catch(Exception e){
				LogUtil.logError(e);
			}
		return tnsViewList;
	}
	
	public static DataBaseView getTnsView(String name){
		for(DataBaseView dbv:getTnsViewList()){
			if(dbv.getName().equals(name)){
				return dbv;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String,String> getTypeTransMap(){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Document doc = getDbNames();
			Element root = doc.getRootElement();
	        if(root!=null){
				Element tns = root.element("trans");
				for(Iterator<Element> i=tns.elementIterator("tran");i.hasNext();){
					Element e = (Element) i.next();
					map.put(e.attributeValue("type"),e.getText());
				}
	        }
		}
		catch(Exception e){
			LogUtil.logError(e);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static void saveTnsView(DataBaseView view,String tnsName){
		try {
			Document doc = getDbNames();
			Element root = doc.getRootElement();
			Element tns = root.element("TNSs");
			if(tns==null)
				tns = root.addElement("TNSs");
			Element node = null;
			for(Iterator<Element> i=tns.elementIterator("TNS");i.hasNext();){
				Element e = (Element) i.next();
				if(e.attributeValue("name").equals(tnsName)){
					node = e;
					break;
				}
			}
			if(node==null){
				node = tns.addElement("TNS");
			}
			node.addAttribute("name", view.getName());
			node.addAttribute("ip", view.getIp());
			node.addAttribute("port", view.getPort());
			node.addAttribute("driver", view.getDriver());
			node.addAttribute("sid", view.getSid());
			node.addAttribute("jdbcUrl", view.getJdbcUrl());
			node.addAttribute("dbtype", view.getDbType().getName());
			node.addAttribute("username", view.getUserName());
			node.addAttribute("password", view.getPassword());
			
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter writer = new XMLWriter(new FileWriter(ROOT_PATH+DBNAMES_XML), format);
	        writer.write(doc);
	        writer.close();
		}
		catch(Exception e){
			LogUtil.logError(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void removeTnsView(DataBaseView view) {
		try {
			Document doc = getDbNames();
			Element root = doc.getRootElement();
			Element tns = root.element("TNSs");
			if(tns==null)
				tns = root.addElement("TNSs");
			for(Iterator<Element> i=tns.elementIterator("TNS");i.hasNext();){
				Element e = (Element) i.next();
				if(e.attributeValue("name").equals(view.getName())){
					tns.remove(e);
				}
			}
			
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter writer = new XMLWriter(new FileWriter(ROOT_PATH+DBNAMES_XML), format);
	        writer.write(doc);
	        writer.close();
		}
		catch(Exception e){
			LogUtil.logError(e);
		}
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-25  下午10:32:00
	* 功能描述: 设置字符串的配置项
	* 方法的参数和返回值: 
	* @param name
	* @param value
	*/
	public static void setString(String name,String value){
		getPropt().setProperty(name, value);
		try {
			getPropt().store(new FileOutputStream(ROOT_PATH+CONFIG_INI), "");
		} catch (IOException e) {
			LogUtil.logError(e);
		}
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-25  下午10:39:00
	* 功能描述: 设置数字型的配置项
	* 方法的参数和返回值: 
	* @param name
	* @param value
	*/
	public static void setInt(String name,int value){
		setString(name,""+value);
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-25  下午10:40:38
	* 功能描述: 获取字符串型的配置项值
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public static String getString(String name){
		return getPropt().getProperty(name);
	}

	public static String getString(String name,String defaul){
		String res = getPropt().getProperty(name);
		if(res == null){
			res = defaul;
		}
		return res;
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-11-25  下午10:41:39
	* 功能描述: 获取整形的配置项值
	* 方法的参数和返回值: 
	* @param name
	* @return
	*/
	public static int getInt(String name){
		return getInt(name,-1);
	}

	public static int getInt(String name,int defaul){
		int res = 0;
		try {
			res = Integer.parseInt(getString(name));
		} catch (NumberFormatException e) {
			return defaul;
		}
		return res;
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-5  下午09:46:19
	* 功能描述: 获取最近访问对象
	* 方法的参数和返回值: 
	* @return
	*/
	public static List<DbObject> getRecentObjects(){
		List<DbObject> list = recentObjs;
		if(list==null){
			list = new ArrayList<DbObject>();
			String lastOp = ConfigUtil.getString(ConfigUtil.getString("LAST_LOGIN")+"_lastObjects");
			if(StringUtil.isBlank(lastOp)){
				return list;
			}
			String[] tmp = lastOp.split(";");
			for(String name:tmp){
				String[] tmpAry = name.split(",");
				if(tmpAry.length<2){
					continue;
				}
				DbObject dbo = new DbObject(tmpAry[0]);
				dbo.setType(tmpAry[1]);
				list.add(dbo);
			}
			recentObjs = list;
		}
		return list;
	}
	
	/**
	 * 增加最近访问对象
	 */
	public static void addRecentObject(DbObject dbo){
		List<DbObject> list = getRecentObjects();
		if(list.contains(dbo)){
			list.remove(dbo);
		}
		list.add(0, dbo);
		int num = getInt("RECENT_OBJECT_NUM");
		if(num<=0){//默认保存15个最近访问对象
			num = 15;
		}
		while(list.size()>num){
			list.remove(list.size()-1);
		}
		StringBuffer buff = new StringBuffer();
		for(DbObject obj:list){
			buff.append(obj.getName()).append(",").append(obj.getType()).append(";");
		}
		ConfigUtil.setString(ConfigUtil.getString("LAST_LOGIN")+"_lastObjects",buff.toString());
//		Frame.getInstance().refreshRecentObject();
	}

	/**
	 * 删除最近访问对象
	 */
	public static void removeRecentObject(DbObject dbo){
		List<DbObject> list = getRecentObjects();
		if(list.contains(dbo)){
			list.remove(dbo);
		}
		StringBuffer buff = new StringBuffer();
		for(DbObject obj:list){
			buff.append(obj.getName()).append(",").append(obj.getType()).append(";");
		}
		ConfigUtil.setString(ConfigUtil.getString("LAST_LOGIN")+"_lastObjects",buff.toString());
//		Frame.getInstance().refreshRecentObject();
	}
	
	/**
	* 开发人：宋帅杰
	* 开发日期: 2010-12-5  下午09:46:29
	* 功能描述: 获取皮肤列表
	* 方法的参数和返回值: 
	* @return
	*/
	public static Map<String,String> getSkinMap(){
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("Windows经典皮肤", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		for(Object key:getSkins().keySet()){
			map.put(key.toString(), getSkins().get(key).toString());
		}
		return map;
	}
	
	public static void main(String[] args){
		GenCodeModel obj = loadTblModel("H_CAR_INFO");
	}
	
	public static GenCodeModel loadTblModel(String name){
		File f = new File("data/"+name+".xml");
		if(f.exists()){
	        SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(f);
				Element root = doc.getRootElement();
				return (GenCodeModel) loadObject(root.element("bean"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static Object loadObject(Element root) {
		if(root==null){
			return null;
		}
		if("bean".equals(root.getQName().getName())){
			String cls = root.attributeValue("type");
			Object obj=null;
			try {
				obj = Class.forName(cls).newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(obj!=null){
				List<?> list = root.elements();
				for(Object tmp:list){
					Element ele = (Element) tmp;
					if("property".equals(ele.getQName().getName())){
						List<?> valList = ele.elements();
						if(valList!=null&&valList.size()>0){
							try {
								PropertyUtils.setProperty(obj, ele.attributeValue("name"), loadObject((Element) valList.get(0)));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			return obj;
		}
		else if("list".equals(root.getQName().getName())){
			ArrayList<Object> list = new ArrayList<Object>();
			List<?> eleList = root.elements();
			for(Object obj:eleList){
				list.add(loadObject((Element) obj));
			}
			return list;
		}
		else if("value".equals(root.getQName().getName())){
			if(root.getText()==null||root.getText().length()==0){
				return null;
			}
			String type = root.attributeValue("type");
			if("java.lang.String".equals(type)){
				return root.getText();
			}
			else if("java.util.Date".equals(type)){
				try {
					return sdf.parse(root.getText());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			else if("int".equals(type)||"java.lang.Integer".equals(type)){
				return Integer.parseInt(root.getText());
			}
			else if("long".equals(type)||"java.lang.Long".equals(type)){
				return Long.parseLong(root.getText());
			}
			else if("float".equals(type)||"java.lang.Float".equals(type)){
				return Float.parseFloat(root.getText());
			}
			else if("double".equals(type)||"java.lang.Double".equals(type)){
				return Double.parseDouble(root.getText());
			}
			else if("boolean".equals(type)||"java.lang.Boolean".equals(type)){
				return Boolean.parseBoolean(root.getText());
			}
		}
		return null;
	}

	public static void saveTblModel(GenCodeModel model){
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("root");
		transXmlElement(model,root);
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
        	File dir = new File("data/");
        	if(!dir.exists()){
        		dir.mkdirs();
        	}
        	System.out.println("write:"+new File("data/"+model.getName()+".xml").getCanonicalPath());
			XMLWriter writer = new XMLWriter(new FileOutputStream("data/"+model.getName()+".xml"), format);
			writer.write(doc);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static Element transXmlElement(Object obj,Element par){
		Element ele = null;
		System.out.println(obj.getClass());
		if(obj instanceof String ||obj instanceof Integer ||obj instanceof Long ||obj instanceof Float ||obj instanceof Double ||
				obj instanceof Boolean ||obj.getClass() == int.class ||obj.getClass() == long.class ||obj.getClass() == float.class ||obj.getClass() == double.class){
			ele = par.addElement("value");
			ele.addAttribute("type", obj.getClass().getName());
			ele.setText(obj.toString());
		}
		else if(obj instanceof Date){
			ele = par.addElement("value");
			ele.addAttribute("type", obj.getClass().getName());
			ele.setText(sdf.format(obj));
		}
		else if(obj instanceof List){
			ele = par.addElement("list");
			List<?> list = (List<?>) obj;
			for(Object tmp:list){
				transXmlElement(tmp,ele);
			}
		}
		else{
			ele = par.addElement("bean");
			ele.addAttribute("type", obj.getClass().getName());
			try {
				String[] propts = (String[]) PropertyUtils.getProperty(obj, "savePropts");
				for(String f:propts){
					Element p = ele.addElement("property");
					p.addAttribute("name", f);
					Object val = null;
					try {
						val = PropertyUtils.getProperty(obj, f);
					} catch (Exception e) {
						e.printStackTrace();
					}
					val = val==null?"":val;
					transXmlElement(val,p);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return ele;
	}
}
