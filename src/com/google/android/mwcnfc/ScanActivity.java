package com.google.android.mwcnfc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ScanActivity extends BaseActivity
{
    private static final String TAG = "MwcNfc";
    private static final int DIALOG_ACCOUNT_CHOOSER_ID = 1;

    private TextView mFirstName;
    private TextView mLastName;
    private TextView mCompany;
    private TextView mJobTitle;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mNotes;
    private Button mSave;
    private Button mCancel;
    private Badge mBadge;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);

        // Get handles to fields
        mFirstName = (TextView) findViewById(R.id.firstName);
        mLastName = (TextView) findViewById(R.id.lastName);
        mCompany = (TextView) findViewById(R.id.company);
        mJobTitle = (TextView) findViewById(R.id.jobTitle);
        mEmail = (TextView) findViewById(R.id.email);
        mPhone = (TextView) findViewById(R.id.phone);
        mNotes = (TextView) findViewById(R.id.notes);
        mSave = (Button) findViewById(R.id.save);
        mCancel = (Button) findViewById(R.id.cancel);

        scanBadge();
    }

    private void scanBadge() {
        Intent intent = getIntent();

        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            Tag rawTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            MifareClassic tag = MifareClassic.get(rawTag);
            try {
                // Read from badge
                NfcConnection tagConnection = new NfcConnection(tag, MifareClassic.KEY_DEFAULT);
                mBadge = new Badge();
                mBadge.readFromTag(tagConnection);
                tagConnection.close();

                // Update UI
                mFirstName.setText(mBadge.getField("firstName"));
                mLastName.setText(mBadge.getField("lastName"));
                mCompany.setText(mBadge.getField("company"));
                mEmail.setText(mBadge.getField("email"));
                mPhone.setText(mBadge.getField("phone"));
            } catch (TagLostException e) {
                // TODO(trevorjohns): Convert to dialog
                Toast.makeText(this, "Tag lost", Toast.LENGTH_LONG);
                finish();
            } catch (IOException e) {
                // TODO(trevorjohns): Convert to dialog
                Toast.makeText(this, "IOExcpetion detected", Toast.LENGTH_LONG);
                e.printStackTrace();
                finish();
            }
        }
    }

    public void onSave(View v) {
        mBadge.setField("firstName", mFirstName.getText().toString());
        mBadge.setField("lastName", mLastName.getText().toString());
        mBadge.setField("company", mCompany.getText().toString());
        mBadge.setField("jobTitle", mJobTitle.getText().toString());
        mBadge.setField("email", mEmail.getText().toString());
        mBadge.setField("phone", mPhone.getText().toString());
        mBadge.setField("notes", mNotes.getText().toString());
        if (PreferenceManager.getDefaultSharedPreferences(this).getString(Preferences.PREF_ACCOUNT, null) != null) {
            mBadge.save(this);
            finish();
        } else {
            showDialog(DIALOG_ACCOUNT_CHOOSER_ID);
            // Dialog will resume save once account is selected
        }
    }

    public void onCancel(View v) {
        finish();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog;
        switch(id) {
            case DIALOG_ACCOUNT_CHOOSER_ID:
                final String[] accounts = Accounts.GetAccounts(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Account");
                builder.setSingleChoiceItems(accounts, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Write new preference data
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScanActivity.this);
                        SharedPreferences.Editor e = prefs.edit();
                        e.putString(Preferences.PREF_ACCOUNT, accounts[item]);
                        e.commit();
                        // Save badge and exit
                        mBadge.save(ScanActivity.this);
                        ScanActivity.this.finish();

                    }
                });
                dialog = builder.create();
                break;
            default:
                dialog = null;
                super.onCreateDialog(id);
        }
        return dialog;
    }
}
