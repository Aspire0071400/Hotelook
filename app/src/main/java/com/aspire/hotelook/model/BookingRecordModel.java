package com.aspire.hotelook.model;

public class BookingRecordModel {

    String bookingId, bookingStatus, clientId;

    public BookingRecordModel(String bookingId, String bookingStatus, String clientId) {
        this.bookingId = bookingId;
        this.bookingStatus = bookingStatus;
        this.clientId = clientId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
