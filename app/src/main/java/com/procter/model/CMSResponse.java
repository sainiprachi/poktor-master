package com.procter.model;

import com.google.gson.annotations.SerializedName;

public class CMSResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private CMSData data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(CMSData data){
		this.data = data;
	}

	public CMSData getData(){
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
			"CMSResponse{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}