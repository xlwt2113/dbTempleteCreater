package com.ssj.util;

import java.io.File;

import org.apache.log4j.Logger;

public class LogUtil {
	
	static{
		File f = new File("../log/");
		if(!f.exists()){
			f.mkdirs();
		}
	}

	static Logger debug = Logger.getLogger("DEBUG");
	static Logger sql = Logger.getLogger("SQL");
	static Logger info = Logger.getLogger("INFO");
	static Logger warn = Logger.getLogger("WARN");
	static Logger err = Logger.getLogger("ERROR");

	public static void logDebug(Object obj){
		debug.debug(obj);
	}

	public static void logInfo(Object obj){
		info.info(obj);
	}

	public static void logWarn(Object obj){
		warn.warn(obj);
	}

	public static void logSql(Object obj){
		sql.info(obj);
	}

	public static void logError(Throwable e){
		err.error(e.getMessage(),e);
	}
	public static void logError(Object obj,Throwable e){
		err.error(obj, e);
	}
}
