package com.example.angel.sqawker.Controls;

import android.content.Context;
import android.support.v7.preference.PreferenceViewHolder;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.angel.sqawker.R;

public class SwitchPreferenceCompatCustom extends SwitchPreferenceCompat {

    private Switch aSwitch;
    private Boolean cheked;

    public SwitchPreferenceCompatCustom(Context context) {
        super(context);
        this.setWidgetLayoutResource(R.xml.custom_switch_preferences);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        this.cheked = checked;
        if (aSwitch != null) aSwitch.setChecked(checked);
    }


    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        aSwitch = (Switch) holder.findViewById(R.id.custom_switch_item);
        aSwitch.setChecked(this.cheked);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cheked = isChecked;
                SwitchPreferenceCompatCustom.super.getOnPreferenceChangeListener().onPreferenceChange(SwitchPreferenceCompatCustom.this, isChecked);

                SwitchPreferenceCompatCustom.super.setChecked(isChecked);
            }
        });
    }

    @Override
    public CharSequence getSwitchTextOff() {
        return super.getSwitchTextOff();
    }


}
