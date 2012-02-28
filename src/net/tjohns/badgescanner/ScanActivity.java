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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.tjohns.badgescanner.widget.CardBack;
import net.tjohns.badgescanner.widget.CardFront;

import java.io.IOException;

public class ScanActivity extends BaseActivity
{
    private static final String TAG = "BadgeScanner";
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

    private static final String ROTATION_AXIS_PROP = "rotationY";
    private static final int ROTATION_HALF_DURATION = 100;
    private static final String FRAGMENTS_ADDED = "FRAGMENTS_ADDED";

    private CardFront mFront;
    private CardBack mBack;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (savedInstanceState == null
                || (!savedInstanceState.containsKey(FRAGMENTS_ADDED) || !savedInstanceState
                .getBoolean(FRAGMENTS_ADDED, false))) {
            scanBadge();
            mFront = new CardFront(new MwcContact(mBadge.getField("firstName"),
                                                  mBadge.getField("lastName"),
                                                  null,
                                                  mBadge.getField("company"),
                                                  mBadge.getField("email"),
                                                  mBadge.getField("phone")));
            mBack = new CardBack();

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.root, mFront);
            ft.add(R.id.root, mBack);
            ft.commit();
        }

        // Get handles to fields
        /*mFirstName = (TextView) findViewById(R.id.firstName);
        mLastName = (TextView) findViewById(R.id.lastName);
        mCompany = (TextView) findViewById(R.id.company);
        mJobTitle = (TextView) findViewById(R.id.jobTitle);
        mEmail = (TextView) findViewById(R.id.email);
        mPhone = (TextView) findViewById(R.id.phone);
        mNotes = (TextView) findViewById(R.id.notes);
        mSave = (Button) findViewById(R.id.save);
        mCancel = (Button) findViewById(R.id.cancel);*/
        // TODO(trevorjohns): Remove

    }

    public void save(View view) {
        mBadge.setField("jobTitle", ((TextView) findViewById(R.id.job_title)).getText().toString());
        mBadge.setField("notes", ((TextView) findViewById(R.id.notes)).getText().toString());
        if (PreferenceManager.getDefaultSharedPreferences(this).getString(Preferences.PREF_ACCOUNT, null) != null) {
            mBadge.save(this);
            String successMessage = getResources().getString(R.string.contact_created, mBadge.getField("firstName"), mBadge.getField("lastName"));
            Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_SHORT);
            finish();
        } else {
            showDialog(DIALOG_ACCOUNT_CHOOSER_ID);
            // Dialog will resume save once account is selected
        }
    }

    public void flipToBack(View view) {
        // animate the card transition. We do this in 3 steps:
        // 1. Rotate out the front fragment
        // 2. Switch the fragments
        // 3. Rotate in the back
        ObjectAnimator anim = ObjectAnimator.ofFloat(mFront.getView(),
                ROTATION_AXIS_PROP, 0, 90).setDuration(ROTATION_HALF_DURATION);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                mFront.getView().setVisibility(View.GONE);
                mBack.getView().setVisibility(View.VISIBLE);

                // rotate in the new note
                ObjectAnimator.ofFloat(mBack.getView(), ROTATION_AXIS_PROP,
                        -90, 0).start();
            }
        });
        anim.start();
    }

    public void flipToFront(View view) {
        // animate the card transition. We do this in 3 steps:
        // 1. Rotate out the back fragment
        // 2. Switch the fragments
        // 3. Rotate in the front
        ObjectAnimator anim = ObjectAnimator.ofFloat(mBack.getView(),
                ROTATION_AXIS_PROP, 0, -90).setDuration(ROTATION_HALF_DURATION);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                mBack.getView().setVisibility(View.GONE);
                mFront.getView().setVisibility(View.VISIBLE);

                // rotate in the new note
                ObjectAnimator.ofFloat(mFront.getView(), ROTATION_AXIS_PROP,
                        90, 0).start();
            }
        });
        anim.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FRAGMENTS_ADDED, true);
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
    
    public void takePhoto(View v) {
    	
    }
}
