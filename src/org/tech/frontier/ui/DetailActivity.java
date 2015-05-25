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

package org.tech.frontier.ui;

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

import com.android.volley.VolleyError;

import org.tech.frontier.R;
import org.tech.frontier.entities.ArticleDetail;
import org.tech.frontier.presenters.ArticleDetailPresenter;
import org.tech.frontier.presenters.FavoritePresenter;
import org.tech.frontier.presenters.SharePresenter;
import org.tech.frontier.ui.interfaces.ArticleDetailView;
import org.tech.frontier.utils.HtmlTemplate;

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
    /**
     * 是否已收藏某篇文章
     */
    private boolean isFavorited = false;
    ArticleDetailPresenter mPresenter = new ArticleDetailPresenter(this);
    SharePresenter mSharePresenter;
    FavoritePresenter mFavoritePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setupToolbar();
        initViews();
        initArticleUrl();
        if (!TextUtils.isEmpty(mTargetUrl)) { // 加载推荐的链接
            mWebView.loadUrl(mTargetUrl);
        } else if (!TextUtils.isEmpty(mPostId)) { // 加载文章
            mPresenter.fetchArticleContent(mPostId);
        } else {
            mWebView.loadDataWithBaseURL("", "<h3>大哥,你的页面没找到呐~</h3>", "text/html", "utf-8", "");
        }

        mSharePresenter = new SharePresenter(getApplicationContext());
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
    public void fetchedData(ArticleDetail result) {
        mWebView.loadDataWithBaseURL("", HtmlTemplate.wrap(mTitle, result.content),
                "text/html", "utf8", "404");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mSharePresenter.handleWeiboResponse(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        mPresenter.isFavorited(mPostId);
        return super.onCreateOptionsMenu(menu);
    }

    private String getShareUrl() {
        return TextUtils.isEmpty(mTargetUrl) ? "http://www.devtf.cn/?p=" + mPostId : mTargetUrl;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                mSharePresenter.share(this, mTitle, getShareUrl());
                break;

            case R.id.action_favorite:
                mPresenter.favorite(this, mPostId, isFavorited);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isFavorited(boolean isFav) {
        isFavorited = isFav;
        if (isFav && mToolbar.getMenu() != null) {
            MenuItem menuItem = mToolbar.getMenu().findItem(R.id.action_favorite);
            if (menuItem != null) {
                menuItem.setTitle(R.string.cancel_fav);
            }
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onError(VolleyError error) {
        hideLoading();
    }
}
