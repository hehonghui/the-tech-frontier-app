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

import com.tech.frontier.ArticleViewInterface;
import com.tech.frontier.entities.Article;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.ArticleModel;
import com.tech.frontier.models.ArticleModelImpl;
import com.tech.frontier.network.ArticleAPI;
import com.tech.frontier.network.ArticleAPIImpl;

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

    public ArticlePresenter(ArticleViewInterface viewInterface) {
        mArticleView = viewInterface;
    }

    // 获取文章
    public void fetchArticles() {
        mArticleView.showLoading();
        mArticleApi.fetchArticles(new DataListener<List<Article>>() {

            @Override
            public void onComplete(List<Article> result) {
                mArticleView.showArticles(result);
                mArticleView.hideLoading();
                // 存储到数据库
                mArticleModel.saveArticles(result);
            }
        });
    }

    public void loadArticlesFromDB() {
        mArticleModel.loadArticlesFromCache(new DataListener<List<Article>>() {

            @Override
            public void onComplete(List<Article> result) {
                mArticleView.showArticles(result);
            }
        });
    }
}
