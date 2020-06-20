package com.procter.model;


import java.util.List;

public class HomeModel {


    /**
     * status : true
     * data : {"profile":{"address":"","latitude":null,"longitude":null,"registration_number":"","pharmacy_name":"XYZ","drug_start_date":"2019-04-01","drug_end_date":"2039-04-01","drug_license":"uploads/users/pharmacy/license/1a716860a6181ee275.png","registration_end_date":"2039-04-01","registration_start_date":"2019-04-01","registration_certificate":"uploads/users/pharmacy/certificate/5e286a4613c9337e53.png","city_id":"0","user_id":"22","name":"Vishwanath Gudisagar","email":"vishwanath@gmail.com","phone":"9481858145","gender":"1","pincode":"","image":null,"status":"1","pharmacy_id":"3","user_type":"3"},"open_bids_count":0,"winning_bids_count":0,"pending_bids_count":0,"total_earnings":2450,"watch_list":[{"pharmacy_id":"22","id":"1","medicine_order_id":"MED5cb6b41643635","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"220.00","total_selling_price":"405.00","ordered_date":"2019-07-23 12:28:27"}],"notification_count":10,"delivering_order_count":0,"unassigned_order_count":0,"expiring_bids_count":0}
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
         * profile : {"address":"","latitude":null,"longitude":null,"registration_number":"","pharmacy_name":"XYZ","drug_start_date":"2019-04-01","drug_end_date":"2039-04-01","drug_license":"uploads/users/pharmacy/license/1a716860a6181ee275.png","registration_end_date":"2039-04-01","registration_start_date":"2019-04-01","registration_certificate":"uploads/users/pharmacy/certificate/5e286a4613c9337e53.png","city_id":"0","user_id":"22","name":"Vishwanath Gudisagar","email":"vishwanath@gmail.com","phone":"9481858145","gender":"1","pincode":"","image":null,"status":"1","pharmacy_id":"3","user_type":"3"}
         * open_bids_count : 0
         * winning_bids_count : 0
         * pending_bids_count : 0
         * total_earnings : 2450
         * watch_list : [{"pharmacy_id":"22","id":"1","medicine_order_id":"MED5cb6b41643635","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"220.00","total_selling_price":"405.00","ordered_date":"2019-07-23 12:28:27"}]
         * notification_count : 10
         * delivering_order_count : 0
         * unassigned_order_count : 0
         * expiring_bids_count : 0
         */

        private ProfileBean profile;
        private int open_bids_count;
        private int winning_bids_count;
        private int pending_bids_count;
        private int total_earnings;
        private int notification_count;
        private int delivering_order_count;
        private int unassigned_order_count;
        private int expiring_bids_count;
        private List<WatchListBean> watch_list;
        private String rating;
        private Settings settings;

        public ProfileBean getProfile() {
            return profile;
        }

        public void setProfile(ProfileBean profile) {
            this.profile = profile;
        }

        public int getOpen_bids_count() {
            return open_bids_count;
        }

        public void setOpen_bids_count(int open_bids_count) {
            this.open_bids_count = open_bids_count;
        }

        public int getWinning_bids_count() {
            return winning_bids_count;
        }

        public void setWinning_bids_count(int winning_bids_count) {
            this.winning_bids_count = winning_bids_count;
        }

        public int getPending_bids_count() {
            return pending_bids_count;
        }

        public void setPending_bids_count(int pending_bids_count) {
            this.pending_bids_count = pending_bids_count;
        }

        public int getTotal_earnings() {
            return total_earnings;
        }

        public void setTotal_earnings(int total_earnings) {
            this.total_earnings = total_earnings;
        }

        public int getNotification_count() {
            return notification_count;
        }

        public void setNotification_count(int notification_count) {
            this.notification_count = notification_count;
        }

        public int getDelivering_order_count() {
            return delivering_order_count;
        }

        public void setDelivering_order_count(int delivering_order_count) {
            this.delivering_order_count = delivering_order_count;
        }

        public int getUnassigned_order_count() {
            return unassigned_order_count;
        }

        public void setUnassigned_order_count(int unassigned_order_count) {
            this.unassigned_order_count = unassigned_order_count;
        }

        public int getExpiring_bids_count() {
            return expiring_bids_count;
        }

        public void setExpiring_bids_count(int expiring_bids_count) {
            this.expiring_bids_count = expiring_bids_count;
        }

        public List<WatchListBean> getWatch_list() {
            return watch_list;
        }

        public void setWatch_list(List<WatchListBean> watch_list) {
            this.watch_list = watch_list;
        }


        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public Settings getSettings() {
            return settings;
        }

        public void setSettings(Settings settings) {
            this.settings = settings;
        }

        public static class ProfileBean {
            /**
             * address :
             * latitude : null
             * longitude : null
             * registration_number :
             * pharmacy_name : XYZ
             * drug_start_date : 2019-04-01
             * drug_end_date : 2039-04-01
             * drug_license : uploads/users/pharmacy/license/1a716860a6181ee275.png
             * registration_end_date : 2039-04-01
             * registration_start_date : 2019-04-01
             * registration_certificate : uploads/users/pharmacy/certificate/5e286a4613c9337e53.png
             * city_id : 0
             * user_id : 22
             * name : Vishwanath Gudisagar
             * email : vishwanath@gmail.com
             * phone : 9481858145
             * gender : 1
             * pincode :
             * image : null
             * status : 1
             * pharmacy_id : 3
             * user_type : 3
             */

            private String address="";
            private Object latitude;
            private Object longitude;
            private String registration_number;
            private String pharmacy_name;
            private String drug_start_date;
            private String drug_end_date;
            private String drug_license;
            private String registration_end_date;
            private String registration_start_date;
            private String registration_certificate;
            private String city_id;
            private String user_id;
            private String name;
            private String email;
            private String phone;
            private String gender;
            private String pincode;
            private String image;
            private String status;
            private String pharmacy_id;
            private String user_type;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public Object getLatitude() {
                return latitude;
            }

            public void setLatitude(Object latitude) {
                this.latitude = latitude;
            }

            public Object getLongitude() {
                return longitude;
            }

            public void setLongitude(Object longitude) {
                this.longitude = longitude;
            }

            public String getRegistration_number() {
                return registration_number;
            }

            public void setRegistration_number(String registration_number) {
                this.registration_number = registration_number;
            }

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

        public static class WatchListBean {
            /**
             * pharmacy_id : 22
             * id : 1
             * medicine_order_id : MED5cb6b41643635
             * patient_name : Pawan K H
             * patient_latitude : 12.8874899
             * patient_longitude : 77.6391859
             * total_amount : 250.00
             * total_pharmacy_price : 220.00
             * total_selling_price : 405.00
             * ordered_date : 2019-07-23 12:28:27
             */

            private String pharmacy_id;
            private String id;
            private String medicine_order_id;
            private String patient_name;
            private String patient_latitude;
            private String patient_longitude;
            private String total_amount;
            private String total_pharmacy_price;
            private String total_selling_price;
            private String ordered_date;

            public String getPharmacy_id() {
                return pharmacy_id;
            }

            public void setPharmacy_id(String pharmacy_id) {
                this.pharmacy_id = pharmacy_id;
            }

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

            public String getOrdered_date() {
                return ordered_date;
            }

            public void setOrdered_date(String ordered_date) {
                this.ordered_date = ordered_date;
            }
        }

        public static class Settings{
            private String price_for_call;
            private String price_for_chat;
            private String price_for_offline;
            private String first_discount;

            private String bid_percentage;
            private String bid_minimum_value;

            public String getPrice_for_call() {
                return price_for_call;
            }

            public void setPrice_for_call(String price_for_call) {
                this.price_for_call = price_for_call;
            }

            public String getPrice_for_chat() {
                return price_for_chat;
            }

            public void setPrice_for_chat(String price_for_chat) {
                this.price_for_chat = price_for_chat;
            }

            public String getPrice_for_offline() {
                return price_for_offline;
            }

            public void setPrice_for_offline(String price_for_offline) {
                this.price_for_offline = price_for_offline;
            }

            public String getFirst_discount() {
                return first_discount;
            }

            public void setFirst_discount(String first_discount) {
                this.first_discount = first_discount;
            }

            public String getBid_percentage() {
                return bid_percentage;
            }

            public void setBid_percentage(String bid_percentage) {
                this.bid_percentage = bid_percentage;
            }

            public String getBid_minimum_value() {
                return bid_minimum_value;
            }

            public void setBid_minimum_value(String bid_minimum_value) {
                this.bid_minimum_value = bid_minimum_value;
            }
        }
    }

}
