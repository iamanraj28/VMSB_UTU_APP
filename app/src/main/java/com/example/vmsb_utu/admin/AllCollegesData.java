package com.example.vmsb_utu.admin;

public class AllCollegesData {

    String collegeName, collegeEmail, collegePhone, purl;

    public AllCollegesData() {
    }

    public AllCollegesData(String collegeName, String collegeEmail, String collegePhone, String purl) {
        this.collegeName = collegeName;
        this.collegeEmail = collegeEmail;
        this.collegePhone = collegePhone;
        this.purl = purl;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public String getCollegeEmail() {
        return collegeEmail;
    }

    public String getCollegePhone() {
        return collegePhone;
    }

    public String getPurl() {
        return purl;
    }
}
