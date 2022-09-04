package com.food.multiuser.Model;

public class Order {
    boolean acceptStatus, deliverStatus;
    String userId, name, price, description, orderId, productId;

    public Order(boolean acceptStatus, boolean deliverStatus, String userId, String name, String price, String description) {
        this.acceptStatus = acceptStatus;
        this.deliverStatus = deliverStatus;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "acceptStatus=" + acceptStatus +
                ", deliverStatus=" + deliverStatus +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
