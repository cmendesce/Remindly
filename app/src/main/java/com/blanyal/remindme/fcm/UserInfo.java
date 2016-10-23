package com.blanyal.remindme.fcm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by carlosmendes on 10/23/16.
 */

public class UserInfo {

    private final Context mContext;
    private final AccountManager mAccountManager;

    public UserInfo(Context context) {
        mContext = context;
        mAccountManager = AccountManager.get(context());
    }


    public String email() {
        Account account = getAccount(accountManager());

        if (account == null) {
            return null;
        } else {
            return account.name;
        }
    }

    private Account getAccount(AccountManager accountManager) {
        if (ActivityCompat.checkSelfPermission(context(), android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                     int[] grantResults) {

            //}
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }

    AccountManager accountManager() {
        return mAccountManager;
    }

    Context context() {
        return mContext;
    }

}
