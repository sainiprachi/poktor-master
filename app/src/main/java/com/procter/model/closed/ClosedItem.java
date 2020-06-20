package com.procter.model.closed;

import com.google.gson.annotations.SerializedName;

public class ClosedItem{

	@SerializedName("patient_latitude")
	private String patientLatitude;

	@SerializedName("distance")
	private String distance;

	@SerializedName("total_selling_price")
	private String totalSellingPrice;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("pharmacy_id")
	private String pharmacyId;

	@SerializedName("patient_longitude")
	private String patientLongitude;

	@SerializedName("patient_name")
	private String patientName;

	@SerializedName("medicine_order_id")
	private String medicineOrderId;

	@SerializedName("total_pharmacy_price")
	private String totalPharmacyPrice;

	@SerializedName("id")
	private String id;

	@SerializedName("patient_image")
	private String patientImage;

	@SerializedName("travel_time")
	private Object travelTime;

	@SerializedName("ordered_date")
	private String ordered_date;

	public String getOrdered_date() {
		return ordered_date;
	}

	public void setOrdered_date(String ordered_date) {
		this.ordered_date = ordered_date;
	}

	public void setPatientLatitude(String patientLatitude){
		this.patientLatitude = patientLatitude;
	}

	public String getPatientLatitude(){
		return patientLatitude;
	}

	public void setDistance(String distance){
		this.distance = distance;
	}

	public String getDistance(){
		return distance;
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

	public void setPatientImage(String patientImage){
		this.patientImage = patientImage;
	}

	public String getPatientImage(){
		return patientImage;
	}

	public void setTravelTime(Object travelTime){
		this.travelTime = travelTime;
	}

	public Object getTravelTime(){
		return travelTime;
	}

	@Override
 	public String toString(){
		return 
			"DeliveryAssign{" +
			"patient_latitude = '" + patientLatitude + '\'' + 
			",distance = '" + distance + '\'' + 
			",total_selling_price = '" + totalSellingPrice + '\'' + 
			",total_amount = '" + totalAmount + '\'' + 
			",pharmacy_id = '" + pharmacyId + '\'' + 
			",patient_longitude = '" + patientLongitude + '\'' + 
			",patient_name = '" + patientName + '\'' + 
			",medicine_order_id = '" + medicineOrderId + '\'' + 
			",total_pharmacy_price = '" + totalPharmacyPrice + '\'' + 
			",id = '" + id + '\'' + 
			",patient_image = '" + patientImage + '\'' + 
			",travel_time = '" + travelTime + '\'' +
					",ordered_date = '" + ordered_date + '\'' +
					"}";
		}
}