package com.app.creaseart.models;

/**
 * Created by hemanta on 29-07-2017.
 */

public class ModelCategory {

    private String Status;

    private String Quantity;

    public String getStickerCode() {
        return StickerCode;
    }

    public void setStickerCode(String stickerCode) {
        StickerCode = stickerCode;
    }

    private String StickerCode;

    public String getDeliveryTime() {
        return DeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        DeliveryTime = deliveryTime;
    }

    private String DeliveryTime;

    private String Time;

    public String getConfirmStatus() {
        return ConfirmStatus;
    }

    public void setConfirmStatus(String confirmStatus) {
        ConfirmStatus = confirmStatus;
    }

    private String ConfirmStatus;

    public String getZone() {
        return Zone;
    }

    public void setZone(String zone) {
        Zone = zone;
    }

    private String Zone;

    private String Date;

    private String OrderNo;

    private String Address;

    private String OrderId;

    private String userImage;

    private String userMobile,userId;

    private String userName;

    private String userEmail;

    public String getStatus ()
    {
        return Status;
    }

    public void setStatus (String Status)
    {
        this.Status = Status;
    }

    public String getQuantity ()
    {
        return Quantity;
    }

    public void setQuantity (String Quantity)
    {
        this.Quantity = Quantity;
    }

    public String getTime ()
    {
        return Time;
    }

    public void setTime (String Time)
    {
        this.Time = Time;
    }

    public String getDate ()
    {
        return Date;
    }

    public void setDate (String Date)
    {
        this.Date = Date;
    }

    public String getOrderNo ()
    {
        return OrderNo;
    }

    public void setOrderNo (String OrderNo)
    {
        this.OrderNo = OrderNo;
    }

    public String getAddress ()
    {
        return Address;
    }

    public void setAddress (String Address)
    {
        this.Address = Address;
    }

    public String getOrderId ()
    {
        return OrderId;
    }

    public void setOrderId (String OrderId)
    {
        this.OrderId = OrderId;
    }

    public String getUserImage ()
    {
        return userImage;
    }

    public void setUserImage (String userImage)
    {
        this.userImage = userImage;
    }

    public String getUserMobile ()
    {
        return userMobile;
    }

    public void setUserMobile (String userMobile)
    {
        this.userMobile = userMobile;
    }

    public String getUserName ()
    {
        return userName;
    }

    public void setUserName (String userName)
    {
        this.userName = userName;
    }

    public String getUserEmail ()
    {
        return userEmail;
    }

    public void setUserEmail (String userEmail)
    {
        this.userEmail = userEmail;
    }


    public int getRowType() {
        return rowType;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 0;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
