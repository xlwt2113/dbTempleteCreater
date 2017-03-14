package com.ssj.jdbcfront.exception;

public class SdbFileFormatException extends Exception {

	private static final long serialVersionUID = 1L;
	public SdbFileFormatException(){
		super();
	}
	public SdbFileFormatException(String msg){
		super(msg);
	}
	public SdbFileFormatException(Throwable e){
		super(e);
	}
	public SdbFileFormatException(String msg,Throwable e){
		super(msg,e);
	}
}
