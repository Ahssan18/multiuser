package com.food.multiuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    private String productId, Discription, Name, Image, MenuId, Barcode;
    private Integer Quantity = 0, Price = 0;
    private boolean feedback = false;

    public Product() {
    }

    public Product(String productId, String discription, String name, String image, int price, int quantity, String menuId, String barcode) {
        Name = name;
        Image = image;
        Discription = discription;
        Price = price;
        Quantity = quantity;
        MenuId = menuId;
        Barcode = barcode;
    }


    protected Product(Parcel in) {
        productId = in.readString();
        Discription = in.readString();
        Name = in.readString();
        Image = in.readString();
        Price = in.readInt();
        Quantity = in.readInt();
        MenuId = in.readString();
        Barcode = in.readString();
        feedback = in.readByte() != 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public Integer getQuantity() {
        return Quantity;
    }

    public void setQuantity(Integer quantity) {
        Quantity = quantity;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    //debug product values
    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                "Discription='" + Discription + '\'' +
                ", Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                ", Price='" + Price + '\'' +
                ", Quantity='" + Quantity + '\'' +
                ", MenuId='" + MenuId + '\'' +
                ", Barcode='" + Barcode + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(Discription);
        parcel.writeString(Name);
        parcel.writeString(Image);
        parcel.writeInt(Price);
        parcel.writeInt(Quantity);
        parcel.writeString(MenuId);
        parcel.writeString(Barcode);
        parcel.writeByte((byte) (feedback ? 1 : 0));
    }

    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }
}
