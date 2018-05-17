package com.example.angel.sqawker.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;

import com.example.angel.sqawker.provider.SqawkContract;
import com.example.angel.sqawker.recycler.SqwakItem;

import org.joda.time.DateTime;

import java.util.List;

public class DataBaseUtils {


    private static Integer idColAutor = null;
    private static Integer idColDate = null;
    private static Integer idColMessage = null;
    private static Integer idKesyInstructor = null;
    private static Integer idkeyColumn = null;


    public static Integer getIdColAutor(Cursor cursor) {
        initializeColumnsIndexs(cursor);
        return idColAutor;
    }

    public static Integer getIdColDate(Cursor cursor) {
        initializeColumnsIndexs(cursor);
        return idColDate;
    }

    public static Integer getIdColMessage(Cursor cursor) {
        initializeColumnsIndexs(cursor);
        return idColMessage;
    }

    public static Integer getIdKesyInstructor(Cursor cursor) {
        initializeColumnsIndexs(cursor);
        return idKesyInstructor;
    }

    public static Integer getIdkeyColumn(Cursor cursor) {
        initializeColumnsIndexs(cursor);
        return idkeyColumn;
    }


    private static void initializeColumnsIndexs(Cursor cursor) {

        if (idkeyColumn == null) {
            idkeyColumn = cursor.getColumnIndex(SqawkContract.COLUMN_ID);
            idColAutor = cursor.getColumnIndex(SqawkContract.COLUMN_AUTOR);
            idColDate = cursor.getColumnIndex(SqawkContract.COLUMN_DATE);
            idColMessage = cursor.getColumnIndex(SqawkContract.COLUMN_MESSAGE);
            idKesyInstructor = cursor.getColumnIndex(SqawkContract.COLUMN_AUTOR_KEY);
        }
    }

    public static List<SqwakItem> getNewElements(Context conetxt, Cursor cursor, Cursor mCursor) {

        initializeColumnsIndexs(cursor);

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

        initializeColumnsIndexs(cursor);

        cursor.moveToPosition(position);

        String autorName = cursor.getString(idColAutor);
        String instructorKey = cursor.getString(idKesyInstructor);
        int dateTimeInt = cursor.getInt(idColDate);
        String message = cursor.getString(idColMessage);

        Bitmap bmp = InstructorsInfo.getInstructorImage(context, instructorKey);
        DateTime dateTime = new DateTime(dateTimeInt);
        return new SqwakItem(bmp, dateTime, autorName, message);
    }
}
