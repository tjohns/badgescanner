// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.nfc.TagLostException;
import android.nfc.tech.MifareClassic;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/25/12
 * Time: 6:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class NfcConnection implements Closeable {
    private MifareClassic mNfcTag;
    private byte[] mKey;
    private int mLastReadSector = -1;
    private static final String TAG = "MwcBadge";

    public NfcConnection(MifareClassic nfcTag, byte[] key) throws IOException {
        if (nfcTag != null) {
            mNfcTag = nfcTag;
            mKey = key;

            mNfcTag.connect();
        } else {
            throw new TagLostException();
        }
    }

    public void close() throws IOException {
       mNfcTag.close();
       mNfcTag = null;
    }
    
    public byte[] readBlock(int block) throws IOException, NonDataRegionException {
        if (mNfcTag != null) {
            if (block % 4 != 3) {
                int sector = mNfcTag.blockToSector(block);
                Log.d(TAG, "NFC Read: Sector " + sector + ", Block " + block);
                if (mLastReadSector != mNfcTag.blockToSector(block)) {
                    Log.d(TAG, "Updating NFC credentials for Sector " + sector) ;
                    mNfcTag.authenticateSectorWithKeyA(sector, mKey);
                }
                return mNfcTag.readBlock(block);
            } else {
                throw new NonDataRegionException();
            }
        } else {
            throw new IOException("NFC connection closed");
        }
    }

    public class NonDataRegionException extends Throwable {
    }
}
