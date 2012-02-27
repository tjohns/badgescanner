// Copyright 2012 Google Inc. All Rights Reserved.

package com.google.android.mwcnfc;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.preference.ListPreference;

/**
 * Created by IntelliJ IDEA.
 * User: trevorjohns
 * Date: 2/26/12
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Accounts {
    public static final String ACCOUNT_TYPE_GOOGLE = "com.google";
    
    public static String[] GetAccounts(Context context) {
        Account accounts[] = AccountManager.get(context).getAccountsByType(ACCOUNT_TYPE_GOOGLE);
        String accountEntries[] = new String[accounts.length];
        for (int i = 0; i < accounts.length; i++) {
            accountEntries[i] = accounts[i].name;
        }
        return accountEntries;
    }
}
