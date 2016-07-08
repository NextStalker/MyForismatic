package com.next.myforismatic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.next.myforismatic.fragments.SettingsFragment;

/**
 * Created by maslparu on 06.07.2016.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (getSupportFragmentManager().findFragmentByTag(TAG) == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, new SettingsFragment(), TAG)
                    .commit();
        }
    }
}
