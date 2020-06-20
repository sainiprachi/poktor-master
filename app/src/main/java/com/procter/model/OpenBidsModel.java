package com.procter.model;

import java.util.List;

public class OpenBidsModel {


    /**
     * key : open_bids
     * response : {"status":true,"message":"Open Bid List","data":[{"patient_distance":"2.72","distance":null,"travel_time":null,"pharmacy_id":"29","id":"2","medicine_order_id":"MED5cb6b41643636","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00"},{"patient_distance":"2.72","distance":null,"travel_time":null,"pharmacy_id":"29","id":"4","medicine_order_id":"MED5cb6b41643638","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00"},{"patient_distance":"2.72","distance":null,"travel_time":null,"pharmacy_id":"29","id":"5","medicine_order_id":"MED5cb6b41643639","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00"}]}
     */

    private String key;
    private ResponseBean response;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ResponseBean getResponse() {
        return response;
    }

    public void setResponse(ResponseBean response) {
        this.response = response;
    }

    public static class ResponseBean {
        /**
         * status : true
         * message : Open Bid List
         * data : [{"patient_distance":"2.72","distance":null,"travel_time":null,"pharmacy_id":"29","id":"2","medicine_order_id":"MED5cb6b41643636","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00"},{"patient_distance":"2.72","distance":null,"travel_time":null,"pharmacy_id":"29","id":"4","medicine_order_id":"MED5cb6b41643638","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00"},{"patient_distance":"2.72","distance":null,"travel_time":null,"pharmacy_id":"29","id":"5","medicine_order_id":"MED5cb6b41643639","patient_name":"Pawan K H","patient_latitude":"12.8874899","patient_longitude":"77.6391859","total_amount":"250.00","total_pharmacy_price":"250.00","total_selling_price":"405.00"}]
         */

        private boolean status;
        private String message;
        private List<DataBean> data;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
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
             * patient_distance : 2.72
             * distance : null
             * travel_time : null
             * pharmacy_id : 29
             * id : 2
             * medicine_order_id : MED5cb6b41643636
             * patient_name : Pawan K H
             * patient_latitude : 12.8874899
             * patient_longitude : 77.6391859
             * total_amount : 250.00
             * total_pharmacy_price : 250.00
             * total_selling_price : 405.00
             */

            private String patient_distance;
            private Object distance;
            private Object travel_time;
            private String pharmacy_id;
            private String id;
            private String medicine_order_id;



            public String getOrdered_date() {
                return ordered_date;
            }

            public void setOrdered_date(String ordered_date) {
                this.ordered_date = ordered_date;
            }

            private String ordered_date;
            private String patient_name;
            private String patient_latitude;
            private String patient_longitude;
            private String total_amount;
            private String expire_time;

            public String getPatient_image() {
                return patient_image;
            }

            public void setPatient_image(String patient_image) {
                this.patient_image = patient_image;
            }

            private String patient_image;
            private String total_pharmacy_price;
            private String total_selling_price;

            public String getExpire_time() {
                return expire_time;
            }

            public void setExpire_time(String expire_time) {
                this.expire_time = expire_time;
            }

            public String getPatient_distance() {
                return patient_distance;
            }

            public void setPatient_distance(String patient_distance) {
                this.patient_distance = patient_distance;
            }

            public Object getDistance() {
                return distance;
            }

            public void setDistance(Object distance) {
                this.distance = distance;
            }

            public Object getTravel_time() {
                return travel_time;
            }

            public void setTravel_time(Object travel_time) {
                this.travel_time = travel_time;
            }

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
        }
    }
}
