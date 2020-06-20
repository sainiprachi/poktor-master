package com.procter.model;

public class DriverDetailModel {


    /**
     * status : true
     * data : {"id":"4","name":"Pawan Kavi","email":"pawan.kh@provbamail.com","phone":"6361592133","address":"Provab Technosoft, Hewlett Packard Avenue, Konappana Agrahara, Electronic City, Bengaluru, Karnataka, India","latitude":"12.8545543","longitude":"77.66231240000002","image":"uploads/delivery/profile/22e0b6455b32c6d1ef.png","vehicle_number":"KA14 EL 6464","driving_license":"uploads/delivery/license/cbdec6b575e57ecf44.jpg","other_document":null,"owner_id":"","status":"1","city":null,"state":null,"pincode":null}
     * code : 200
     * message :
     */

    private boolean status;
    private DataBean data;
    private int code;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /**
         * id : 4
         * name : Pawan Kavi
         * email : pawan.kh@provbamail.com
         * phone : 6361592133
         * address : Provab Technosoft, Hewlett Packard Avenue, Konappana Agrahara, Electronic City, Bengaluru, Karnataka, India
         * latitude : 12.8545543
         * longitude : 77.66231240000002
         * image : uploads/delivery/profile/22e0b6455b32c6d1ef.png
         * vehicle_number : KA14 EL 6464
         * driving_license : uploads/delivery/license/cbdec6b575e57ecf44.jpg
         * other_document : null
         * owner_id :
         * status : 1
         * city : null
         * state : null
         * pincode : null
         */

        private String id;
        private String name;
        private String email;
        private String phone;
        private String address;
        private String latitude;
        private String longitude;
        private String image;
        private String vehicle_number;
        private String driving_license;
        private String other_document;
        private String owner_id;
        private String status;
        private String city;
        private String state;
        private String pincode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getVehicle_number() {
            return vehicle_number;
        }

        public void setVehicle_number(String vehicle_number) {
            this.vehicle_number = vehicle_number;
        }

        public String getDriving_license() {
            return driving_license;
        }

        public void setDriving_license(String driving_license) {
            this.driving_license = driving_license;
        }

        public String getOther_document() {
            return other_document;
        }

        public void setOther_document(String other_document) {
            this.other_document = other_document;
        }

        public String getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(String owner_id) {
            this.owner_id = owner_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }
    }
}
