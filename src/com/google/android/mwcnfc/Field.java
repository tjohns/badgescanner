// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/26/12
 * Time: 1:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Field {

    public String getValue();

    public void setValue(String value);

    public void readFromTag(NfcConnection nfcConnection) throws IOException;

}
