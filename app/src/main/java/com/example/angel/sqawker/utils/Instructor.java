package com.example.angel.sqawker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.angel.sqawker.R;

import java.lang.reflect.Field;

public class Instructor {

    public String autor_key;
    public String autor_name;
    public String autor_image_name;
    public Bitmap autor_bmp_image;
    public boolean following;
    private Context context;

    public Instructor(Context context, String autor_key, String autor_name, String autor_image_name, boolean following) {
        this.context = context;
        this.autor_key = autor_key;
        this.autor_name = autor_name;
        this.autor_image_name = autor_image_name;
        this.autor_bmp_image = getInstructorImage(context);
        this.following = following;
    }

    private Bitmap getInstructorImage(Context context) {

        Class res = R.drawable.class;

        try {
            Field field = res.getField(autor_image_name);
            int drawableId = field.getInt(null);
            return BitmapFactory.decodeResource(context.getResources(), drawableId);

        } catch (NoSuchFieldException e) {
          
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;

    }
}
