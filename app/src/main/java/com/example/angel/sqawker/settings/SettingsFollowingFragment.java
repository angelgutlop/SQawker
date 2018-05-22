package com.example.angel.sqawker.settings;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.angel.sqawker.Controls.SwitchPreferenceCompatCustom;
import com.example.angel.sqawker.R;
import com.example.angel.sqawker.utils.Instructor;
import com.example.angel.sqawker.utils.InstructorsInfo;

import java.util.Iterator;
import java.util.Map;

public class SettingsFollowingFragment extends PreferenceFragmentCompat {


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
            switchPreference.setWidgetLayoutResource(R.xml.custom_switch_preferences);

            switchPreference.setSummaryOff("No lo sigues");
            switchPreference.setSummaryOn("Siguiendo");
            switchPreference.setTitle(instructor.autor_name);
            switchPreference.setKey(key);
            Drawable drawable = new BitmapDrawable(getResources(), instructor.autor_bmp_image);
            switchPreference.setIcon(drawable);
            category.addPreference(switchPreference);
            switchPreference.setChecked(instructor.following);

        }


        setPreferenceScreen(screen);


    }

}
