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

import java.io.IOException;
import java.nio.charset.Charset;

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

    public String getValue() {
        return mValue;
    }

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

        // Convert to a string. Trailing characters are used for
        // padding, so those are removed.
        mValue = new String(data, 0, dataLen, Charset.forName("ISO-8859-15")).trim();
    }
}
