package com.example.angel.sqawker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import com.example.angel.sqawker.firebase.MyFirebaseInstanceIdService;
import com.example.angel.sqawker.firebase.MyFirebaseService;
import com.example.angel.sqawker.provider.InstructorsContract;
import com.example.angel.sqawker.provider.SqawkContract;
import com.example.angel.sqawker.provider.SqawkProvider;
import com.example.angel.sqawker.recycler.SqwakAdapter;
import com.example.angel.sqawker.recycler.SqwakItem;
import com.example.angel.sqawker.utils.Instructor;
import com.example.angel.sqawker.utils.InstructorsInfo;
import com.google.firebase.iid.FirebaseInstanceId;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.sqwaker_recycler_view)
    RecyclerView sqwakerRecyclerView;
    SqwakAdapter sqwakAdapter;
    SharedPreferences pref;
    private MyFirebaseService myFirebaseService = new MyFirebaseService();
    private boolean loadData = true;

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadData = true;
        //secuentialLoaders();
    }


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

        JodaTimeAndroid.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

        } else {
            //TODO Cambiar por el error de C
            //https://github.com/JakeWharton/timber/blob/master/timber-sample/src/main/java/com/example/timber/ExampleApp.java
            Timber.plant(new Timber.DebugTree());
        }

        Timber.d("Timber is working");
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);


        // Initialize the Adapter
        sqwakAdapter = new SqwakAdapter(this);
        sqwakerRecyclerView.setAdapter(sqwakAdapter);

        //Configure adapter
        sqwakAdapter.setLongPressDragEnabled(true);
        sqwakerRecyclerView.setHasFixedSize(true);


        sqwakerRecyclerView.setLayoutManager(layoutManager);

        fillDataBases(); //Da error ya que no está cargada la base de instructores.
        secuentialLoaders();
        Intent intentFirebaseMessService = new Intent(this, MyFirebaseService.class);
        startService(intentFirebaseMessService);
        Intent intentFirebaseIdService = new Intent(this, MyFirebaseInstanceIdService.class);
        startService(intentFirebaseIdService);


        //Presenta informacion sobre la app
        String firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Token firebase de la app: " + firebaseToken);
    }

    @Override
    protected void onDestroy() {
        pref.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // if (loadData) secuentialLoaders();
        loadData = false;
        super.onResume();
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


    private Boolean multipleLoaders = false;

    private void secuentialLoaders() {
        multipleLoaders = true;
        getSupportLoaderManager().initLoader(1, null, this);
    }

    private void secuentialLoadersLast() {
        multipleLoaders = false;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        LOADERS loader = LOADERS.valueOf(id);

        switch (loader) {


            case LOADER_MESSAGES_ID:
                String[] projection = new String[]{SqawkContract.COLUMN_AUTOR_KEY, InstructorsContract.COLUMN_AUTHOR_NAME, SqawkContract.COLUMN_DATE, SqawkContract.COLUMN_MESSAGE, InstructorsContract.COLUMN_FOLLOWING};
                return new CursorLoader(this, SqawkProvider.SqawkMessages.CONTENT_URI_FOLLOWED, projection, null, null, null);

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

        int id = loader.getId();
        switch (LOADERS.valueOf(id)) {

            case LOADER_INSTRUCTORS_ID:
                cursor = (Cursor) data;
                InstructorsInfo.setInfo(this, cursor);
                getSupportLoaderManager().initLoader(++id, null, this);
                break;
            case LOADER_MESSAGES_ID:
                cursor = (Cursor) data;
                sqwakAdapter.updateDataSet(cursor);
                secuentialLoadersLast();
                break;

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
            int following = instructor.getFollowing() ? 1 : 0;

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
        contentResolver.delete(SqawkProvider.SqawkMessages.CONTENT_URI_MESSAGES, null, null);
        contentResolver.bulkInsert(SqawkProvider.SqawkMessages.CONTENT_URI_MESSAGES, values);


    }


    private List<Instructor> getInstructors() {
        List<Instructor> lista = new ArrayList<>();
        lista.add(new Instructor(this, "key_asser", "Asser", "asser", true));
        lista.add(new Instructor(this, "key_cezanne", "Cezanne", "cezanne", true));
        lista.add(new Instructor(this, "key_jlin", "JLin", "jlin", true));
        lista.add(new Instructor(this, "key_lyla", "Lyla", "lyla", true));
        lista.add(new Instructor(this, "key_nikita", "Nikita", "nikita", true));
        lista.add(new Instructor(this, "key_test", "Test", "test", true));


        return lista;
    }


    private List<SqwakItem> generateRandomMessages() {

        List<SqwakItem> lista = new ArrayList<>();
        DateTime dateNow = DateTime.now();

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.test);


        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Subjects to ecstatic children he. Could ye leave up as built match. Dejection agreeable attention set suspected led offending. Admitting an performed supposing by. Garden agreed matter are should formed temper had. Full held gay now roof whom such next was. Ham pretty our people moment put excuse narrow. Spite mirth money six above get going great own. Started now shortly had for assured hearing expense. Led juvenile his laughing speedily put pleasant relation offering. "));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Carried nothing on am warrant towards. Polite in of in oh needed itself silent course. Assistance travelling so especially do prosperous appearance mr no celebrated. Wanted easily in my called formed suffer. Songs hoped sense as taken ye mirth at. Believe fat how six drawing pursuit minutes far. Same do seen head am part it dear open to. Whatever may scarcely judgment had."));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Needed feebly dining oh talked wisdom oppose at. Applauded use attempted strangers now are middleton concluded had."));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "It is tried no added purse shall no on truth. Pleased anxious or as in by viewing forbade minutes prevent. Too leave had those get being led weeks blind. Had men rose from down lady able. Its son him ferrars proceed six parlors."));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Her say projection age announcing decisively men. Few gay sir those green men timed downs widow chief. Prevailed remainder may propriety can and. "));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "By impossible of in difficulty discovered celebrated ye. Justice joy manners boy met resolve produce. Bed head loud next plan rent had easy add him. As earnestly shameless elsewhere defective estimable fulfilled of. Esteem my advice it an excuse enable. Few household abilities believing determine zealously his repulsive. To open draw dear be by side like. "));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "HI!"));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Good morning"));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Now residence dashwoods she excellent you. Shade being under his bed her."));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Pleasant horrible but confined day end marriage. Eagerness furniture set preserved far recommend. Did even but nor are most gave hope. Secure active living depend son repair day ladies now. "));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Of resolve to gravity thought my prepare chamber so. Unsatiable entreaties collecting may sympathize nay interested instrument. If continue building numerous of at relation in margaret. Lasted engage roused mother an am at."));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Here we are"));
        lista.add(new SqwakItem(this, dateNow.plusMinutes(23), InstructorsInfo.TEST_ACCOUNT_KEY, "Test", bmp, "Other early while if by do to. Missed living excuse as be. Cause heard fat above first shall for. My smiling to he removal weather on anxious. "));


        return lista;
    }
}
