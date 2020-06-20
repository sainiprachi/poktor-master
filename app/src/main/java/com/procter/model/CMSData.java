package com.procter.model;

import com.google.gson.annotations.SerializedName;

public class CMSData{

	@SerializedName("description")
	private String description;

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"description = '" + description + '\'' + 
			"}";
		}
}