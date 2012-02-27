// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/26/12
 * Time: 1:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class NotesField implements Field {
    private String mValue;
    
    @Override
    public String getValue() {
        return mValue;
    }

    @Override
    public void setValue(String value) {
        mValue = value;
    }

    @Override
    public void readFromTag(NfcConnection nfcConnection) throws IOException {
        // This is a notes field, it does not exist on tags.
        return;
    }
}
