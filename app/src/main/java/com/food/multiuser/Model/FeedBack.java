package com.food.multiuser.Model;

public class FeedBack {
    String message;
    String userName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;
    Float rating;


    public FeedBack() {
    }

    public FeedBack(String message, String userName, String userId, Float rating) {
        this.message = message;
        this.userName = userName;
        this.userId = userId;
        this.rating = rating;
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

    @Override
    public String toString() {
        return "FeedBack{" +
                "message='" + message + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", rating=" + rating +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
