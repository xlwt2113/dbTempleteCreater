package com.ssj.jdbcfront.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import com.ssj.jdbcfront.util.FixedInputStream;
import com.ssj.util.StringUtil;

public class SdbFileWriter {
	
	public static long FILE_STAMP = 8888888888888888L;
	
	private OutputStream out;
	private boolean hasHeader;

	public SdbFileWriter(String outFilePath) throws FileNotFoundException{
		this(new FileOutputStream(outFilePath));
	}

	public SdbFileWriter(File out) throws FileNotFoundException{
		this(new FileOutputStream(out));
	}

	public SdbFileWriter(FileOutputStream out){
		this.out = new BufferedOutputStream(out);
	}
	
	public void writeHead(String version,String driver,String jdbcUrl,String user) throws IOException{
		if(hasHeader){
			throw new IOException("文件头不能重复写入!");
		}
		IOUtil.writeLong(out, FILE_STAMP);
		IOUtil.writeString(out, version);
		IOUtil.writeString(out, driver);
		IOUtil.writeString(out, jdbcUrl);
		IOUtil.writeString(out, user);
		IOUtil.writeDate(out, new Date());
		hasHeader = true;
	}
	
	public void writeSql(String sql) throws IOException{
		if(!hasHeader){
			throw new IOException("请先写入文件头!");
		}
			if(StringUtil.isBlank(sql)){
				return;
			}
			IOUtil.writeString(out, sql);
	}

	public void writeSqls(String sql,String split) throws IOException{
		if(sql!=null){
			String[] ss = sql.split(split);
			for(String s:ss){
				writeSql(s);
			}
		}
	}

	/**
	 * 列数据类型，目前支持字符串0、日期1、字节流2两种类型
	 * @param row
	 */
	public void writeRowDataType(byte[] row) throws IOException{
		if(!hasHeader){
			throw new IOException("请先写入文件头!");
		}
		out.write(row);
	}
	
	public static byte getColType(String type){
		if(type==null){
			return 0;
		}
		type = type.toUpperCase();
		if(type.indexOf("DATE")>=0 || type.indexOf("TIME")>=0){
			return 1;
		}
		if(type.equals("BLOB")){
			return 2;
		}
		return 0;
	}

	public void writeRowData(Object[] row) throws IOException{
		if(!hasHeader){
			throw new IOException("请先写入文件头!");
		}
			out.write(new byte[]{1});
			for(Object col:row){
				if(col==null || col instanceof String){
					IOUtil.writeString(out, (String)col);
				}
				else if(col instanceof Date){
					Date d = (Date)col;
					IOUtil.writeDate(out, d);
				}
				else if(col instanceof FixedInputStream){
					FixedInputStream fi = (FixedInputStream)col;
					IOUtil.writeInputStream(out, fi.getIn(), fi.getLen());
				}
			}
	}
	
	public void writeTableDataEnd() throws IOException{
		if(!hasHeader){
			throw new IOException("请先写入文件头!");
		}
			out.write(new byte[]{0});
	}
	
	public void close() throws IOException{
		out.close();
	}
}
