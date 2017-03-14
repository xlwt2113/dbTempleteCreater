package com.ssj.jdbcfront.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import com.ssj.jdbcfront.exception.SdbFileFormatException;

public class SdbFileReader {
	
	public static long FILE_STAMP = 8888888888888888L;
	
	private InputStream in;
	private String version,driver,jdbcUrl,user;
	private Date date;

	public SdbFileReader(String inFilePath) throws SdbFileFormatException, IOException{
		this(new FileInputStream(inFilePath));
	}

	public SdbFileReader(File in) throws SdbFileFormatException, IOException{
		this(new FileInputStream(in));
	}

	public SdbFileReader(FileInputStream in) throws SdbFileFormatException, IOException{
		this.in = new BufferedInputStream(in);
		if(FILE_STAMP!=IOUtil.readLong(in)){
			in.close();
			throw new SdbFileFormatException("文件格式不正确，可能文件被破坏!");
		}
		version = IOUtil.readString(this.in);
		driver = IOUtil.readString(this.in);
		jdbcUrl = IOUtil.readString(this.in);
		user = IOUtil.readString(this.in);
		date = IOUtil.readDate(this.in);
	}
	
	public String getVersion() {
		return version;
	}

	public String getDriver() {
		return driver;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public String getUser() {
		return user;
	}

	public Date getDate() {
		return date;
	}

	public String getSql() throws IOException{
		return IOUtil.readString(in);
	}

	/**
	 * 列数据类型，目前支持字符串0、日期1、字节流2两种类型
	 * @param row
	 */
	public byte[] getRowDataType(int len) throws IOException{
		byte[] buff = new byte[len];
		in.read(buff,0,len);
		return buff;
	}
	
	public static byte getColType(String type){
		return SdbFileWriter.getColType(type);
	}

	public Object[] getRowData(byte[] types) throws IOException{
		if(in.read()==0){
			return null;
		}
		Object[] row = new Object[types.length];
		for(int i=0;i<types.length;i++){
			if(types[i]==1){
				Date d = IOUtil.readDate(in);
				if(d!=null){
					row[i] = new Timestamp(d.getTime());
				}
			}
			else if(types[i]==2){
				row[i] = IOUtil.readInputStream(in);
			}
			else{
				row[i] = IOUtil.readString(in);
			}
		}
		return row;
	}
	
	public void close() throws IOException{
		in.close();
	}
}
