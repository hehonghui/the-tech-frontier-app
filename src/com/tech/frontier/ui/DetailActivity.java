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

package com.tech.frontier.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.tech.frontier.R;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.UserInfo;
import com.tech.frontier.presenters.ArticleDetailPresenter;
import com.tech.frontier.presenters.AuthPresenter;
import com.tech.frontier.ui.interfaces.ArticleDetailView;
import com.tech.frontier.utils.HtmlTemplate;

/**
 * 文章阅读页面,使用WebView加载文章。
 * 
 * @author mrsimple
 */
public class DetailActivity extends BaseActionBarActivity implements ArticleDetailView {

    ProgressBar mProgressBar;
    WebView mWebView;
    private String mPostId;
    private String mTitle;
    private String mTargetUrl;
    ArticleDetailPresenter mPresenter = new ArticleDetailPresenter(this);
    AuthPresenter mAuthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupToolbar();
        initViews();
        initArticleUrl();
        if (!TextUtils.isEmpty(mTargetUrl)) { // 加载推荐的链接
            mWebView.loadUrl(mTargetUrl);
        } else { // 加载文章
            mPresenter.fetchArticleContent(mPostId);
        }

        mAuthPresenter = new AuthPresenter(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
        mWebView = (WebView) findViewById(R.id.articles_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebSettings settings = mWebView.getSettings();
                settings.setBuiltInZoomControls(true);
                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initArticleUrl() {
        Bundle extraBundle = getIntent().getExtras();
        if (extraBundle != null) {
            mPostId = extraBundle.getString("post_id");
            mTitle = extraBundle.getString("title");
            mTargetUrl = extraBundle.getString("url");
        }
    }

    @Override
    public void showArticleContent(String html) {
        mWebView.loadDataWithBaseURL("", HtmlTemplate.wrap(mTitle, html), "text/html", "utf8",
                "404");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAuthPresenter.login(new DataListener<UserInfo>() {

            @Override
            public void onComplete(UserInfo result) {

            }
        });
        switch (item.getItemId()) {
            case R.id.action_share:
                Toast.makeText(getApplicationContext(), "share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_favorite:

                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthPresenter.onActivityResult(requestCode, resultCode, data);
    }

}
