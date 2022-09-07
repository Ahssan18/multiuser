package com.food.multiuser.Model;

import java.util.List;

public class CartItem {
    private String cartId;
    private String totalPrice;
    private List<Product> list;

    public CartItem(String cartId, String totalPrice, List<Product> list) {
        this.cartId = cartId;
        this.totalPrice = totalPrice;
        this.list = list;
    }

    public CartItem() {
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartId='" + cartId + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", list=" + list +
                '}';
    }
}