package com.example.zhangzhao.mweibo.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.zhangzhao.mweibo.Constants;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * Created by zhangzhao on 2015/8/28.
 */
public class LoginService {

    public static void handleLogin(Activity activity) {
        AuthInfo mAuthInfo = new AuthInfo(activity, Constants.APP_KEY,
                Constants.REDIRECT_URL, null);
        class AuthListener implements WeiboAuthListener {
            @Override
            public void onComplete(Bundle values) {
                Oauth2AccessToken mAccessToken = Oauth2AccessToken.parseAccessToken(values); // 从 Bundle 中解析 Token
                if (mAccessToken.isSessionValid()) {
                    String token=mAccessToken.getToken();
                    SharedPreferences sharedPreferences=activity.getSharedPreferences("mweibo", Context.MODE_PRIVATE);
                    Log.i("LoginService",token);
                    sharedPreferences.edit().putString("token",token).commit();
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
        SsoHandler mSsoHandler = new SsoHandler(activity, mAuthInfo);
        mSsoHandler.authorizeWeb(new AuthListener());
    }

}
