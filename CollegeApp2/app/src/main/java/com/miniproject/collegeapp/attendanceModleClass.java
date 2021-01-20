package com.miniproject.collegeapp;

public class attendanceModleClass {
    String Date,name,cinTime,coutTime,cinLocation,coutLocation,whrs,inlongitude,inlatitude,outlongitude,outlatitude;

    public attendanceModleClass(String date, String name, String cinTime, String coutTime, String cinLocation, String coutLocation,
                                String whrs,String inlongitude,String inlatitude,
                                String outlongitude,String outlatitude) {
        this.Date = date;
        this.name = name;
        this.cinTime = cinTime;
        this.coutTime = coutTime;
        this.cinLocation = cinLocation;
        this.coutLocation = coutLocation;
        this.whrs=whrs;
        this.inlongitude=inlongitude;
        this.inlatitude=inlatitude;
        this.outlongitude=outlongitude;
        this.outlatitude=outlatitude;
    }


    public String getInlongitude() {
        return inlongitude;
    }

    public void setInlongitude(String inlongitude) {
        this.inlongitude = inlongitude;
    }

    public String getInlatitude() {
        return inlatitude;
    }

    public void setInlatitude(String inlatitude) {
        this.inlatitude = inlatitude;
    }

    public String getOutlongitude() {
        return outlongitude;
    }

    public void setOutlongitude(String outlongitude) {
        this.outlongitude = outlongitude;
    }

    public String getOutlatitude() {
        return outlatitude;
    }

    public void setOutlatitude(String outlatitude) {
        this.outlatitude = outlatitude;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCinTime() {
        return cinTime;
    }

    public void setCinTime(String cinTime) {
        this.cinTime = cinTime;
    }

    public String getCoutTime() {
        return coutTime;
    }

    public void setCoutTime(String coutTime) {
        this.coutTime = coutTime;
    }

    public String getCinLocation() {
        return cinLocation;
    }

    public void setCinLocation(String cinLocation) {
        this.cinLocation = cinLocation;
    }

    public String getCoutLocation() {
        return coutLocation;
    }

    public void setCoutLocation(String coutLocation) {
        this.coutLocation = coutLocation;
    }

    public String getWhrs() {
        return whrs;
    }

    public void setWhrs(String whrs) {
        this.whrs = whrs;
    }
}
