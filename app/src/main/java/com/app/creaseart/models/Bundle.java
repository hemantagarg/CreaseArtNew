package com.app.creaseart.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hemanta on 30-09-2017.
 */

public class Bundle {

    private String bundleUnit;
    private String remainingUnit = "";
    private Item[] item;

    private String bundlePrice;
    private int totalAddedItemprice = 0;

    @SerializedName("priceLeft")
    private int intPriceLeft ;

    private String bundleId;
    private int seletedPosition = -1;

    private String bundleName;

    private String bundleColor;

    public String getBundleUnit() {
        return bundleUnit;
    }

    public void setBundleUnit(String bundleUnit) {
        this.bundleUnit = bundleUnit;
    }

    public Item[] getItem() {
        return item;
    }

    public void setItem(Item[] item) {
        this.item = item;
    }

    public String getBundlePrice() {
        return bundlePrice;
    }

    public void setBundlePrice(String bundlePrice) {
        this.bundlePrice = bundlePrice;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getBundleColor() {
        return bundleColor;
    }

    public void setBundleColor(String bundleColor) {
        this.bundleColor = bundleColor;
    }

    @Override
    public String toString() {
        return "ClassPojo [bundleUnit = " + bundleUnit + ", item = " + item + ", bundlePrice = " + bundlePrice + ", bundleId = " + bundleId + ", bundleName = " + bundleName + ", bundleColor = " + bundleColor + "]";
    }

    public String getRemainingUnit() {
        return remainingUnit;
    }

    public void setRemainingUnit(String remainingUnit) {
        this.remainingUnit = remainingUnit;
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

    public int getTotalAddedItemprice() {
        return totalAddedItemprice;
    }

    public void setTotalAddedItemprice(int totalAddedItemprice) {
        this.totalAddedItemprice = totalAddedItemprice;
    }

    public int getIntPriceLeft() {
        return intPriceLeft;
    }

    public void setIntPriceLeft(int intPriceLeft) {
        this.intPriceLeft = intPriceLeft;
    }
}
