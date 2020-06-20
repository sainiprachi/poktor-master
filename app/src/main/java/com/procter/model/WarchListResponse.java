package com.procter.model;

import java.util.List;

public class WarchListResponse{
	private int code;
	private List<WatchListData> data;
	private String message;
	private boolean status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(List<WatchListData> data){
		this.data = data;
	}

	public List<WatchListData> getData(){
		return data;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"WarchListResponse{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}