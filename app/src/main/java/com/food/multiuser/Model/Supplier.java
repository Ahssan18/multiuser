package com.food.multiuser.Model;

public class Supplier {
    private String Name;
    private String Image;
    private String Email;
    private String PhoneNumber;

    public Supplier(){
    }

    public Supplier(String name, String image, String email, String phoneNumber) {
        Name = name;
        Image = image;
        Email = email;
        PhoneNumber = phoneNumber;
    }

    public String getName() { return Name;}

    public void setName(String name) { Name = name;}

    public String getImage() { return Image;}

    public void setImage(String image) { Image = image;}

    public String getEmail() { return Email;}

    public void setEmail(String email) { Email = email;}

    public String getPhoneNumber() { return PhoneNumber;}

    public void setPhoneNumber(String phoneNumber) { PhoneNumber = phoneNumber;}
}
