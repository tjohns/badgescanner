// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.util.Log;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/25/12
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class NfcField implements Field {
    private int mStartBlock;
    private int mBlockCount;
    private String mValue;
    private static final int NFC_BLOCK_LENGTH = 15;
    private static final String TAG = "MwcBadge";

    public NfcField(int startBlock, int blockCount) {
        mStartBlock = startBlock;
        mBlockCount = blockCount;
    }

    @Override
    public String getValue() {
        return mValue;
    }

    @Override
    public void setValue(String value) {
        mValue = value;
    }

    public void readFromTag(NfcConnection nfcConnection) throws IOException {
        byte[] data = new byte[NFC_BLOCK_LENGTH * mBlockCount];
        int dataLen = 0;

        // Read all blocks that will contain desired data
        for (int i = mStartBlock; i < mStartBlock + mBlockCount; i++) {
            try {
                byte[] block = nfcConnection.readBlock(i);
                // This block is just a fragment of data (15 bytes). Copy into the
                // combined data array, where it is merged with other fragments.
                for (int j = 0; j < NFC_BLOCK_LENGTH; j++) {
                    data[dataLen++] = block[j];
                }
            } catch (NfcConnection.NonDataRegionException e) {
                // Do nothing. We tried to read the key storage area for a sector.
            }
        }

        // Convert to a string. We're assuming this is UTF-8. Trailing characters are used for
        // padding, so those are removed.
        mValue = new String(data, 0, dataLen).trim();
    }
}
