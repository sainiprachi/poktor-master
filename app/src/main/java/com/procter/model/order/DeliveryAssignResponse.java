package com.procter.model.order;

import java.util.List;

public class DeliveryAssignResponse {
	private int code;
	private List<DeliveryAssign> data;
	private String message;
	private boolean status;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setData(List<DeliveryAssign> data){
		this.data = data;
	}

	public List<DeliveryAssign> getData(){
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
			"code = '" + code + '\'' + 
			",data = '" + data + '\'' + 
			",message = '" + message + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}