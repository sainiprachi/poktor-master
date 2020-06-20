package com.procter.model.pending;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response{

	@SerializedName("data")
	private List<PendingItem> data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public void setData(List<PendingItem> data){
		this.data = data;
	}

	public List<PendingItem> getData(){
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