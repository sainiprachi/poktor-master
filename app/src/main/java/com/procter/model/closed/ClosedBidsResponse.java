package com.procter.model.closed;

import com.google.gson.annotations.SerializedName;

public class ClosedBidsResponse{

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
			"ClosedBidsResponse{" + 
			"response = '" + response + '\'' + 
			",key = '" + key + '\'' + 
			"}";
		}
}