// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/26/12
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Preferences extends PreferenceActivity {

    public final static String TAG = "MwcNfc";
    public final static String PREF_ACCOUNT = "account";
    public final static String PREF_GROUP = "group";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class GeneralPrefsFragment extends PreferenceFragment{
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            // Add account list
            ListPreference accountPreference = (ListPreference) getPreferenceScreen().findPreference(PREF_ACCOUNT);
            String[] accounts = Accounts.GetAccounts(getActivity());
            accountPreference.setEntries(accounts);
            accountPreference.setEntryValues(accounts); // values are the same as entries

            // TODO(trevorjohns): Add preference for background NFC read
        }
    }


}
