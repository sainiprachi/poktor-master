package com.procter.model.order;

import com.google.gson.annotations.SerializedName;

public class OrderItemsItem{

	@SerializedName("medicine_id")
	private String medicineId;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("price")
	private String price;

	@SerializedName("name")
	private String name;

	@SerializedName("medicine_order_id")
	private String medicineOrderId;

	@SerializedName("id")
	private String id;

	public void setMedicineId(String medicineId){
		this.medicineId = medicineId;
	}

	public String getMedicineId(){
		return medicineId;
	}

	public void setQuantity(String quantity){
		this.quantity = quantity;
	}

	public String getQuantity(){
		return quantity;
	}

	public void setPrice(String price){
		this.price = price;
	}

	public String getPrice(){
		return price;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setMedicineOrderId(String medicineOrderId){
		this.medicineOrderId = medicineOrderId;
	}

	public String getMedicineOrderId(){
		return medicineOrderId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"OrderItemsItem{" + 
			"medicine_id = '" + medicineId + '\'' + 
			",quantity = '" + quantity + '\'' + 
			",price = '" + price + '\'' + 
			",name = '" + name + '\'' + 
			",medicine_order_id = '" + medicineOrderId + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}