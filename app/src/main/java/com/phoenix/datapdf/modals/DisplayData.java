package com.phoenix.datapdf.modals;

public class DisplayData {

    String name;
    String regNo;
    String id;

    public DisplayData(String name, String regNo, String id) {
        this.name = name;
        this.regNo = regNo;
        this.id = id;
    }

    public DisplayData() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
