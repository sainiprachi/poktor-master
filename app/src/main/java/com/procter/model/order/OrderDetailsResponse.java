package com.procter.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderDetailsResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("data")
	private OrderDetails data;

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

	public void setData(OrderDetails data){
		this.data = data;
	}

	public OrderDetails getData(){
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
			"OrderDetailsResponse{" + 
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}