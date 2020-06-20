package com.procter.model.order;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetails {

    @SerializedName("patient_latitude")
    private String patientLatitude;

    @SerializedName("distance")
    private String distance;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("bidding_status")
    private String biddingStatus;

    @SerializedName("total_market_price")
    private String totalMarketPrice;

    @SerializedName("patient_address")
    private String patientAddress;

    @SerializedName("travel_time")
    private String travelTime;

    @SerializedName("consultation_id")
    private String consultationId;

    @SerializedName("coupon_id")
    private String couponId;

    @SerializedName("patient_name")
    private String patientName;

    @SerializedName("medicine_order_id")
    private String medicineOrderId;

    @SerializedName("patient_phone")
    private String patientPhone;

    @SerializedName("id")
    private String id;

    @SerializedName("order_type")
    private String orderType;

    @SerializedName("order_items")
    private List<OrderItemsItem> orderItems;

    @SerializedName("patient_address_id")
    private String patientAddressId;

    @SerializedName("coupon_amount")
    private String couponAmount;

    @SerializedName("order_activities")
    private List<Object> orderActivities;

    @SerializedName("payment_status")
    private String paymentStatus;

    @SerializedName("patient_longitude")
    private String patientLongitude;

    @SerializedName("driver_phone")
    private Object driverPhone;

    @SerializedName("driver_name")
    private Object driverName;

    @SerializedName("total_selling_price")
    private String totalSellingPrice;

    @SerializedName("total_amount")
    private String totalAmount;

    @SerializedName("pharmacy_id")
    private String pharmacyId;

    @SerializedName("patient_id")
    private String patientId;

    @SerializedName("ordered_date")
    private String orderedDate;

    @SerializedName("patient_email")
    private String patientEmail;

    @SerializedName("total_pharmacy_price")
    private String totalPharmacyPrice;

    @SerializedName("loyalty_point")
    private String loyaltyPoint;

    @SerializedName("payable_amount")
    private String payableAmount;

    @SerializedName("delivery_status")
    private String deliveryStatus;

    @SerializedName("expire_time")
    private String expire_time;

    public String getPatientLatitude() {
        return patientLatitude;
    }

    public void setPatientLatitude(String patientLatitude) {
        this.patientLatitude = patientLatitude;
    }

    public Object getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getBiddingStatus() {
        return biddingStatus;
    }

    public void setBiddingStatus(String biddingStatus) {
        this.biddingStatus = biddingStatus;
    }

    public String getTotalMarketPrice() {
        return totalMarketPrice;
    }

    public void setTotalMarketPrice(String totalMarketPrice) {
        this.totalMarketPrice = totalMarketPrice;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

    public Object getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getMedicineOrderId() {
        return medicineOrderId;
    }

    public void setMedicineOrderId(String medicineOrderId) {
        this.medicineOrderId = medicineOrderId;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public List<OrderItemsItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemsItem> orderItems) {
        this.orderItems = orderItems;
    }

    public String getPatientAddressId() {
        return patientAddressId;
    }

    public void setPatientAddressId(String patientAddressId) {
        this.patientAddressId = patientAddressId;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public List<Object> getOrderActivities() {
        return orderActivities;
    }

    public void setOrderActivities(List<Object> orderActivities) {
        this.orderActivities = orderActivities;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPatientLongitude() {
        return patientLongitude;
    }

    public void setPatientLongitude(String patientLongitude) {
        this.patientLongitude = patientLongitude;
    }

    public Object getDriverPhone() {
        return driverPhone;
    }

    public void setDriverPhone(Object driverPhone) {
        this.driverPhone = driverPhone;
    }

    public Object getDriverName() {
        return driverName;
    }

    public void setDriverName(Object driverName) {
        this.driverName = driverName;
    }

    public String getTotalSellingPrice() {
        return totalSellingPrice;
    }

    public void setTotalSellingPrice(String totalSellingPrice) {
        this.totalSellingPrice = totalSellingPrice;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getTotalPharmacyPrice() {
        return totalPharmacyPrice;
    }

    public void setTotalPharmacyPrice(String totalPharmacyPrice) {
        this.totalPharmacyPrice = totalPharmacyPrice;
    }

    public String getLoyaltyPoint() {
        return loyaltyPoint;
    }

    public void setLoyaltyPoint(String loyaltyPoint) {
        this.loyaltyPoint = loyaltyPoint;
    }

    public String getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        this.payableAmount = payableAmount;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    @Override
    public String toString() {
        return
                "Data{" +
                        "patient_latitude = '" + patientLatitude + '\'' +
                        ",distance = '" + distance + '\'' +
                        ",created_at = '" + createdAt + '\'' +
                        ",bidding_status = '" + biddingStatus + '\'' +
                        ",total_market_price = '" + totalMarketPrice + '\'' +
                        ",patient_address = '" + patientAddress + '\'' +
                        ",travel_time = '" + travelTime + '\'' +
                        ",consultation_id = '" + consultationId + '\'' +
                        ",coupon_id = '" + couponId + '\'' +
                        ",patient_name = '" + patientName + '\'' +
                        ",medicine_order_id = '" + medicineOrderId + '\'' +
                        ",patient_phone = '" + patientPhone + '\'' +
                        ",id = '" + id + '\'' +
                        ",order_type = '" + orderType + '\'' +
                        ",order_items = '" + orderItems + '\'' +
                        ",patient_address_id = '" + patientAddressId + '\'' +
                        ",coupon_amount = '" + couponAmount + '\'' +
                        ",order_activities = '" + orderActivities + '\'' +
                        ",payment_status = '" + paymentStatus + '\'' +
                        ",patient_longitude = '" + patientLongitude + '\'' +
                        ",driver_phone = '" + driverPhone + '\'' +
                        ",driver_name = '" + driverName + '\'' +
                        ",total_selling_price = '" + totalSellingPrice + '\'' +
                        ",total_amount = '" + totalAmount + '\'' +
                        ",pharmacy_id = '" + pharmacyId + '\'' +
                        ",patient_id = '" + patientId + '\'' +
                        ",ordered_date = '" + orderedDate + '\'' +
                        ",patient_email = '" + patientEmail + '\'' +
                        ",total_pharmacy_price = '" + totalPharmacyPrice + '\'' +
                        ",loyalty_point = '" + loyaltyPoint + '\'' +
                        ",payable_amount = '" + payableAmount + '\'' +
                        ",delivery_status = '" + deliveryStatus + '\'' +
                        ",expire_time = '" + expire_time + '\'' +
                        "}";
    }
}