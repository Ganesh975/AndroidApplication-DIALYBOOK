package com.example.dialybook;

public class productmodel {
    public productmodel() {
    }
    String product_name,product_quantity,product_cost,seller_name;

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public productmodel(String product_name, String product_quantity, String product_cost) {
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_cost = product_cost;

    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }
}
