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

package net.tjohns.badgescanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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

    public void showToS() {
        Intent i = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("http://www.google.com/mobile/tnc.html"));
        startActivity(i);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
            case DIALOG_LEGAL_ID:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Legal Notice");
                builder.setMessage(R.string.legal_text);
                builder.setNeutralButton("Terms of Service", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showToS();
                    }});
                dialog = builder.create();
                break;
            default:
                dialog = null;
                super.onCreateDialog(id);
        }
        return dialog;
    }

}
