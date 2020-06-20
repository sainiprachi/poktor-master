package com.procter.model;

import java.util.List;

public class OrdersModel  {


    /**
     * status : true
     * data : [{"id":"3","medicine_order_id":"MED5cb6b41643637","patient_name":"Pawan K H","patient_email":"pawankh.provab@gmail.com","patient_phone":"9741842614","patient_address":"E174, 3rd Cross Rd, Chikka Begur, Industrial Layout, Begur, Bengaluru, Karnataka 560068","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00","delivery_status":"4"}]
     * code : 200
     * message :
     */

    private boolean status;
    private int code;
    private String message;
    private List<DataBean> data;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * medicine_order_id : MED5cb6b41643637
         * patient_name : Pawan K H
         * patient_email : pawankh.provab@gmail.com
         * patient_phone : 9741842614
         * patient_address : E174, 3rd Cross Rd, Chikka Begur, Industrial Layout, Begur, Bengaluru, Karnataka 560068
         * patient_latitude : 12.8874899
         * patient_longitude : 77.6391859
         * total_amount : 250.00
         * total_pharmacy_price : 250.00
         * total_selling_price : 405.00
         * delivery_status : 4
         */

        private String id;
        private String medicine_order_id;
        private String patient_name;
        private String patient_email;
        private String patient_phone;
        private String patient_address;
        private String patient_latitude;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String image;
        private String patient_longitude;
        private String total_amount;
        private String total_pharmacy_price;
        private String total_selling_price;
        private String delivery_status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMedicine_order_id() {
            return medicine_order_id;
        }

        public void setMedicine_order_id(String medicine_order_id) {
            this.medicine_order_id = medicine_order_id;
        }

        public String getPatient_name() {
            return patient_name;
        }

        public void setPatient_name(String patient_name) {
            this.patient_name = patient_name;
        }

        public String getPatient_email() {
            return patient_email;
        }

        public void setPatient_email(String patient_email) {
            this.patient_email = patient_email;
        }

        public String getPatient_phone() {
            return patient_phone;
        }

        public void setPatient_phone(String patient_phone) {
            this.patient_phone = patient_phone;
        }

        public String getPatient_address() {
            return patient_address;
        }

        public void setPatient_address(String patient_address) {
            this.patient_address = patient_address;
        }

        public String getPatient_latitude() {
            return patient_latitude;
        }

        public void setPatient_latitude(String patient_latitude) {
            this.patient_latitude = patient_latitude;
        }

        public String getPatient_longitude() {
            return patient_longitude;
        }

        public void setPatient_longitude(String patient_longitude) {
            this.patient_longitude = patient_longitude;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getTotal_pharmacy_price() {
            return total_pharmacy_price;
        }

        public void setTotal_pharmacy_price(String total_pharmacy_price) {
            this.total_pharmacy_price = total_pharmacy_price;
        }

        public String getTotal_selling_price() {
            return total_selling_price;
        }

        public void setTotal_selling_price(String total_selling_price) {
            this.total_selling_price = total_selling_price;
        }

        public String getDelivery_status() {
            return delivery_status;
        }

        public void setDelivery_status(String delivery_status) {
            this.delivery_status = delivery_status;
        }
    }
}
