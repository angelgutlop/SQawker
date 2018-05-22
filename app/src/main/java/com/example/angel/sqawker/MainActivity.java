package com.example.angel.sqawker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angel.sqawker.provider.InstructorsContract;
import com.example.angel.sqawker.provider.SqawkContract;
import com.example.angel.sqawker.provider.SqawkProvider;
import com.example.angel.sqawker.recycler.SqwakAdapter;
import com.example.angel.sqawker.recycler.SqwakItem;
import com.example.angel.sqawker.utils.Instructor;
import com.example.angel.sqawker.utils.InstructorsInfo;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    @BindView(R.id.sqwaker_recycler_view)
    RecyclerView sqwakerRecyclerView;
    SqwakAdapter sqwakAdapter;

    private enum LOADERS {
        LOADER_INSTRUCTORS_ID(1),
        LOADER_MESSAGES_ID(2);

        private int value;

        private static SparseArray<LOADERS> map = new SparseArray<>();

        LOADERS(int value) {
            this.value = value;
        }


        static {
            for (LOADERS loaders : LOADERS.values()) {
                map.put(loaders.value, loaders);
            }
        }

        public static LOADERS valueOf(int pageType) {
            return map.get(pageType);
        }

        public int getValue() {
            return value;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //fillDataBases(); //Da error ya que no está cargada la base de instructores.

        secuentialLoaders();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


        // Initialize the Adapter
        sqwakAdapter = new SqwakAdapter(this);
        sqwakerRecyclerView.setAdapter(sqwakAdapter);
        sqwakAdapter.setLongPressDragEnabled(true);
        sqwakAdapter.setSwipeEnabled(true);
        // Initialize the RecyclerView and attach the Adapter to it as usual

        sqwakerRecyclerView.setLayoutManager(layoutManager);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.following_settingsButton:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    int nextLoader = 1;
    private Boolean multipleLoaders = false;

    private void secuentialLoaders() {
        multipleLoaders = true;
        nextLoader();
    }

    private void nextLoader() {
        if (!multipleLoaders) return;
        if (nextLoader > LOADERS.values().length) {
            nextLoader = 1;
            multipleLoaders = false;
            return;
        }
        getSupportLoaderManager().initLoader(nextLoader++, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        LOADERS loader = LOADERS.valueOf(id);

        switch (loader) {


            case LOADER_MESSAGES_ID:
                String[] projection = new String[]{SqawkContract.COLUMN_AUTOR_KEY, InstructorsContract.COLUMN_AUTHOR_NAME, SqawkContract.COLUMN_DATE, SqawkContract.COLUMN_MESSAGE, InstructorsContract.COLUMN_FOLLOWING};
                return new CursorLoader(this, SqawkProvider.SqawkMessages.CONTENT_URI, projection, null, null, null);

            case LOADER_INSTRUCTORS_ID:
                return new CursorLoader(this, SqawkProvider.Instructors.CONTENT_URI, null, null, null, null);

            default:
                try {
                    throw new Exception("Loader con id " + id + " no implementado");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        Cursor cursor;
        if (data == null) return;

        switch (LOADERS.valueOf(loader.getId())) {

            case LOADER_MESSAGES_ID:
                cursor = (Cursor) data;
                sqwakAdapter.updateDataSet(cursor);
                nextLoader();
                break;
            case LOADER_INSTRUCTORS_ID:
                cursor = (Cursor) data;
                InstructorsInfo.setInfo(this, cursor);
                nextLoader();

            default:
                return;
        }


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    private void fillDataBases() {

        ContentResolver contentResolver = getContentResolver();

        //La de instructores


        List<Instructor> listaInst = getInstructors();

        ContentValues[] valuesInst = new ContentValues[listaInst.size()];

        for (int i = 0; i < listaInst.size(); i++) {
            Instructor instructor = listaInst.get(i);

            valuesInst[i] = new ContentValues();
            int following = instructor.following ? 1 : 0;

            valuesInst[i].put(InstructorsContract.COLUMN_AUTHOR_NAME, instructor.autor_name);
            valuesInst[i].put(InstructorsContract.COLUMN_AUTHOR_KEY, instructor.autor_key);
            valuesInst[i].put(InstructorsContract.COLUMN_AUTHOR_IMAGE, instructor.autor_image_name);
            valuesInst[i].put(InstructorsContract.COLUMN_FOLLOWING, following);
        }

        contentResolver.delete(SqawkProvider.Instructors.CONTENT_URI, null, null);
        contentResolver.bulkInsert(SqawkProvider.Instructors.CONTENT_URI, valuesInst);

        //La de mensajes
        List<SqwakItem> lista = generateRandomMessages();
        ContentValues[] values = new ContentValues[lista.size()];

        for (int i = 0; i < lista.size(); i++) {
            SqwakItem sqItem = lista.get(i);

            values[i] = new ContentValues();

            values[i].put(SqawkContract.COLUMN_AUTOR_KEY, sqItem.authorKey);
            values[i].put(SqawkContract.COLUMN_AUTOR, sqItem.authorName);
            values[i].put(SqawkContract.COLUMN_DATE, sqItem.date.getMillis());
            values[i].put(SqawkContract.COLUMN_MESSAGE, sqItem.message);
        }
        contentResolver.delete(SqawkProvider.SqawkMessages.CONTENT_URI, null, null);
        contentResolver.bulkInsert(SqawkProvider.SqawkMessages.CONTENT_URI, values);

        ;

    }


    private List<Instructor> getInstructors() {
        List<Instructor> lista = new ArrayList<>();
        lista.add(new Instructor(this, "key_asser", "Asser", "asser", true));
        lista.add(new Instructor(this, "key_cezanne", "Cezanne", "cezanne", true));
        lista.add(new Instructor(this, "key_jlin", "JLin", "jlin", true));
        lista.add(new Instructor(this, "key_lyla", "Lyla", "lyla", true));
        lista.add(new Instructor(this, "key_nikita", "Nikita", "nikita", true));
        lista.add(new Instructor(this, "key_test", "Test", "text", true));


        return lista;
    }


    private List<SqwakItem> generateRandomMessages() {

        List<SqwakItem> lista = new ArrayList<>();
        DateTime dateNow = DateTime.now();

        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.ASSER_KEY, "Hola!!!"));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(32), InstructorsInfo.CEZANNE_KEY, "Que tal vamos?"));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(50), InstructorsInfo.LYLA_KEY, "Bien, y vos?"));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(72), InstructorsInfo.NIKITA_KEY, "Que animado está esto!!!"));
        lista.add(new SqwakItem(this, dateNow.plusDays(2), InstructorsInfo.NIKITA_KEY, "Hola???"));


        return lista;
    }
}
