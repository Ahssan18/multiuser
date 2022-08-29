package com.food.multiuser.Model;

public class FeedBack {
    String message;
    Float rating;

    public FeedBack(String message, Float rating) {
        this.message = message;
        this.rating = rating;
    }

    public FeedBack() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
