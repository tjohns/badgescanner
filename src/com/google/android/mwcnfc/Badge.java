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

package com.google.android.mwcnfc;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;
import junit.framework.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Badge {
    private HashMap<String,Field> mFields = new HashMap<String,Field>() {
        {
            put("firstName", new NfcField(0x08, 3));
            put("lastName", new NfcField(0x0C, 3));
            put("company", new NfcField(0x1A, 10));
            put("jobTitle", new NotesField());
            put("email", new NfcField(0x14, 6));
            put("phone", new NfcField(0x24, 2));
            put("notes", new NotesField());
        }
    };

    private static final String TAG = "MwcBadge";

    public void readFromTag(NfcConnection nfcConnection) throws IOException {
        for (Map.Entry<String, Field> i : mFields.entrySet()) {
            String fieldName = i.getKey();
            Field fieldData = i.getValue();

            Log.d(TAG, "Requesting field: " + fieldName);
            fieldData.readFromTag(nfcConnection);
            Log.i(TAG, "New data: " + fieldName + " => [" + fieldData.getValue() + "]");
        }
    }
    
    public String getField(String name) {
        return mFields.get(name).getValue();
    }
    
    public void setField(String name, String value) {
        mFields.get(name).setValue(value);
    }

    public void save(Context ctx) {
        ContentValues values = new ContentValues();
        ContentResolver cr = ctx.getContentResolver();
        long targetGroupId = prepareTargetGroupId(ctx);
        createContact(ctx, targetGroupId);
    }
    
    private long prepareTargetGroupId(Context ctx) {
        long groupId;

        ContentResolver cr = ctx.getContentResolver();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        // Find group for MWC 2012
        String groupName = prefs.getString(Preferences.PREF_GROUP,
                ctx.getString(R.string.group_default));

        Uri uri = ContactsContract.Groups.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE
        };
        String selection = ContactsContract.Groups.TITLE + " = ?";
        String[] selectionArgs = {groupName};
        String sortOrder = "_ID ASC";

        Cursor result = cr.query(uri, projection, selection, selectionArgs, sortOrder);
        if (result.getCount() > 0) {
            result.moveToNext();
            groupId = result.getInt(0);
        } else {
            // Group doesn't exist, so it needs to be created
            ContentValues values = new ContentValues();
            values.put(ContactsContract.Groups.TITLE, groupName);
            values.put(ContactsContract.Groups.ACCOUNT_NAME, prefs.getString(Preferences.PREF_ACCOUNT, null));
            // TODO(trevorjohns): This should be refactored so that we're not touching app logic in the data model.
            // Probably would be good to prompt closer to here, too.
            values.put(ContactsContract.Groups.ACCOUNT_TYPE, Accounts.ACCOUNT_TYPE_GOOGLE);
            Uri newGroup = cr.insert(ContactsContract.Groups.CONTENT_URI, values);
            groupId = ContentUris.parseId(newGroup);
        }
        result.close();
        return groupId;
    }

    private void createContact(Context ctx, long targetGroupId) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        // Prepare contact creation request
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, Accounts.ACCOUNT_TYPE_GOOGLE)
                // TODO(trevorjohns): Extact preference check from data model.
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, prefs.getString(Preferences.PREF_ACCOUNT, null))
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, getField("firstName") + " " +
                        getField("lastName"))
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, getField("company"))
                .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, getField("jobTitle"))
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Email.DATA, getField("email"))
                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, getField("phone"))
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Note.NOTE, getField("notes"))
                .build());
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID, targetGroupId)
                .build());
        try {
            ctx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            // Display warning
            CharSequence txt = ctx.getString(R.string.contact_creation_failure);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(ctx, txt, duration);
            toast.show();

            // Log exception
            Log.e(TAG, "Exception encountered while inserting contact: " + e);
            //TODO(trevorjohns): This should probably be a dialog
        }

    }
}