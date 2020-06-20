package com.procter.model;

import java.util.List;

public class DeliveryExecutiveModel  {


    /**
     * status : true
     * data : [{"id":"4","name":"Pawan Kavi","phone":"6361592133","email":"pawan.kh@provbamail.com","image":"uploads/delivery/profile/22e0b6455b32c6d1ef.png","address":"Provab Technosoft, Hewlett Packard Avenue, Konappana Agrahara, Electronic City, Bengaluru, Karnataka, India","vehicle_name":"Bajaj V15","vehicle_number":"KA14 EL 6464","address_proof":"","driving_license":"uploads/delivery/license/cbdec6b575e57ecf44.jpg","insurance_proof":"uploads/delivery/insurance/42652aacf6c6e88a6d.png","registration_certificate":"uploads/delivery/certificate/4067c199178c3542d8.png","other_document":null}]
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
         * id : 4
         * name : Pawan Kavi
         * phone : 6361592133
         * email : pawan.kh@provbamail.com
         * image : uploads/delivery/profile/22e0b6455b32c6d1ef.png
         * address : Provab Technosoft, Hewlett Packard Avenue, Konappana Agrahara, Electronic City, Bengaluru, Karnataka, India
         * vehicle_name : Bajaj V15
         * vehicle_number : KA14 EL 6464
         * address_proof :
         * driving_license : uploads/delivery/license/cbdec6b575e57ecf44.jpg
         * insurance_proof : uploads/delivery/insurance/42652aacf6c6e88a6d.png
         * registration_certificate : uploads/delivery/certificate/4067c199178c3542d8.png
         * other_document : null
         */

        private String id;
        private String name;
        private String phone;
        private String email;
        private String owner_id;
        private String image;
        private String address;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        private boolean isSelect;
        private String vehicle_name;
        private String vehicle_number;
        private String address_proof;
        private String driving_license;
        private String insurance_proof;
        private String registration_certificate;
        private String other_document;

        public String getOwner_id() {
            return owner_id;
        }

        public void setOwner_id(String owner_id) {
            this.owner_id = owner_id;
        }


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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getVehicle_name() {
            return vehicle_name;
        }

        public void setVehicle_name(String vehicle_name) {
            this.vehicle_name = vehicle_name;
        }

        public String getVehicle_number() {
            return vehicle_number;
        }

        public void setVehicle_number(String vehicle_number) {
            this.vehicle_number = vehicle_number;
        }

        public String getAddress_proof() {
            return address_proof;
        }

        public void setAddress_proof(String address_proof) {
            this.address_proof = address_proof;
        }

        public String getDriving_license() {
            return driving_license;
        }

        public void setDriving_license(String driving_license) {
            this.driving_license = driving_license;
        }

        public String getInsurance_proof() {
            return insurance_proof;
        }

        public void setInsurance_proof(String insurance_proof) {
            this.insurance_proof = insurance_proof;
        }

        public String getRegistration_certificate() {
            return registration_certificate;
        }

        public void setRegistration_certificate(String registration_certificate) {
            this.registration_certificate = registration_certificate;
        }

        public String getOther_document() {
            return other_document;
        }

        public void setOther_document(String other_document) {
            this.other_document = other_document;
        }
    }
}
