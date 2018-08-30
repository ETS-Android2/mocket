package com.rms.mocket.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.util.Random;


public class Utils {

    public static final String TAG = "Mocket";

    /* Resize ListView dynamically */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        final int UNBOUNDED = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);


        int grossElementHeight = 0;
        for (int i = 0; i < listView.getCount(); i++) {
            View childView = listAdapter.getView(i, null, listView);
            childView.measure(UNBOUNDED, UNBOUNDED);
            grossElementHeight += childView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = grossElementHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



    /* Generates six digits verification code. */
    public static int generateVerificationCode() {
        Random rnd = new Random();
        return 100000 + rnd.nextInt(900000);
    }

    public static void sendEmail(String email, String message) {

    }

    public static void log(String message){
        Log.d(TAG, message);
    }


    public static String getEncodedImage(ImageView imageView){

        BitmapDrawable draw = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = draw.getBitmap();
        bitmap = getResizedBitmap(bitmap, 1000);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);

        return encodedImage;

    }

    public static void setEncodedImage(ImageView view, String encoded_image){
        byte[] decodedString = Base64.decode(encoded_image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        view.setImageBitmap(decodedByte);
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
