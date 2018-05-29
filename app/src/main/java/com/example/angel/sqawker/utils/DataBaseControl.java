package com.example.angel.sqawker.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.angel.sqawker.provider.InstructorsContract;
import com.example.angel.sqawker.provider.SqawkContract;
import com.example.angel.sqawker.provider.SqawkProvider;
import com.example.angel.sqawker.recycler.SqwakItem;

import org.joda.time.DateTime;

import java.util.List;

public class DataBaseControl {


    private static Integer idColAutor = null;
    private static Integer idColDate = null;
    private static Integer idColMessage = null;
    private static Integer idKesyInstructor = null;
    private static Integer idkeyColumn = null;


    private static void initializeMessagesColumnsIndexs(Cursor cursor) {

        if (idkeyColumn == null) {
            idkeyColumn = cursor.getColumnIndex(SqawkContract.COLUMN_ID);
            idColAutor = cursor.getColumnIndex(SqawkContract.COLUMN_AUTOR);
            idColDate = cursor.getColumnIndex(SqawkContract.COLUMN_DATE);
            idColMessage = cursor.getColumnIndex(SqawkContract.COLUMN_MESSAGE);
            idKesyInstructor = cursor.getColumnIndex(SqawkContract.COLUMN_AUTOR_KEY);
        }
    }

    private static Integer idAutor_key_name;
    private static Integer idAutor_name;
    private static Integer idAutor_image;
    private static Integer idFollowing;


    private static void initializeInstructorsColumnsIndexs(Cursor cursor) {

        if (idAutor_key_name == null) {
            idAutor_key_name = cursor.getColumnIndex(InstructorsContract.COLUMN_AUTHOR_KEY);
            idAutor_name = cursor.getColumnIndex(InstructorsContract.COLUMN_AUTHOR_NAME);
            idAutor_image = cursor.getColumnIndex(InstructorsContract.COLUMN_AUTHOR_IMAGE);
            idFollowing = cursor.getColumnIndex(InstructorsContract.COLUMN_FOLLOWING);
        }

    }

    public static List<SqwakItem> getNewElements(Context conetxt, Cursor cursor, Cursor mCursor) {

        initializeMessagesColumnsIndexs(cursor);

        List<SqwakItem> squaItemList = null;
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount() - 1; i++) {

            Boolean equals = false;
            cursor.moveToPosition(0);
            int idNew = cursor.getInt(idkeyColumn);

            for (int j = 0; i < mCursor.getCount() - 1; i++) {
                mCursor.moveToPosition(j);
                int idOld = mCursor.getInt(idkeyColumn);
                if (idNew == idOld) {
                    equals = true;
                    break;
                }
            }

            if (!equals) squaItemList.add(convert2SqwakItem(conetxt, cursor, i));
        }

        return squaItemList;
    }


    public static SqwakItem convert2SqwakItem(Context context, Cursor cursor, int position) {

        initializeMessagesColumnsIndexs(cursor);

        cursor.moveToPosition(position);

        String instructorKey = cursor.getString(idKesyInstructor);
        long dateTimeInt = cursor.getLong(idColDate);
        String message = cursor.getString(idColMessage);

        DateTime dateTime = new DateTime(dateTimeInt);
        return new SqwakItem(context, dateTime, instructorKey, message);
    }


    public static Instructor convert2InstructorItem(Context context, Cursor cursor, int position) {

        initializeInstructorsColumnsIndexs(cursor);
        cursor.moveToPosition(position);

        String instructorKey = cursor.getString(idAutor_key_name);
        String imageName = cursor.getString(idAutor_image);
        String autorName = cursor.getString(idAutor_name);

        Boolean following = cursor.getInt(idFollowing) == 1;

        return new Instructor(context, instructorKey, autorName, imageName, following);
    }


    public static void followUnfollowInstructor(Context context, String instructorKey, Boolean follow) {
        ContentValues values = new ContentValues();
        int value = follow ? 1 : 0;
        values.put(InstructorsContract.COLUMN_FOLLOWING, value);
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.update(SqawkProvider.Instructors.CONTENT_URI, values, InstructorsContract.COLUMN_AUTHOR_KEY + "='" + instructorKey + "' ", null);
    }

    public static void storeMessage(Context context, String authorKey, String authorName, DateTime date, String message) {

        ContentValues values = new ContentValues();


        values.put(SqawkContract.COLUMN_AUTOR_KEY, authorKey);
        values.put(SqawkContract.COLUMN_AUTOR, authorName);
        values.put(SqawkContract.COLUMN_DATE, date.getMillis());
        values.put(SqawkContract.COLUMN_MESSAGE, message);

        context.getContentResolver().insert(SqawkProvider.SqawkMessages.CONTENT_URI_FOLLOWED, values);
    }
}
