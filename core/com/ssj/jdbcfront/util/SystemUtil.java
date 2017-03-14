package com.ssj.jdbcfront.util;

import java.io.File;
import java.io.IOException;

import com.ssj.util.LogUtil;

public class SystemUtil {

	public static void openFolder(String dir){
		if(dir==null){
			return;
		}
		String command = null;
		if(System.getProperty("os.name").toUpperCase().contains("WINDOWS")){
			command = "explorer "+dir.replace("/", "\\");
		}
		if(command==null){
//			Frame.showMsg("�ݲ�֧���ڴ����Ͳ���ϵͳ����Դ��������");
			command = "nautilus "+dir;
		}
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			LogUtil.logError(e);
		}
	}
	
	public static void openFile(String path){
		if(path==null||!new File(path).exists()){
			return;
		}
		String command = null;
		if(System.getProperty("os.name").toUpperCase().contains("WINDOWS")){
			command = "cmd /c "+path.replace("/", "\\");
		}
		if(command==null){
//			Frame.showMsg("�ݲ�֧���ڴ����Ͳ���ϵͳ����Դ��������");
		}
		try {
			Runtime.getRuntime().exec(command);
		} catch (IOException e) {
			LogUtil.logError(e);
		}
	}
}
