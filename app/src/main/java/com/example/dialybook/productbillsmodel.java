package com.example.dialybook;

public class productbillsmodel {
    String datetime,customername,seller_name;
    Integer quantity,cost;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public productbillsmodel(String datetime, String customername, Integer quantity, Integer cost, String seller_name) {
        this.datetime = datetime;
        this.customername = customername;
        this.quantity = quantity;
        this.cost = cost;
        this.seller_name=seller_name;
    }

    public productbillsmodel() {
    }
}
