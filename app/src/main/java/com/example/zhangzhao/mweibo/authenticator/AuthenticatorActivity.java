

package com.example.zhangzhao.mweibo.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.zhangzhao.mweibo.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Activity which displays login screen to the user.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public static final String PARAM_USERNAME = "username";

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";

    private static final String TAG = "AuthenticatorActivity";
    private AccountManager mAccountManager;



    protected boolean mRequestNewAccount = false;

    private String mUsername;


    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle icicle) {


        super.onCreate(icicle);
        mAccountManager = AccountManager.get(this);

        final Intent intent = getIntent();
        mUsername = intent.getStringExtra(PARAM_USERNAME);
        mRequestNewAccount = mUsername == null;
        Log.v(TAG, "request new: " + mRequestNewAccount);

        handleLogin();
    }


    public void handleLogin() {
        AuthInfo mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, null);
        class AuthListener implements WeiboAuthListener {
            @Override
            public void onComplete(Bundle values) {
                Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
                if (mAccessToken.isSessionValid()) {
                    mUsername = mAccessToken.getUid();
                    onAuthenticationResult(mAccessToken.getToken()); //保存Token
                } else {
// 当您注册的应用程序签名不正确时，就会收到错误Code，请确保签名正确
                    String code = values.getString("code", "");
                    Log.i(TAG,code);
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                e.printStackTrace();
            }

            @Override
            public void onCancel() {

            }

        }
        SsoHandler mSsoHandler = new SsoHandler(this, mAuthInfo);
        mSsoHandler.authorizeWeb(new AuthListener());
    }


    private void finishLogin(String authToken) {

        Log.i(TAG, "finishLogin()");

        final Account account = new Account(mUsername, Constants.ACCOUNT_TYPE);
        if (mRequestNewAccount) {
            mAccountManager.addAccountExplicitly(account, null, null);
            mAccountManager.setAuthToken(account, Constants.ACCOUNT_TYPE, authToken);
        } else {
            mAccountManager.setAuthToken(account, Constants.ACCOUNT_TYPE, authToken);

        }
        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, mUsername);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }


    public void onAuthenticationResult(String authToken) {

        boolean success = ((authToken != null) && (authToken.length() > 0));
        Log.i(TAG, "onAuthenticationResult(" + success + ")");


        if (success) {
            finishLogin(authToken);
        } else {
            Log.e(TAG, "onAuthenticationResult: failed to authenticate");

        }
    }

    public void onAuthenticationCancel() {
        Log.i(TAG, "onAuthenticationCancel()");


    }


}

