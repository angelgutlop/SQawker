package com.example.angel.sqawker.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class InstructorsInfo {


    // Topic keys as matching what is found in the database
    public static final String ASSER_KEY = "key_asser";
    public static final String CEZANNE_KEY = "key_cezanne";
    public static final String JLIN_KEY = "key_jlin";
    public static final String LYLA_KEY = "key_lyla";
    public static final String NIKITA_KEY = "key_nikita";
    public static final String TEST_ACCOUNT_KEY = "key_test";

    static Map<String, Instructor> instructorsMap = new HashMap<String, Instructor>();


    static Map<String, Instructor> treeMap = new TreeMap<String, Instructor>(new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            String n1 = instructorsMap.get(o1).autor_name;
            String n2 = instructorsMap.get(o2).autor_name;
            return n1.compareTo(n2);
        }
    }
    );

    /* For Java 8, try this lambda
    Map<Integer, String> treeMap = new TreeMap<>(
                    (Comparator<Integer>) (o1, o2) -> o2.compareTo(o1)
            );
    */
    public static Map<String, Instructor> getInstructorsAvailable() {

        treeMap.putAll(instructorsMap);

        return treeMap;
    }

    public static void setInfo(Context context, Cursor cursor) {
        instructorsMap.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            Instructor instructor = DataBaseUtils.convert2InstructorItem(context, cursor, i);
            instructorsMap.put(instructor.autor_key, instructor);
        }
    }


    public static String getInstructorName(String key) {

        Instructor instructor = instructorsMap.get(key);
        return instructor.autor_name;
    }


    public static Bitmap getInstructorImage(String key) {

        Instructor instructor = instructorsMap.get(key);
        return instructor.autor_bmp_image;

    }


}
