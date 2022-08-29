package com.food.multiuser.Model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("barcode")
    private String barcode;
    @SerializedName("discription")

    private String discription;
    @SerializedName("name")

    private String name;
    @SerializedName("price")

    private String price;
    @SerializedName("productId")

    private String productId;
    @SerializedName("quantity")

    private String quantity;

    public CartItem() {
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "barcode='" + barcode + '\'' +
                ", discription='" + discription + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity='" + quantity + '\'' +
                '}';
    }
}