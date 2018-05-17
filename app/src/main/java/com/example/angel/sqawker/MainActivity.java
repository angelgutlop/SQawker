package com.example.angel.sqawker;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.angel.sqawker.provider.SqawkProvider;
import com.example.angel.sqawker.recycler.SqwakAdapter;
import com.example.angel.sqawker.recycler.SqwakItem;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    @BindView(R.id.sqwaker_recycler_view)
    RecyclerView sqwakerRecyclerView;


    private static final int LOADER_MESSAGES_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        // Optional but strongly recommended: Compose the initial list
        List<SqwakItem> myItems = getRandomData();

// Initialize the Adapter
        SqwakAdapter sqwakAdapter = new SqwakAdapter(myItems);

// Initialize the RecyclerView and attach the Adapter to it as usual
        sqwakerRecyclerView.setAdapter(sqwakAdapter);
        sqwakerRecyclerView.setLayoutManager(layoutManager);
        //  getSupportLoaderManager().initLoader(LOADER_MESSAGES_ID, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        switch (id) {
            case LOADER_MESSAGES_ID:
                Uri contentUri = SqawkProvider.SqawkMessages.CONTENT_MESSAGES_URI;
                CursorLoader cursorLoader = new CursorLoader(this, contentUri, null, null, null, null);
                return cursorLoader;

            default:
                return null;
        }

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

        if (data == null) return;
        switch (loader.getId()) {
            case LOADER_MESSAGES_ID:
                Cursor cursor = (Cursor) data;
                //   sqwakAdapter.setCursor(cursor);
                break;
            default:
                return;
        }


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    private List<SqwakItem> getRandomData() {

        List<SqwakItem> lista = new ArrayList<>();
        DateTime dateNow = DateTime.now();

        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.cezanne);
        lista.add(new SqwakItem(bitmap, dateNow.plusMinutes(23), "Gertrudis", "Hola!!!"));
        lista.add(new SqwakItem(bitmap, dateNow.plusMinutes(202), "Penelope", "Cuanto tiempo"));
        lista.add(new SqwakItem(bitmap, dateNow.plusDays(2), "Gertrudis", "Hola?"));

        return lista;
    }
}
