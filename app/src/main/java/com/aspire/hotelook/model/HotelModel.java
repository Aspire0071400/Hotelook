package com.aspire.hotelook.model;

import java.util.ArrayList;

public class HotelModel {

    String hotelId;
    String hotelName;
    String hotelDescription;
    String hotelImage;
    String hotelAddress;
    String hotelPrice;
    ArrayList<String> hotelBookedClientId;

    public HotelModel() {
    }

//    public HotelModel(String hotelName, String hotelDescription, String hotelImage,String hotelAddress,String hotelPrice) {
//        this.hotelName = hotelName;
//        this.hotelDescription = hotelDescription;
//        this.hotelImage = hotelImage;
//        this.hotelAddress = hotelAddress;
//        this.hotelPrice = hotelPrice;
//    }// this constructor needs to be used when new ui fields are made for address and price.

    public HotelModel(String hotelId, String hotelName, String hotelDescription, String hotelImage) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
    }// this constructor need to be disposed-off when new ui fields are made for address and price.

    public HotelModel(String hotelId, String hotelName, String hotelDescription, String hotelImage, ArrayList<String> hotelBookedClientId) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
        this.hotelBookedClientId = hotelBookedClientId;
    }//this constructor is used when client booked hotel or fetches above data.

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelDescription() {
        return hotelDescription;
    }

    public void setHotelDescription(String hotelDescription) {
        this.hotelDescription = hotelDescription;
    }

    public String getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(String hotelImage) {
        this.hotelImage = hotelImage;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(String hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public ArrayList<String> getHotelBookedClientId() {
        return hotelBookedClientId;
    }

    public void setHotelBookedClientId(ArrayList<String> hotelBookedClientId) {
        this.hotelBookedClientId = hotelBookedClientId;
    }
}
