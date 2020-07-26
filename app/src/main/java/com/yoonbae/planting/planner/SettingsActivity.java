package com.yoonbae.planting.planner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
                .replace(R.id.settings, new SettingsFragment(this))
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        initBottomNavigationView();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private Activity activity;

        public SettingsFragment(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Optional<Preference> optOpenSource = Optional.ofNullable(findPreference("open_source"));
            optOpenSource.ifPresent(openSource -> openSource.setOnPreferenceClickListener(preference -> {
                Intent intent = new Intent(getContext(), OpenSourceListActivity.class);
                startActivity(intent);
                return true;
            }));

            Optional<Preference> optVibration = Optional.ofNullable(findPreference("vibration"));
            optVibration.ifPresent(vibration -> vibration.setOnPreferenceChangeListener((preference, newValue) -> {
                AudioManager audioManager = (AudioManager) activity.getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                if ((boolean) newValue) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                } else {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                return true;
            }));
        }

        private void showSettingsDialog(Activity activity) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("권한 필요");
            builder.setMessage("이 기능을 사용하려면 이 앱에 권한이 필요합니다. 앱 설정에서 권한을 설정할 수 있습니다.");
            builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
                Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivity(intent);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        }
    }

    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);
        bottomNavigationView.setSelectedItemId(R.id.action_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.action_calendar:
                    intent = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.action_list:
                    intent = new Intent(SettingsActivity.this, ListActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return false;
        });
    }
}