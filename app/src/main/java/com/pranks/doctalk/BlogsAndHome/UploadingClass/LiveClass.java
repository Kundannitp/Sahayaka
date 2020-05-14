package com.pranks.doctalk.BlogsAndHome.UploadingClass;

public class LiveClass {
    int totalcases,totaldeath,totalrecovered,activecases,seriouscri;
    String countryname;

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public LiveClass(String countryname, int totalcases, int totaldeath, int totalrecovered, int activecases, int seriouscri) {
        this.totalcases = totalcases;
        this.totaldeath = totaldeath;
        this.totalrecovered = totalrecovered;
        this.activecases = activecases;
        this.seriouscri = seriouscri;
        this.countryname=countryname;
    }

    public int getTotalcases() {
        return totalcases;
    }

    public void setTotalcases(int totalcases) {
        this.totalcases = totalcases;
    }

    public int getTotaldeath() {
        return totaldeath;
    }

    public void setTotaldeath(int totaldeath) {
        this.totaldeath = totaldeath;
    }

    public int getTotalrecovered() {
        return totalrecovered;
    }

    public void setTotalrecovered(int totalrecovered) {
        this.totalrecovered = totalrecovered;
    }

    public int getActivecases() {
        return activecases;
    }

    public void setActivecases(int activecases) {
        this.activecases = activecases;
    }

    public int getSeriouscri() {
        return seriouscri;
    }

    public void setSeriouscri(int seriouscri) {
        this.seriouscri = seriouscri;
    }
}
