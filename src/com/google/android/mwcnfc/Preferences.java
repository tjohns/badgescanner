// Copyright 2012 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.mwcnfc;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import java.util.List;

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
