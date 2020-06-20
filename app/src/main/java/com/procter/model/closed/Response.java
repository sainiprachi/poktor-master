package com.procter.model.closed;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{

	@SerializedName("data")
	private List<ClosedItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(List<ClosedItem> data){
		this.data = data;
	}

	public List<ClosedItem> getData(){
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
			"DeliveryAssignResponse{" +
			"data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}