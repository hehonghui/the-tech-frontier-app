/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2015 Umeng, Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tech.frontier.presenters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tech.frontier.entities.UserInfo;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.net.UserAPI;
import com.tech.frontier.net.impl.UserAPIImpl;
import com.tech.frontier.utils.Constants;
import com.tech.frontier.utils.LoginSession;

public class AuthPresenter {
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private AuthInfo mAuthInfo;
    UserAPI userAPI = new UserAPIImpl();

    public AuthPresenter(Activity context) {
        mAuthInfo = new AuthInfo(context, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(context, mAuthInfo);
    }

    public void login(DataListener<UserInfo> listener) {
        mSsoHandler.authorize(new AuthListener(listener));
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        DataListener<UserInfo> mDataListener;

        public AuthListener(DataListener<UserInfo> listener) {
            mDataListener = listener;
        }

        @Override
        public void onComplete(Bundle values) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                final String uid = mAccessToken.getUid();
                final String token = mAccessToken.getToken();
                Log.i("RESULT", mAccessToken.toString());
                userAPI.fetchUserInfo(uid, token, new DataListener<UserInfo>() {

                    @Override
                    public void onComplete(UserInfo result) {
                        result.token = token;
                        result.uid = uid;
                        LoginSession.getLoginSession().saveUserInfo(result);
                        if (mDataListener != null) {
                            mDataListener.onComplete(result);
                        }
                    }
                });

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                // String code = values.getString("code");

            }
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onWeiboException(WeiboException e) {

        }
    }
}
