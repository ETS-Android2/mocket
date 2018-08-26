package com.rms.mocket.common;

import android.text.TextUtils;

public class Checker {

    /* Checks Email validation */
    public static boolean checkEmailValidation(String emailAddress) {
        if (TextUtils.isEmpty(emailAddress)) return false;
        else return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches();
    }

    /* Check if the password is weak */
    public static boolean checkWeakPassword(String password) {
        /* At least one Upper case and at least 8 digits. */
        String regex = "^(?=.*[A-Z]).{8}$";
        return !password.matches(regex);
    }

    /* Check if the string is number */
    public static boolean checkIfNumber(String number) {
        return number.matches("-?\\d+(\\.\\d+)?");
    }

    /* Check if the name is correct */
    public static boolean checkName(String name) {
        return name.matches("-?[a-zA-Z ]*");
    }

}
