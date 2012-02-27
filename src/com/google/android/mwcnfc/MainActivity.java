// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends BaseActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //NfcAdapter a = NfcAdapter.getDefaultAdapter(this);
        //a.enableForegroundDispatch(ScanActivity.class, new PendingIntent);
    }
}
