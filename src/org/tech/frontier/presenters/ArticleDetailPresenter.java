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

package org.tech.frontier.presenters;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import org.tech.frontier.db.ArticleDetailDBAPI;
import org.tech.frontier.db.FavoriteDBAPI;
import org.tech.frontier.db.models.DbFactory;
import org.tech.frontier.entities.Article;
import org.tech.frontier.entities.ArticleDetail;
import org.tech.frontier.entities.UserInfo;
import org.tech.frontier.listeners.DataListener;
import org.tech.frontier.net.ArticleAPI;
import org.tech.frontier.net.impl.ArticleAPIImpl;
import org.tech.frontier.ui.interfaces.ArticleDetailView;
import org.tech.frontier.utils.LoginSession;

/**
 * 文章阅读页面的Presenter
 * 
 * @author mrsimple
 */
public class ArticleDetailPresenter extends NetBasePresenter<ArticleDetailView> {
    // 从网络上获取文章的Api
    ArticleAPI mArticleApi = new ArticleAPIImpl();
    /**
     * 
     */
    ArticleDetailDBAPI mArticleDBAPI = DbFactory.createArticleDetailModel();
    /**
     * 
     */
    final FavoriteDBAPI mFavoriteDBAPI = DbFactory.createFavoriteModel();

    AuthPresenter mAuthPresenter;

    public ArticleDetailPresenter(ArticleDetailView view) {
        mView = view;
    }

    /**
     * 获取某篇文章的内容,先从数据库获取,如果数据库没有缓存则从网络上获取
     * 
     * @param post_id
     */
    public void fetchArticleContent(final String postId) {
        mArticleDBAPI.fetchArticleContent(postId, new DataListener<ArticleDetail>() {

            @Override
            public void onComplete(ArticleDetail result) {
                // 数据库中没有则通过网络获取
                if (TextUtils.isEmpty(result.content)) {
                    fetchFromNetwork(postId);
                } else {
                    mView.fetchedData(result);
                }
            }
        });
    }

    private void fetchFromNetwork(final String postId) {
        mArticleApi.fetchArticleContent(postId, new DataListener<String>() {

            @Override
            public void onComplete(String result) {
                ArticleDetail articleDetail = new ArticleDetail(postId, result);
                mView.fetchedData(articleDetail);
                // 存储文章到数据库中
                mArticleDBAPI.saveItem(articleDetail);
            }
        }, mErrorListener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mAuthPresenter != null) {
            mAuthPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 收藏某篇文章
     * 
     * @param activity
     * @param postId
     * @param isFav
     */
    public void favorite(Activity activity, final String postId, boolean isFav) {
        final Article article = new Article(postId);
        LoginSession loginSession = LoginSession.getLoginSession();
        if (!loginSession.isLogined()) {
            mAuthPresenter = new AuthPresenter();
            mAuthPresenter.login(activity, new DataListener<UserInfo>() {

                @Override
                public void onComplete(UserInfo result) {
                    mFavoriteDBAPI.saveItem(article);
                }
            });
        } else if (!isFav) {
            // 收藏文章
            mFavoriteDBAPI.saveItem(article);
        } else {
            // 取消收藏
            mFavoriteDBAPI.unfavoriteArticle(postId);
        }
    }

    public void isFavorited(String postId) {
        if (TextUtils.isEmpty(postId)) {
            return;
        }
        mFavoriteDBAPI.isFavorited(postId, new DataListener<Boolean>() {

            @Override
            public void onComplete(Boolean result) {
                mView.isFavorited(result);
            }
        });
    }
}
