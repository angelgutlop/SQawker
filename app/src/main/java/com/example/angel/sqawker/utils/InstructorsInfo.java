package com.example.angel.sqawker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.angel.sqawker.R;

public class InstructorsInfo {

    // Topic keys as matching what is found in the database
    public static final String ASSER_KEY = "key_asser";
    public static final String CEZANNE_KEY = "key_cezanne";
    public static final String JLIN_KEY = "key_jlin";
    public static final String LYLA_KEY = "key_lyla";
    public static final String NIKITA_KEY = "key_nikita";
    public static final String TEST_ACCOUNT_KEY = "key_test";


    public static final String[] INSTRUCTOR_KEYS = {
            ASSER_KEY, CEZANNE_KEY, JLIN_KEY, LYLA_KEY, NIKITA_KEY
    };

    public static Bitmap getInstructorImage(Context context, String key) {

        int idBmp;
        switch (key) {
            case ASSER_KEY:
                idBmp = R.drawable.asser;
                break;
            case CEZANNE_KEY:
                idBmp = R.drawable.cezanne;
                break;
            case JLIN_KEY:
                idBmp = R.drawable.jlin;
                break;
            case LYLA_KEY:
                idBmp = R.drawable.lyla;
                break;
            case NIKITA_KEY:
                idBmp = R.drawable.nikita;
                break;
            default:
                idBmp = R.drawable.test;
        }

        return BitmapFactory.decodeResource(context.getResources(), idBmp);

    }
}
