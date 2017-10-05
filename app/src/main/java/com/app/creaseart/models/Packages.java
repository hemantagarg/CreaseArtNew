package com.app.creaseart.models;


/**
 * Created by hemanta on 30-09-2017.
 */

public class Packages {
    private String discountType;

    private String packagePrice;

    private String packageName;
    private int seletedPosition = -1;
    private String isDiscount;

    private Bundle[] bundle;

    private String packageId;

    private String discountPrice;

    private String discount;


    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(String isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Bundle[] getBundle() {
        return bundle;
    }

    public void setBundle(Bundle[] bundle) {
        this.bundle = bundle;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "ClassPojo [discountType = " + discountType + ", packagePrice = " + packagePrice + ", packageName = " + packageName + ", isDiscount = " + isDiscount + ", bundle = " + bundle + ", packageId = " + packageId + ", discountPrice = " + discountPrice + ", discount = " + discount + "]";
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 1;

    public int getRowType() {
        return rowType;
    }

    public int getSeletedPosition() {
        return seletedPosition;
    }

    public void setSeletedPosition(int seletedPosition) {
        this.seletedPosition = seletedPosition;
    }
}
