package com.example.zhangzhao.mweibo.authenticator;

import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AccountsException;
import android.app.Activity;
import android.os.Bundle;

import com.example.zhangzhao.mweibo.Constants;

import java.io.IOException;

import static android.accounts.AccountManager.KEY_AUTHTOKEN;

/**
 * Created by zhangzhao on 2015/8/11.
 */
public class ApiKeyProvider {
    private AccountManager accountManager;
    public ApiKeyProvider(AccountManager accountManager) {
        this.accountManager = accountManager;
    }



    public String getAuthKey(final Activity activity) throws AccountsException, IOException {
        final AccountManagerFuture<Bundle> accountManagerFuture
                = accountManager.getAuthTokenByFeatures(Constants.ACCOUNT_TYPE,
                Constants.AUTHTOKEN_TYPE, new String[0], activity, null, null, null, null);
        return accountManagerFuture.getResult().getString(KEY_AUTHTOKEN);
    }


}
