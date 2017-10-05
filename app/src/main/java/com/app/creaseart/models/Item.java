package com.app.creaseart.models;

/**
 * Created by hemanta on 30-09-2017.
 */

public class Item {
    private String itemAttribute;

    private String itemName;

    private String itemPrice;
    private int itemAdded = 0;
    private int totalAddedItemPrice = 0;

    private String itemId;

    public String getItemAttribute() {
        return itemAttribute;
    }

    public void setItemAttribute(String itemAttribute) {
        this.itemAttribute = itemAttribute;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String toString() {
        return "ClassPojo [itemAttribute = " + itemAttribute + ", itemName = " + itemName + ", itemPrice = " + itemPrice + ", itemId = " + itemId + "]";
    }

    public int getItemAdded() {
        return itemAdded;
    }

    public void setItemAdded(int itemAdded) {
        this.itemAdded = itemAdded;
    }

    public void setRowType(int rowType) {
        this.rowType = rowType;
    }

    private int rowType = 1;

    public int getRowType() {
        return rowType;
    }

    public int getTotalAddedItemPrice() {
        return totalAddedItemPrice;
    }

    public void setTotalAddedItemPrice(int totalAddedItemPrice) {
        this.totalAddedItemPrice = totalAddedItemPrice;
    }
}
