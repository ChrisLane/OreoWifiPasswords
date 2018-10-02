package com.pithsoftware.wifipasswords.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.*;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.pithsoftware.wifipasswords.R;
import com.pithsoftware.wifipasswords.dialogs.AboutDialogFragment;
import com.pithsoftware.wifipasswords.extras.AppCompatPreferenceActivity;
import com.pithsoftware.wifipasswords.extras.MyApplication;
import com.pithsoftware.wifipasswords.extras.RequestCodes;


public class SettingsActivity extends AppCompatPreferenceActivity {

    SettingsFragment mSettingsFragment;

    static final String SETTINGS_FRAGMENT_TAG = "settings_fragment_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MyApplication.sIsDark) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.action_settings));

        ActionBar sBar = getSupportActionBar();
        if (sBar != null) {
            sBar.setDisplayShowHomeEnabled(true);
            sBar.setDisplayHomeAsUpEnabled(true);
            sBar.setDisplayShowTitleEnabled(true);

        }


        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();

        } else {
            mSettingsFragment = (SettingsFragment) getFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG);
        }

        // Display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(R.id.settings_frame, mSettingsFragment, SETTINGS_FRAGMENT_TAG).commit();

    }


    //Required Method to Override to Validated Fragments
    @Override
    protected boolean isValidFragment(String fragmentName) {

        return SettingsFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_help:
                AboutDialogFragment dialog = AboutDialogFragment.getInstance();
                dialog.show(getFragmentManager(), getString(R.string.dialog_about_key));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /***************************************************************/
    /****************** Settings Fragment **************************/
    /***************************************************************/
    public static class SettingsFragment extends PreferenceFragment {


        /***** Bind Summary to value - Listener *****/
        private Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener
                = (preference, newValue) -> {

            getActivity().setResult(RESULT_OK);

            String stringValue = newValue.toString();

            if (preference instanceof EditTextPreference) {

                preference.setSummary(stringValue);

            } else if (preference instanceof ListPreference) {

                int index = ((ListPreference) preference).findIndexOfValue(stringValue);
                String summary = "";

                if (preference.getKey().equals(getString(R.string.pref_auto_update_key))) {
                    String disabled = getResources().getStringArray(R.array.pref_auto_update_list_values)[0];
                    if (!stringValue.equals(disabled))
                        summary += getString(R.string.pref_auto_update_summary) + " - ";
                }

                summary += ((ListPreference) preference).getEntries()[index];
                preference.setSummary(summary);
            }

            return true;
        };

        private void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getActivity().setResult(RESULT_CANCELED);

            loadGeneralPreferences();
        }


        //Helper method for onCreate
        public void loadGeneralPreferences() {


            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);


            Preference resetManualPath = findPreference(getString(R.string.pref_reset_manual_key));
            resetManualPath.setOnPreferenceClickListener(preference -> {
                resetPathPref();
                return true;
            });
            // Allow the Default file path to be different for Oreo+
            if (android.os.Build.VERSION.SDK_INT >= 26) { // Hard-CODED: Oreo
                String currentValue = PreferenceManager.getDefaultSharedPreferences(getActivity())
                        .getString(getString(R.string.pref_path_manual_key), getString(R.string.pref_path_default));
                if (currentValue.equals(getString(R.string.pref_path_default))) {
                    resetPathPref();
                }
            }

            //set dependency on checkbox
            resetManualPath.setDependency(getString(R.string.pref_path_checkbox_key));
            findPreference(getString(R.string.pref_path_manual_key)).setDependency(getString(R.string.pref_path_checkbox_key));

            findPreference(getString(R.string.pref_default_key)).setOnPreferenceClickListener(preference -> {
                showResetWarningDialog();
                return true;
            });

            findPreference(getString(R.string.pref_show_no_password_key)).setOnPreferenceClickListener(preference -> {
                getActivity().setResult(RequestCodes.SHOW_NO_PASSWORD_CODE);
                return true;
            });

            findPreference(getString(R.string.pref_dark_theme_key)).setOnPreferenceClickListener(preference -> {
                MyApplication.darkTheme((CheckBoxPreference) preference);
                getActivity().setResult(RequestCodes.DARK_THEME);
                getActivity().finish();
                return true;
            });

            findPreference(getString(R.string.pref_hide_pass_key)).setOnPreferenceClickListener(preference -> {
                MyApplication.hidePass((CheckBoxPreference) preference);
                getActivity().setResult(RequestCodes.HIDE_PASS);
                getActivity().finish();
                return true;
            });

            //Summary to Value
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_path_manual_key)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_auto_update_key)));
        }

        //Restore wpa_supplicant path to default
        private void resetPathPref() {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = settings.edit();
            if (android.os.Build.VERSION.SDK_INT >= 26) { // Hard-CODED: Oreo
                editor.putString(getString(R.string.pref_path_manual_key), getString(R.string.pref_path_default_oreo));
            } else {
                editor.putString(getString(R.string.pref_path_manual_key), getString(R.string.pref_path_default));
            }
            editor.apply();


            if (android.os.Build.VERSION.SDK_INT >= 26) { // Hard-CODED: Oreo
                findPreference(getString(R.string.pref_path_manual_key)).setSummary(getString(R.string.pref_path_default_oreo));
            } else {
                findPreference(getString(R.string.pref_path_manual_key)).setSummary(getString(R.string.pref_path_default));
            }

            //Refresh Preference Screen
            setPreferenceScreen(null);
            loadGeneralPreferences();

        }


        private void showResetWarningDialog() {

            String[] buttons = getResources().getStringArray(R.array.dialog_warning_reset_buttons);

            AlertDialog.Builder builder;

            if (MyApplication.sIsDark) {
                builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme_Dark);
            } else {
                builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
            }

            //Send Result Codes to target fragment according to button clicked
            builder.setMessage(R.string.dialog_warning_reset_message)
                    .setTitle(R.string.dialog_warning_reset_title)
                    .setPositiveButton(buttons[0], (dialog, which) -> {
                        getActivity().setResult(RequestCodes.RESET_TO_DEFAULT);
                        getActivity().finish();
                    })
                    .setNegativeButton(buttons[1], (dialog, which) -> {
                        //Dismiss Dialog
                    });

            builder.create().show();
        }

    }
}
