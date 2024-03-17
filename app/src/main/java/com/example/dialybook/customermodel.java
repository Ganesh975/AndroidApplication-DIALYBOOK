package com.example.dialybook;

public class customermodel {

    String customername;
    String datetime , seller_name;

    public String getPayement_method() {
        return payement_method;
    }

    public void setPayement_method(String payement_method) {
        this.payement_method = payement_method;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public customermodel(String customername, String datetime, String payement_method, int total_purchase, String seller_name) {
        this.customername = customername;
        this.datetime = datetime;
        this.payement_method = payement_method;
        this.total_purchase = total_purchase;
        this.seller_name=seller_name;
    }

    String payement_method;
    int total_purchase;

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }




    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public int  getTotal_purchase() {
        return total_purchase;
    }

    public void setTotal_purchase(int total_purchase) {
        this.total_purchase = total_purchase;
    }

    public customermodel(String customername, String datetime, int total_purchase) {
        this.customername = customername;

        this.datetime = datetime;
        this.total_purchase = total_purchase;
    }

    public customermodel() {
    }
}
