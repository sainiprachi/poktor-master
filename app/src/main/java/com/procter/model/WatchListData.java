package com.procter.model;

import com.google.gson.annotations.SerializedName;

public class WatchListData {
	@SerializedName("patient_latitude")
	private String patientLatitude;

	@SerializedName("total_selling_price")
	private String totalSellingPrice;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("pharmacy_id")
	private String pharmacyId;

	@SerializedName("patient_longitude")
	private String patientLongitude;

	@SerializedName("ordered_date")
	private String orderedDate;

	@SerializedName("patient_name")
	private String patientName;

	@SerializedName("medicine_order_id")
	private String medicineOrderId;

	@SerializedName("total_pharmacy_price")
	private String totalPharmacyPrice;

	@SerializedName("id")
	private String id;

	public void setPatientLatitude(String patientLatitude){
		this.patientLatitude = patientLatitude;
	}

	public String getPatientLatitude(){
		return patientLatitude;
	}

	public void setTotalSellingPrice(String totalSellingPrice){
		this.totalSellingPrice = totalSellingPrice;
	}

	public String getTotalSellingPrice(){
		return totalSellingPrice;
	}

	public void setTotalAmount(String totalAmount){
		this.totalAmount = totalAmount;
	}

	public String getTotalAmount(){
		return totalAmount;
	}

	public void setPharmacyId(String pharmacyId){
		this.pharmacyId = pharmacyId;
	}

	public String getPharmacyId(){
		return pharmacyId;
	}

	public void setPatientLongitude(String patientLongitude){
		this.patientLongitude = patientLongitude;
	}

	public String getPatientLongitude(){
		return patientLongitude;
	}

	public void setOrderedDate(String orderedDate){
		this.orderedDate = orderedDate;
	}

	public String getOrderedDate(){
		return orderedDate;
	}

	public void setPatientName(String patientName){
		this.patientName = patientName;
	}

	public String getPatientName(){
		return patientName;
	}

	public void setMedicineOrderId(String medicineOrderId){
		this.medicineOrderId = medicineOrderId;
	}

	public String getMedicineOrderId(){
		return medicineOrderId;
	}

	public void setTotalPharmacyPrice(String totalPharmacyPrice){
		this.totalPharmacyPrice = totalPharmacyPrice;
	}

	public String getTotalPharmacyPrice(){
		return totalPharmacyPrice;
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
			"WatchListData{" +
			"patient_latitude = '" + patientLatitude + '\'' + 
			",total_selling_price = '" + totalSellingPrice + '\'' + 
			",total_amount = '" + totalAmount + '\'' + 
			",pharmacy_id = '" + pharmacyId + '\'' + 
			",patient_longitude = '" + patientLongitude + '\'' + 
			",ordered_date = '" + orderedDate + '\'' + 
			",patient_name = '" + patientName + '\'' + 
			",medicine_order_id = '" + medicineOrderId + '\'' + 
			",total_pharmacy_price = '" + totalPharmacyPrice + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}
