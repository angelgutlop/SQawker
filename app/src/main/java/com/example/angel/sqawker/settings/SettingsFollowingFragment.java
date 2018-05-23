package com.example.angel.sqawker.settings;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.angel.sqawker.Controls.SwitchPreferenceCompatCustom;
import com.example.angel.sqawker.R;
import com.example.angel.sqawker.provider.InstructorsContract;
import com.example.angel.sqawker.provider.SqawkProvider;
import com.example.angel.sqawker.utils.Instructor;
import com.example.angel.sqawker.utils.InstructorsInfo;

import java.util.Iterator;
import java.util.Map;


public class SettingsFollowingFragment extends PreferenceFragmentCompat implements android.support.v7.preference.Preference.OnPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.settings_following);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(getContext());


        PreferenceCategory category = new PreferenceCategory(getContext());
        category.setTitle("Instructores disponibles");

        Map<String, Instructor> instructors = InstructorsInfo.getInstructorsAvailable();
        screen.addPreference(category);


        Iterator<String> iterInstructors = instructors.keySet().iterator();


        while (iterInstructors.hasNext()) {

            String key = iterInstructors.next();
            Instructor instructor = instructors.get(key);

            SwitchPreferenceCompatCustom switchPreference = new SwitchPreferenceCompatCustom(getContext());
            switchPreference.setKey(key);
            switchPreference.setOnPreferenceChangeListener(this);
            switchPreference.setWidgetLayoutResource(R.xml.custom_switch_preferences);
            switchPreference.setSummaryOff("No lo sigues");
            switchPreference.setSummaryOn("Siguiendo");
            switchPreference.setTitle(instructor.autor_name);
            switchPreference.setIcon(new BitmapDrawable(getResources(), instructor.autor_bmp_image));
            category.addPreference(switchPreference);
            switchPreference.setChecked(instructor.following);

        }


        setPreferenceScreen(screen);


    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean b = (Boolean) newValue;
        String key = preference.getKey();

        int value = b ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(InstructorsContract.COLUMN_FOLLOWING, value);
        ContentResolver contentResolver = getContext().getContentResolver();

        contentResolver.update(SqawkProvider.Instructors.CONTENT_URI, values, InstructorsContract.COLUMN_AUTHOR_KEY + "='" + key + "' ", null);
       

        return false;
    }
}
