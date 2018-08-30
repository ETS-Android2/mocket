package com.rms.mocket.object;

public class User {

    public static final String REFERENCE_USERS = "users";

    public String profile_image;
    public String first_name;
    public String last_name;

    public String setting_notification;
    public String setting_game;
    public String setting_gesture;
    public String setting_vibration;

    public User(){

    }

    public User(String first_name, String last_name, String profile_image){

        this.first_name = first_name;
        this.last_name = last_name;
        this.setting_notification = "None";
        this.setting_game = "0";
        this.setting_gesture = "None";
        this.setting_vibration = "ON";
        this.profile_image = profile_image;
    }


}

