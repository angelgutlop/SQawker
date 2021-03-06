package com.example.angel.sqawker.recycler;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.angel.sqawker.utils.DataBaseControl;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class SqwakAdapter extends FlexibleAdapter<SqwakItem> {

    private Cursor mCursor;
    private Context context;


    public SqwakAdapter(Context context) {
        super(null);
        this.context = context;
    }


    public SqwakAdapter(Context context, @Nullable List<SqwakItem> items) {
        super(items);
        this.context = context;
    }

    public SqwakAdapter(Context context, Cursor cursor) {

        this(context, cursor2List(context, cursor));

        this.mCursor = cursor;
        this.context = context;
    }


    public void updateDataSet(Cursor cursor) {
        this.mCursor = cursor;
        List<SqwakItem> list = cursor2List(context, cursor);
        updateDataSet(list);

    }

    public void addElements(Cursor cursor) {

        List<SqwakItem> listaNew = DataBaseControl.getNewElements(context, cursor, mCursor);
        updateDataSet(listaNew, true);
    }


    private static List<SqwakItem> cursor2List(Context context, Cursor cursor) {

        if (cursor == null) return null;

        List<SqwakItem> lista = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {

            lista.add(DataBaseControl.convert2SqwakItem(context, cursor, i));
        }

        return lista;
    }

    @Override
    public void updateDataSet(@Nullable List<SqwakItem> items) {
        super.updateDataSet(items);
    }

    @Override
    public void updateItem(@NonNull SqwakItem item) {
        super.updateItem(item);
    }


}
