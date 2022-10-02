package com.food.multiuser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.food.multiuser.Model.User;
import com.google.gson.Gson;

public class Helper {
    Context context;
    Gson gson;

    public Helper(Context context) {
        this.context = context;
        gson = new Gson();
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    public void saveUser(User user) {

        String userData = gson.toJson(user);
        getSharedPreferences().edit().putString("user", userData).apply();
    }

    public void logout() {
        getSharedPreferences().edit().clear().apply();
    }

    public User getUser() {
        String user = getSharedPreferences().getString("user", "");
        if (!user.equals("")) {
            return gson.fromJson(user, User.class);
        } else {
            return null;
        }
    }
    @SuppressLint("SimpleDateFormat")
    public String getDate(long timeStamp)
    {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").
                format(new java.util.Date(timeStamp));
    }
}
