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

import android.nfc.TagLostException;
import android.nfc.tech.MifareClassic;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

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
