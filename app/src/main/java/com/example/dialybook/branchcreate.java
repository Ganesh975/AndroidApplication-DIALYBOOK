package com.example.dialybook;

public class branchcreate {
    private String branchname;
    private String branchtype;
    private String branchaddress;
    private String branchpartners;

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname;
    }

    public String getBranchtype() {
        return branchtype;
    }

    public void setBranchtype(String branchtype) {
        this.branchtype = branchtype;
    }

    public String getBranchaddress() {
        return branchaddress;
    }

    public void setBranchaddress(String branchaddress) {
        this.branchaddress = branchaddress;
    }

    public String getBranchpartners() {
        return branchpartners;
    }

    public void setBranchpartners(String branchpartners) {
        this.branchpartners = branchpartners;
    }

    public String getBranchmobile() {
        return branchmobile;
    }

    public void setBranchmobile(String branchmobile) {
        this.branchmobile = branchmobile;
    }

    public branchcreate() {
    }

    public branchcreate(String branchname, String branchtype, String branchaddress, String branchpartners, String branchmobile) {
        this.branchname = branchname;
        this.branchtype = branchtype;
        this.branchaddress = branchaddress;
        this.branchpartners = branchpartners;
        this.branchmobile = branchmobile;
    }

    private String branchmobile;
}
