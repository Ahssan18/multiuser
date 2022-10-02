package com.food.multiuser.Model;

import java.util.List;

public class Order {
    boolean acceptStatus, deliverStatus;
    String userId, TotalPrice, orderId;
    List<Product> productList;

    public Order(boolean acceptStatus, boolean deliverStatus, String userId, String orderId, List<Product> productList, long timeStamp) {
        this.acceptStatus = acceptStatus;
        this.deliverStatus = deliverStatus;
        this.userId = userId;
        this.orderId = orderId;
        this.productList = productList;
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    long timeStamp = -1;

    public Order(boolean acceptStatus, boolean deliverStatus, String userId, String price, List<Product> productList) {
        this.acceptStatus = acceptStatus;
        this.deliverStatus = deliverStatus;
        this.userId = userId;
        this.TotalPrice = price;
        this.productList = productList;
    }

    public Order() {
    }

    public boolean isAcceptStatus() {
        return acceptStatus;
    }

    public void setAcceptStatus(boolean acceptStatus) {
        this.acceptStatus = acceptStatus;
    }

    public boolean isDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(boolean deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.TotalPrice = totalPrice;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "acceptStatus=" + acceptStatus +
                ", deliverStatus=" + deliverStatus +
                ", userId='" + userId + '\'' +
                ", price='" + TotalPrice + '\'' +
                ", orderId='" + orderId + '\'' +
                '}';
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
