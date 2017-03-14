package com.ssj.jdbcfront.exception;

public class AppException extends Exception {
	private static final long serialVersionUID = 6457984229927151115L;
	public AppException(){
		super();
	}
	public AppException(String msg){
		super(msg);
	}
	public AppException(Throwable e){
		super(e);
	}
	public AppException(String msg,Throwable e){
		super(msg,e);
	}

}
