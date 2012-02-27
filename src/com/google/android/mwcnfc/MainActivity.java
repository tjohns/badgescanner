// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.os.Bundle;

public class MainActivity extends BaseActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        //NfcAdapter a = NfcAdapter.getDefaultAdapter(this);
        //a.enableForegroundDispatch(ScanActivity.class, new PendingIntent);
    }
}
