/* Copyright 2016 Braden Farmer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.farmerbb.taskbar.fragment;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.farmerbb.taskbar.R;
import com.farmerbb.taskbar.util.U;

public class RecentAppsFragment extends SettingsFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        finishedLoadingPrefs = false;

        super.onCreate(savedInstanceState);

        // Add preferences
        addPreferencesFromResource(R.xml.pref_recent_apps);

        // Set OnClickListeners for certain preferences
        findPreference("enable_recents").setOnPreferenceClickListener(this);
        findPreference("max_num_of_recents").setOnPreferenceClickListener(this);
        findPreference("refresh_frequency").setOnPreferenceClickListener(this);

        if(showRunningAppsOnly()) {
            ListPreference recentsAmountPref = ((ListPreference) findPreference("recents_amount"));
            recentsAmountPref.setEntries(getResources().getStringArray(R.array.pref_recents_amount_alt));
            recentsAmountPref.setEntryValues(getResources().getStringArray(R.array.pref_recents_amount_values_alt));

            SharedPreferences pref = U.getSharedPreferences(getActivity());
            if(pref.getString("recents_amount", "past_day").equals("running_apps_only")) {
                ListPreference sortOrderPref = ((ListPreference) findPreference("sort_order"));
                sortOrderPref.setEntries(getResources().getStringArray(R.array.pref_sort_order_alt));
                sortOrderPref.setEntryValues(getResources().getStringArray(R.array.pref_sort_order_values_alt));
            }
        }

        bindPreferenceSummaryToValue(findPreference("recents_amount"));
        bindPreferenceSummaryToValue(findPreference("sort_order"));
        bindPreferenceSummaryToValue(findPreference("disable_scrolling_list"));
        bindPreferenceSummaryToValue(findPreference("full_length"));

        updateMaxNumOfRecents(false);
        updateRefreshFrequency(false);

        finishedLoadingPrefs = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setTitle(R.string.pref_header_recent_apps);
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onPreferenceClick(final Preference p) {
        final SharedPreferences pref = U.getSharedPreferences(getActivity());

        switch(p.getKey()) {
            case "enable_recents":
                try {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                } catch (ActivityNotFoundException e) {
                    U.showErrorDialog(getActivity(), "GET_USAGE_STATS");
                }
                break;
            case "max_num_of_recents":
                final int max = 26;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LinearLayout dialogLayout = (LinearLayout) View.inflate(getActivity(), R.layout.seekbar_pref, null);

                String value = pref.getString("max_num_of_recents", "10");

                final TextView textView = U.findViewById(dialogLayout, R.id.seekbar_value);
                textView.setText("0");

                final SeekBar seekBar = U.findViewById(dialogLayout, R.id.seekbar);
                seekBar.setMax(max);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(progress == max)
                            textView.setText(R.string.infinity);
                        else
                            textView.setText(Integer.toString(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

                seekBar.setProgress(Integer.parseInt(value));

                TextView blurb = U.findViewById(dialogLayout, R.id.blurb);
                blurb.setText(R.string.num_of_recents_blurb);

                builder.setView(dialogLayout)
                        .setTitle(R.string.pref_max_num_of_recents)
                        .setPositiveButton(R.string.action_ok, (dialog, id) -> {
                            int progress = seekBar.getProgress();
                            if(progress == max)
                                progress = Integer.MAX_VALUE;

                            pref.edit().putString("max_num_of_recents", Integer.toString(progress)).apply();
                            updateMaxNumOfRecents(true);
                        })
                        .setNegativeButton(R.string.action_cancel, null);

                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case "refresh_frequency":
                final int max2 = 20;

                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                LinearLayout dialogLayout2 = (LinearLayout) View.inflate(getActivity(), R.layout.seekbar_pref, null);

                String value2 = pref.getString("refresh_frequency", "2");

                final TextView textView2 = U.findViewById(dialogLayout2, R.id.seekbar_value);
                textView2.setText(R.string.infinity);

                final SeekBar seekBar2 = U.findViewById(dialogLayout2, R.id.seekbar);
                seekBar2.setMax(max2);
                seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(progress == 0)
                            textView2.setText(R.string.infinity);
                        else
                            textView2.setText(Double.toString(progress * 0.5));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {}

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {}
                });

                seekBar2.setProgress((int) (Double.parseDouble(value2) * 2));

                TextView blurb2 = U.findViewById(dialogLayout2, R.id.blurb);
                blurb2.setText(R.string.refresh_frequency_blurb);

                builder2.setView(dialogLayout2)
                        .setTitle(R.string.pref_title_recents_refresh_interval)
                        .setPositiveButton(R.string.action_ok, (dialog2, id) -> {
                            double progress = seekBar2.getProgress() * 0.5;

                            pref.edit().putString("refresh_frequency", Double.toString(progress)).apply();
                            updateRefreshFrequency(true);
                        })
                        .setNegativeButton(R.string.action_cancel, null);

                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;
        }

        return true;
    }

    private void updateMaxNumOfRecents(boolean restartTaskbar) {
        SharedPreferences pref = U.getSharedPreferences(getActivity());
        int value = Integer.parseInt(pref.getString("max_num_of_recents", "10"));

        switch(value) {
            case 1:
                findPreference("max_num_of_recents").setSummary(R.string.max_num_of_recents_singular);
                break;
            case Integer.MAX_VALUE:
                findPreference("max_num_of_recents").setSummary(R.string.max_num_of_recents_unlimited);
                break;
            default:
                findPreference("max_num_of_recents").setSummary(getString(R.string.max_num_of_recents, value));
                break;
        }

        if(restartTaskbar) U.restartTaskbar(getActivity());
    }

    private void updateRefreshFrequency(boolean restartTaskbar) {
        SharedPreferences pref = U.getSharedPreferences(getActivity());
        String value = pref.getString("refresh_frequency", "2");
        double doubleValue = Double.parseDouble(value);
        int intValue = (int) doubleValue;

        if(doubleValue == 0)
            findPreference("refresh_frequency").setSummary(R.string.refresh_frequency_continuous);
        else if(doubleValue == 1)
            findPreference("refresh_frequency").setSummary(R.string.refresh_frequency_singular);
        else if(doubleValue == (double) intValue)
            findPreference("refresh_frequency").setSummary(getString(R.string.refresh_frequency, Integer.toString(intValue)));
        else
            findPreference("refresh_frequency").setSummary(getString(R.string.refresh_frequency, value));

        if(restartTaskbar) U.restartTaskbar(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();

        // Register listener to check for changed preferences
        if(showRunningAppsOnly())
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Unregister listener
        if(showRunningAppsOnly())
            PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("recents_amount")) {
            boolean useAlt = sharedPreferences.getString(key, "past_day").equals("running_apps_only");

            ListPreference sortOrderPref = ((ListPreference) findPreference("sort_order"));
            sortOrderPref.setEntries(getResources().getStringArray(useAlt ? R.array.pref_sort_order_alt : R.array.pref_sort_order));
            sortOrderPref.setEntryValues(getResources().getStringArray(useAlt ? R.array.pref_sort_order_values_alt : R.array.pref_sort_order_values));

            String sortOrderValue = sharedPreferences.getString("sort_order", "false");
            if(useAlt && sortOrderValue.startsWith("most_used_"))
                sharedPreferences.edit().putString("sort_order", sortOrderValue.replace("most_used_", "")).apply();
        }
    }

    private boolean showRunningAppsOnly() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && U.isSystemApp(getActivity());
    }
}
