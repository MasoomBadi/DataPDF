package com.phoenix.datapdf.modals;

public class Agri {

    String name;
    String regNo;
    String sonOf;
    String dob;
    String phone;
    String aadharNo;
    String block;
    String village;
    String panchayat;
    String district;

    public Agri(String name, String regNo, String sonOf, String dob, String phone, String aadharNo, String block, String village, String panchayat, String district) {
        this.name = name;
        this.regNo = regNo;
        this.sonOf = sonOf;
        this.dob = dob;
        this.phone = phone;
        this.aadharNo = aadharNo;
        this.block = block;
        this.village = village;
        this.panchayat = panchayat;
        this.district = district;
    }

    public Agri() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getSonOf() {
        return sonOf;
    }

    public void setSonOf(String sonOf) {
        this.sonOf = sonOf;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getPanchayat() {
        return panchayat;
    }

    public void setPanchayat(String panchayat) {
        this.panchayat = panchayat;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
