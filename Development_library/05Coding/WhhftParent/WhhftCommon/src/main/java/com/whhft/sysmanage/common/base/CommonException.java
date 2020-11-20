package com.whhft.sysmanage.common.base;

public class CommonException extends Exception{
	
	private String errorMsg = "";
	
	public CommonException(){
		
	}
	
	public CommonException(String errorMsg){
		this.errorMsg = errorMsg;
	}
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return errorMsg;
	}

}
