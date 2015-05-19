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

import android.annotation.SuppressLint;

import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.ArticleModel;
import com.tech.frontier.models.ArticleModelImpl;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.net.ArticleAPI;
import com.tech.frontier.net.ArticleAPIImpl;
import com.tech.frontier.ui.interfaces.ArticleViewInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Presenter角色，作为View和Model的中间人，中介模式
 * 
 * @author mrsimple
 */
public class ArticlePresenter {
    // View的接口,被Presenter调用，用于像View传递数据,代表了View角色
    ArticleViewInterface mArticleView;
    // 文章数据的Model,也就是Model角色
    ArticleModel mArticleModel = new ArticleModelImpl();
    // 从网络上获取文章的Api
    ArticleAPI mArticleApi = new ArticleAPIImpl();
    /**
     * 文章列表
     */
    List<Article> mArticles = new ArrayList<Article>();

    /** 文章的时间格式 */
    private static String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private boolean isNoMoreArticles = false;

    public ArticlePresenter(ArticleViewInterface viewInterface) {
        mArticleView = viewInterface;
    }

    // 获取文章
    public void fetchArticles() {
        mArticleView.showLoading();
        mArticleApi.fetchArticles(new DataListener<List<Article>>() {

            @Override
            public void onComplete(List<Article> result) {
                fetchDataFinished(result);
            }
        });
    }

    public void loadModeArticles() {
        if (isNoMoreArticles) {
            return;
        }
        mArticleView.showLoading();
        mArticleApi.loadMode(new DataListener<List<Article>>() {

            @Override
            public void onComplete(List<Article> result) {
                fetchDataFinished(result);
                if (result.size() == 0) {
                    isNoMoreArticles = true;
                }
            }
        });
    }

    private void fetchDataFinished(List<Article> result) {
        // 移除已经存在的数据
        mArticles.removeAll(result);
        // 添加心数据
        mArticles.addAll(result);
        // 排序
        sortArticles(mArticles);
        mArticleView.showArticles(mArticles);
        mArticleView.hideLoading();
        // 存储到数据库
        mArticleModel.saveArticles(result);
    }

    private void sortArticles(List<Article> articles) {
        Collections.sort(articles, mArticleComparator);
    }

    public void loadArticlesFromDB() {
        mArticleModel.loadArticlesFromCache(new DataListener<List<Article>>() {

            @Override
            public void onComplete(List<Article> result) {
                mArticleView.showArticles(result);
            }
        });
    }

    /**
     * 根据文章时间进行降序排序
     */
    @SuppressLint("SimpleDateFormat")
    Comparator<Article> mArticleComparator = new Comparator<Article>() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);

        @Override
        public int compare(Article lhs, Article rhs) {
            try {

                long lTime = df.parse(lhs.publishTime).getTime();
                long rTime = df.parse(rhs.publishTime).getTime();
                return (int) Math.abs(lTime - rTime);
            } catch (Exception e) {
            }
            return 0;
        }

    };

}
