package com.example.dialybook;

public class createstock {
    public createstock(String fieldname, String itemname, String itemcost, String itemstock, String itemunits, String stockleft) {
        this.fieldname = fieldname;
        this.itemname = itemname;
        this.itemcost = itemcost;
        this.itemstock = itemstock;
        this.itemunits = itemunits;
        this.stockleft=stockleft;
    }

    public  String fieldname,itemname,itemcost,itemstock,itemunits,stockleft;
    public createstock() {
    }

    public String getStockleft() {
        return stockleft;
    }

    public void setStockleft(String stockleft) {
        this.stockleft = stockleft;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemcost() {
        return itemcost;
    }

    public void setItemcost(String itemcost) {
        this.itemcost = itemcost;
    }

    public String getItemstock() {
        return itemstock;
    }

    public void setItemstock(String itemstock) {
        this.itemstock = itemstock;
    }

    public String getItemunits() {
        return itemunits;
    }

    public void setItemunits(String itemunits) {
        this.itemunits = itemunits;
    }

    public createstock(String fieldname) {
        this.fieldname = fieldname;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }
}
