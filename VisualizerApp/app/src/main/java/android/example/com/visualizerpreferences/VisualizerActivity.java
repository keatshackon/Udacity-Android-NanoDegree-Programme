package android.example.com.visualizerpreferences;


import android.Manifest;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.example.com.visualizerpreferences.AudioVisuals.AudioInputReader;
import android.example.com.visualizerpreferences.AudioVisuals.VisualizerView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.text.NoCopySpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.Toast;

public class VisualizerActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE = 88;
    private VisualizerView mVisualizerView;
    private AudioInputReader mAudioInputReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizer);
        mVisualizerView = findViewById(R.id.activity_visualizer);
        defaultSetup();
        setupPermissions();
    }

    private void defaultSetup() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mVisualizerView.setShowBass(sharedPreferences.getBoolean(getString(R.string.pref_show_bass_key)
                , getResources().getBoolean(R.bool.pref_show_bass_default)));

        mVisualizerView.setShowMid(sharedPreferences.getBoolean(getString(R.string.pref_show_mid_range_key)
                , getResources().getBoolean(R.bool.pref_show_mid_range_default)));

        mVisualizerView.setShowTreble(sharedPreferences.getBoolean(getString(R.string.pref_show_treble_key),
                getResources().getBoolean(R.bool.pref_show_treble_default)));

        loadColorFromPreferences(sharedPreferences);
        loadSizeFromPreferences(sharedPreferences);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getString(R.string.pref_show_bass_key))) {
            mVisualizerView.setShowBass(sharedPreferences.getBoolean(s,
                    getResources().getBoolean(R.bool.pref_show_bass_default)));
        } else if (s.equals(getString(R.string.pref_show_mid_range_key))) {
            mVisualizerView.setShowMid(sharedPreferences.getBoolean(s,
                    getResources().getBoolean(R.bool.pref_show_mid_range_default)));
        } else if (s.equals(getString(R.string.pref_show_treble_key))) {
            mVisualizerView.setShowTreble(sharedPreferences.getBoolean(s,
                    getResources().getBoolean(R.bool.pref_show_treble_default)));
        } else if (s.equals(getString(R.string.pref_color_key))) {
            loadColorFromPreferences(sharedPreferences);
        } else if (s.equals(getString(R.string.pref_size_key))) {
            loadSizeFromPreferences(sharedPreferences);
        }

    }

    private void loadColorFromPreferences(SharedPreferences sharedPreferences) {
        mVisualizerView.setColor(sharedPreferences.getString(getString(R.string.pref_color_key),
                getString(R.string.pref_color_red_value)));
    }

    private void loadSizeFromPreferences(SharedPreferences sharedPreferences) {
        float minSize = Float.parseFloat(sharedPreferences.getString(getString(R.string.pref_size_key),
                getString(R.string.pref_size_default)));

        mVisualizerView.setMinSizeScale(minSize);
    }

    /**
     * Below this point is code you do not need to modify; it deals with permissions
     * and starting/cleaning up the AudioInputReader
     **/

    /**
     * onPause Cleanup audio stream
     **/
    @Override
    protected void onPause() {
        super.onPause();
        if (mAudioInputReader != null) {
            mAudioInputReader.shutdown(isFinishing());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAudioInputReader != null) {
            mAudioInputReader.restart();
        }
    }

    /**
     * App Permissions for Audio
     **/
    private void setupPermissions() {
        // If we don't have the record audio permission...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // And if we're on SDK M or later...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Ask again, nicely, for the permissions.
                String[] permissionsWeNeed = new String[]{Manifest.permission.RECORD_AUDIO};
                requestPermissions(permissionsWeNeed, MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE);
            }
        } else {
            // Otherwise, permissions were granted and we are ready to go!
            mAudioInputReader = new AudioInputReader(mVisualizerView, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_RECORD_AUDIO_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // The permission was granted! Start up the visualizer!
                    mAudioInputReader = new AudioInputReader(mVisualizerView, this);

                } else {
                    Toast.makeText(this, "Permission for audio not granted. Visualizer can't run.", Toast.LENGTH_LONG).show();
                    finish();
                    // The permission was denied, so we can show a message why we can't run the app
                    // and then close the app.
                }
            }
            // Other permissions could go down here

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_setting) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visualizer_menu, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

    }
}