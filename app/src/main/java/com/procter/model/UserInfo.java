package com.procter.model;

public class UserInfo {


    /**
     * status : true
     * data : {"refresh_token":"60123d755afeec120e514b1324d2c7a1f820aba9","profile":{"pharmacy_name":"Shivamogga Drug House","drug_start_date":"2015-08-10","drug_end_date":"2035-08-09","drug_license":"uploads/users/pharmacy/license/8d50f6adf8a8f27df0.jpg","registration_end_date":"2035-08-09","registration_start_date":"2015-08-10","registration_certificate":"uploads/users/pharmacy/certificate/b8bf7a956e0a39e379.jpg","city_id":"1","user_id":"3","name":"Vishwanath Gudisagar","email":"vishwanathgudisagar.provab@gmail.com","phone":"9481858145","gender":"1","address":"Electronic city bengaluru","pincode":"560100","image":"uploads/users/pharmacy/profile/51c20a9dead3876e6a.png","status":"1","pharmacy_id":"1","user_type":"3"},"auth_key":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvMjMuMjAuNS44XC9wb2t0b3JcLyIsIm5iZiI6MTU2MTYxMTA1NSwiaWF0IjoxNTYxNjExMDU1LCJleHAiOjE1NjE2OTc0NTUsInVzZXJfaWQiOiIzIiwidXNlcl90eXBlIjozLCJlbWFpbCI6InZpc2h3YW5hdGhndWRpc2FnYXIucHJvdmFiQGdtYWlsLmNvbSIsInBoYXJtYWN5X2lkIjoiMSIsImZjbV9pZCI6ImRzZmRzZmRzZiJ9.TonNLRDt-gXA4MRCCasFUn5ofYnSvhDX8BoloTvlRh4"}
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
         * refresh_token : 60123d755afeec120e514b1324d2c7a1f820aba9
         * profile : {"pharmacy_name":"Shivamogga Drug House","drug_start_date":"2015-08-10","drug_end_date":"2035-08-09","drug_license":"uploads/users/pharmacy/license/8d50f6adf8a8f27df0.jpg","registration_end_date":"2035-08-09","registration_start_date":"2015-08-10","registration_certificate":"uploads/users/pharmacy/certificate/b8bf7a956e0a39e379.jpg","city_id":"1","user_id":"3","name":"Vishwanath Gudisagar","email":"vishwanathgudisagar.provab@gmail.com","phone":"9481858145","gender":"1","address":"Electronic city bengaluru","pincode":"560100","image":"uploads/users/pharmacy/profile/51c20a9dead3876e6a.png","status":"1","pharmacy_id":"1","user_type":"3"}
         * auth_key : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwczpcL1wvMjMuMjAuNS44XC9wb2t0b3JcLyIsIm5iZiI6MTU2MTYxMTA1NSwiaWF0IjoxNTYxNjExMDU1LCJleHAiOjE1NjE2OTc0NTUsInVzZXJfaWQiOiIzIiwidXNlcl90eXBlIjozLCJlbWFpbCI6InZpc2h3YW5hdGhndWRpc2FnYXIucHJvdmFiQGdtYWlsLmNvbSIsInBoYXJtYWN5X2lkIjoiMSIsImZjbV9pZCI6ImRzZmRzZmRzZiJ9.TonNLRDt-gXA4MRCCasFUn5ofYnSvhDX8BoloTvlRh4
         */

        private String refresh_token;
        private ProfileBean profile;
        private String auth_key;

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public String getAuth_key() {
            return auth_key;
        }

        public void setAuth_key(String auth_key) {
            this.auth_key = auth_key;
        }

        public static class ProfileBean {
            /**
             * pharmacy_name : Shivamogga Drug House
             * drug_start_date : 2015-08-10
             * drug_end_date : 2035-08-09
             * drug_license : uploads/users/pharmacy/license/8d50f6adf8a8f27df0.jpg
             * registration_end_date : 2035-08-09
             * registration_start_date : 2015-08-10
             * registration_certificate : uploads/users/pharmacy/certificate/b8bf7a956e0a39e379.jpg
             * city_id : 1
             * user_id : 3
             * name : Vishwanath Gudisagar
             * email : vishwanathgudisagar.provab@gmail.com
             * phone : 9481858145
             * gender : 1
             * address : Electronic city bengaluru
             * pincode : 560100
             * image : uploads/users/pharmacy/profile/51c20a9dead3876e6a.png
             * status : 1
             * pharmacy_id : 1
             * user_type : 3
             */

            private String pharmacy_name;
            private String drug_start_date;
            private String drug_end_date;
            private String drug_license;
            private String registration_end_date;
            private String registration_start_date;
            private String registration_certificate;

            public String getOther_document() {
                return other_document;
            }

            public void setOther_document(String other_document) {
                this.other_document = other_document;
            }

            private String other_document;
            private String city_id;
            private String user_id;
            private String name;

            public String getAuth_key() {
                return auth_key;
            }

            public void setAuth_key(String auth_key) {
                this.auth_key = auth_key;
            }

            public String getRefresh_token() {
                return refresh_token;
            }

            public void setRefresh_token(String refresh_token) {
                this.refresh_token = refresh_token;
            }

            private String refresh_token;
            private String auth_key;
            private String email;
            private String phone;
            private String gender;
            private String address;
            private String pincode;
            private String image;
            private String status;
            private String pharmacy_id;
            private String user_type;

            public String getPharmacy_name() {
                return pharmacy_name;
            }

            public void setPharmacy_name(String pharmacy_name) {
                this.pharmacy_name = pharmacy_name;
            }

            public String getDrug_start_date() {
                return drug_start_date;
            }

            public void setDrug_start_date(String drug_start_date) {
                this.drug_start_date = drug_start_date;
            }

            public String getDrug_end_date() {
                return drug_end_date;
            }

            public void setDrug_end_date(String drug_end_date) {
                this.drug_end_date = drug_end_date;
            }

            public String getDrug_license() {
                return drug_license;
            }

            public void setDrug_license(String drug_license) {
                this.drug_license = drug_license;
            }

            public String getRegistration_end_date() {
                return registration_end_date;
            }

            public void setRegistration_end_date(String registration_end_date) {
                this.registration_end_date = registration_end_date;
            }

            public String getRegistration_start_date() {
                return registration_start_date;
            }

            public void setRegistration_start_date(String registration_start_date) {
                this.registration_start_date = registration_start_date;
            }

            public String getRegistration_certificate() {
                return registration_certificate;
            }

            public void setRegistration_certificate(String registration_certificate) {
                this.registration_certificate = registration_certificate;
            }

            public String getCity_id() {
                return city_id;
            }

            public void setCity_id(String city_id) {
                this.city_id = city_id;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
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

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPincode() {
                return pincode;
            }

            public void setPincode(String pincode) {
                this.pincode = pincode;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getPharmacy_id() {
                return pharmacy_id;
            }

            public void setPharmacy_id(String pharmacy_id) {
                this.pharmacy_id = pharmacy_id;
            }

            public String getUser_type() {
                return user_type;
            }

            public void setUser_type(String user_type) {
                this.user_type = user_type;
            }
        }
    }
}
