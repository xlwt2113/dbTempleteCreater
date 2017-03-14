package com.ssj.jdbcfront.template;

import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.ssj.util.LogUtil;

public class Engine {
	
	static String charSet;

	public static VelocityEngine getVelocityEngine(String rootPath,String charSet){
		Engine.charSet = charSet;
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, rootPath);
//		ve.setProperty(VelocityEngine.ENCODING_DEFAULT, charSet);
//		ve.setProperty("input.encoding", charSet);
//		ve.setProperty("output.encoding", charSet);
		ve.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
		ve.setProperty("file.resource.loader.class", "com.ssj.jdbcfront.template.ResourceLoader");
		try {
			ve.init();
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return ve;
	}
	
	public static void main(String[] args) throws Exception{
		VelocityEngine ve = getVelocityEngine("C:/","UTF-8");
		VelocityContext cont = new VelocityContext();
		cont.put("name", "动物");
		Template t = ve.getTemplate("2.txt");
		FileWriter out = new FileWriter("C:/3.txt");
		t.merge(cont, out);
		out.flush();
		out.close();
		Properties p = System.getProperties();
		for(Object key:p.keySet()){
			System.out.println(key+"\t=\t"+p.get(key));
		}
		System.out.println(merge(ve,cont,"${name}人人","UTF-8"));
	}
	
	public static String merge(VelocityEngine ve,Context cont,String src,String charSet){
		String res = null;
		Engine.charSet = charSet;
		try {
			String pre = "STRING:";
			Template t = ve.getTemplate(pre+src,charSet);
			t.setEncoding("UTF-8");
			StringWriter sw = new StringWriter();
			t.merge(cont, sw);
			sw.flush();
			res = sw.getBuffer().substring(pre.length());
		} catch (ResourceNotFoundException e) {
			LogUtil.logError(e);
		} catch (ParseErrorException e) {
			LogUtil.logError(e);
		} catch (MethodInvocationException e) {
			LogUtil.logError(e);
		} catch (Exception e) {
			LogUtil.logError(e);
		}
		return res;
	}
}
