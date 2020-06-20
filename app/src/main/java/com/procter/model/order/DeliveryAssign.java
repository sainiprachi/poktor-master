package com.procter.model.order;

import com.google.gson.annotations.SerializedName;

public class DeliveryAssign {

	@SerializedName("delivered_orders")
	private String deliveredOrders;

	@SerializedName("image")
	private String image;

	@SerializedName("phone")
	private String phone;

	@SerializedName("vehicle_number")
	private String vehicleNumber;

	@SerializedName("name")
	private String name;

	@SerializedName("pending_orders")
	private String pendingOrders;

	@SerializedName("id")
	private String id;

	@SerializedName("public_id")
	private String publicId;

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	private boolean isSelect=false;

	public void setDeliveredOrders(String deliveredOrders){
		this.deliveredOrders = deliveredOrders;
	}

	public String getDeliveredOrders(){
		return deliveredOrders;
	}

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setVehicleNumber(String vehicleNumber){
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleNumber(){
		return vehicleNumber;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPendingOrders(String pendingOrders){
		this.pendingOrders = pendingOrders;
	}

	public String getPendingOrders(){
		return pendingOrders;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setPublicId(String publicId){
		this.publicId = publicId;
	}

	public String getPublicId(){
		return publicId;
	}

	@Override
 	public String toString(){
		return 
			"DeliveryAssign{" +
			"delivered_orders = '" + deliveredOrders + '\'' + 
			",image = '" + image + '\'' + 
			",phone = '" + phone + '\'' + 
			",vehicle_number = '" + vehicleNumber + '\'' + 
			",name = '" + name + '\'' + 
			",pending_orders = '" + pendingOrders + '\'' + 
			",id = '" + id + '\'' + 
			",public_id = '" + publicId + '\'' + 
			"}";
		}
}
