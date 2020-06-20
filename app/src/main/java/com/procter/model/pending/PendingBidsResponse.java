package com.procter.model.pending;

import com.google.gson.annotations.SerializedName;

public class PendingBidsResponse{

	@SerializedName("response")
	private Response response;

	@SerializedName("key")
	private String key;

	public void setResponse(Response response){
		this.response = response;
	}

	public Response getResponse(){
		return response;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getKey(){
		return key;
	}

	@Override
 	public String toString(){
		return 
			"PendingBidsResponse{" + 
			"response = '" + response + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}