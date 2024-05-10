package com.aspire.hotelook.model;

import java.util.ArrayList;

public class HotelModel {

    String hotelId;
    String hotelName;
    String hotelDescription;
    String hotelImage;
    ArrayList<String> hotelBookedClientId;

    public HotelModel() {
    }

    public HotelModel(String hotelId, String hotelName, String hotelDescription, String hotelImage) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
    }

    public HotelModel(String hotelId, String hotelName, String hotelDescription, String hotelImage, ArrayList<String> hotelBookedClientId) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.hotelDescription = hotelDescription;
        this.hotelImage = hotelImage;
        this.hotelBookedClientId = hotelBookedClientId;
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

    public ArrayList<String> getHotelBookedClientId() {
        return hotelBookedClientId;
    }

    public void setHotelBookedClientId(ArrayList<String> hotelBookedClientId) {
        this.hotelBookedClientId = hotelBookedClientId;
    }
}
