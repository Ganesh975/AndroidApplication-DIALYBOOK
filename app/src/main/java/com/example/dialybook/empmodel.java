package com.example.dialybook;

public class empmodel {
    String empname,empid,empaddress,empgender,empmobile,emppassword,empbranch;


    public empmodel() {
    }

    public empmodel(String empname, String empid, String empaddress, String empgender, String empmobile, String emppassword, String empbranch) {
        this.empname = empname;
        this.empid = empid;

        this.empgender = empgender;
        this.empaddress = empaddress;
        this.empmobile = empmobile;
        this.emppassword = emppassword;
        this.empbranch = empbranch;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpaddress() {
        return empaddress;
    }

    public void setEmpaddress(String empaddress) {
        this.empaddress = empaddress;
    }

    public String getEmpgender() {
        return empgender;
    }

    public void setEmpgender(String empgender) {
        this.empgender = empgender;
    }

    public String getEmpmobile() {
        return empmobile;
    }

    public void setEmpmobile(String empmobile) {
        this.empmobile = empmobile;
    }

    public String getEmppassword() {
        return emppassword;
    }

    public void setEmppassword(String emppassword) {
        this.emppassword = emppassword;
    }

    public String getEmpbranch() {
        return empbranch;
    }

    public void setEmpbranch(String empbranch) {
        this.empbranch = empbranch;
    }
}
