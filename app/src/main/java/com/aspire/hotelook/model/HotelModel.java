package com.aspire.hotelook.model;

import java.util.ArrayList;

public class HotelModel {

    String hotelId;
    String hotelName;
    String hotelDescription;
    String hotelImage;
    String hotelAddress;
    String hotelPrice;
    String hotelVendorId;
    ArrayList<String> hotelBookedClientId;

    public HotelModel() {
    }

    public HotelModel(String hotelId, String hotelName, String hotelDescription, String hotelImage, String hotelAddress, String hotelPrice, String hotelVendorId) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
        this.hotelAddress = hotelAddress;
        this.hotelPrice = hotelPrice;
        this.hotelVendorId = hotelVendorId;
    }


    public HotelModel(String hotelId, String hotelName, String hotelDescription, String hotelImage, String hotelAddress, String hotelPrice, String hotelVendorId, ArrayList<String> hotelBookedClientId) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
        this.hotelAddress = hotelAddress;
        this.hotelPrice = hotelPrice;
        this.hotelVendorId = hotelVendorId;
        this.hotelBookedClientId = hotelBookedClientId;
    }//this constructor is used when client booked hotel or fetches above data.

    public String getHotelVendorId() {
        return hotelVendorId;
    }

    public void setHotelVendorId(String hotelVendorId) {
        this.hotelVendorId = hotelVendorId;
    }

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
