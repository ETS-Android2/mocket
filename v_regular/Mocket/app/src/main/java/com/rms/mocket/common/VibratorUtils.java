package com.rms.mocket.common;

import android.content.Context;
import android.os.Vibrator;

import static android.content.Context.VIBRATOR_SERVICE;

public class VibratorUtils {

    public static void vibrateCorrect(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        try {
            vibrator.vibrate(50);
            Thread.sleep(150);
            vibrator.vibrate(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void vibrateIncorrect(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    public static void vibrateAlert(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }
}
