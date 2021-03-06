package com.yoonbae.planting.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Optional;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initBottomNavigationView();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Optional<Preference> optOpenSource = Optional.ofNullable(findPreference("open_source"));
            optOpenSource.ifPresent(openSource -> openSource.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getContext(), OpenSourceListActivity.class);
                startActivity(intent);
                return true;
            }));
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.action_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_calendar:
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.action_list:
                    intent = new Intent(this, ListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        });
    }
}