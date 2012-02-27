// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BaseActivity extends Activity {
    private static final int DIALOG_LEGAL_ID = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.preferences:
                launchPreferences();
                return true;
            case R.id.report_issue:
                reportIssue();
                return true;
            case R.id.show_legal:
                showLegal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void launchPreferences() {
        Intent i = new Intent(this, Preferences.class);
        startActivity(i);
    }

    public void reportIssue() {
        Intent i = new Intent(android.content.Intent.ACTION_SEND);
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"trevorjohns@google.com"});
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "[MWC NFC] Issue Report");
        i.setType("text/plain");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "Summary:\n\nSteps to reproduce:\n\nAdditional information:\n");
        startActivity(Intent.createChooser(i, "Select Mail Client"));
    }

    public void showLegal() {
        showDialog(DIALOG_LEGAL_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
            case DIALOG_LEGAL_ID:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Legal Notice");
                builder.setMessage(R.string.legal_text);
                dialog = builder.create();
                break;
            default:
                dialog = null;
                super.onCreateDialog(id);
        }
        return dialog;
    }

}
