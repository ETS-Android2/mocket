package com.rms.mocket;

import java.util.Random;

public class Utils {

    /* Generates six digits verification code. */
    public static int generateVerificationCode(){
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

    public static void sendEmail(String email, String message){

    }

}
