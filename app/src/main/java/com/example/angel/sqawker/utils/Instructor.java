package com.example.angel.sqawker.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.angel.sqawker.R;
import com.google.firebase.messaging.FirebaseMessaging;

import java.lang.reflect.Field;

public class Instructor {

    public String autor_key;
    public String autor_name;
    public String autor_image_name;
    public Bitmap autor_bmp_image;


    private boolean following;
    private Context context;

    public Instructor(Context context, String autor_key, String autor_name, String autor_image_name, boolean following) {
        this.context = context;
        this.autor_key = autor_key;
        this.autor_name = autor_name;
        this.autor_image_name = autor_image_name;
        this.autor_bmp_image = getInstructorImage(context);
        this.following = following;

        subcribe2Instructor(this.autor_name, this.following);
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

    public void setFollowing(boolean following) {
        this.following = following;
        subcribe2Instructor(this.autor_name, this.following);
    }

    public Boolean getFollowing() {
        return this.following;
    }

    public void subcribe2Instructor(Boolean follow) {

        subcribe2Instructor(this.autor_name, follow);
    }

    public static void subcribe2Instructor(String nameInstructor, Boolean follow) {

        if (follow) {
            FirebaseMessaging.getInstance().subscribeToTopic(nameInstructor);
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(nameInstructor);
        }
    }
}
