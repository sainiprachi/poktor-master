package com.procter.model.pending;

import com.google.gson.annotations.SerializedName;

public class PendingItem{

	@SerializedName("patient_latitude")
	private String patientLatitude;

	@SerializedName("distance")
	private Object distance;

	@SerializedName("patient_longitude")
	private String patientLongitude;

	@SerializedName("patient_image")
	private String patientImage;

	@SerializedName("travel_time")
	private Object travelTime;

	@SerializedName("total_selling_price")
	private String totalSellingPrice;

	@SerializedName("total_amount")
	private String totalAmount;

	@SerializedName("pharmacy_id")
	private String pharmacyId;

	@SerializedName("patient_name")
	private String patientName;

	@SerializedName("medicine_order_id")
	private String medicineOrderId;

	@SerializedName("total_pharmacy_price")
	private String totalPharmacyPrice;

	@SerializedName("id")
	private String id;

	@SerializedName("bid_price")
	private String bidPrice;

	@SerializedName("expire_time")
	private String expire_time;

	@SerializedName("ordered_date")
	private String ordered_date;

	public String getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}

	public void setPatientLatitude(String patientLatitude){
		this.patientLatitude = patientLatitude;
	}

	public String getPatientLatitude(){
		return patientLatitude;
	}

	public void setDistance(Object distance){
		this.distance = distance;
	}

	public Object getDistance(){
		return distance;
	}

	public void setPatientLongitude(String patientLongitude){
		this.patientLongitude = patientLongitude;
	}

	public String getPatientLongitude(){
		return patientLongitude;
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

	public void setBidPrice(String bidPrice){
		this.bidPrice = bidPrice;
	}

	public String getBidPrice(){
		return bidPrice;
	}

	public String getOrdered_date() {
		return ordered_date;
	}

	public void setOrdered_date(String ordered_date) {
		this.ordered_date = ordered_date;
	}

	@Override
 	public String toString(){
		return 
			"DeliveryAssign{" +
			"patient_latitude = '" + patientLatitude + '\'' + 
			",distance = '" + distance + '\'' + 
			",patient_longitude = '" + patientLongitude + '\'' + 
			",patient_image = '" + patientImage + '\'' + 
			",travel_time = '" + travelTime + '\'' + 
			",total_selling_price = '" + totalSellingPrice + '\'' + 
			",total_amount = '" + totalAmount + '\'' + 
			",pharmacy_id = '" + pharmacyId + '\'' + 
			",patient_name = '" + patientName + '\'' + 
			",medicine_order_id = '" + medicineOrderId + '\'' + 
			",total_pharmacy_price = '" + totalPharmacyPrice + '\'' + 
			",id = '" + id + '\'' + 
			",bid_price = '" + bidPrice + '\'' +
					",expire_time = '" + expire_time + '\'' +
					",ordered_date = '" + ordered_date + '\'' +
			"}";
		}
}