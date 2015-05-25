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

package org.tech.frontier.ui.frgms;

import org.tech.frontier.adapters.ArticleAdapter;
import org.tech.frontier.entities.Article;
import org.tech.frontier.listeners.OnItemClickListener;
import org.tech.frontier.presenters.FavoritePresenter;

/**
 * 文章收藏页面
 * 
 * @author mrsimple
 */
public class FavoriteFragment extends ArticlesFragment {

    FavoritePresenter mFavoritePresenter;

    @Override
    protected void initAdapter() {
        mAdapter = new ArticleAdapter(mDataSet);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {

            @Override
            public void onClick(Article article) {
                loadArticle(article);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initPresenter() {
        mFavoritePresenter = new FavoritePresenter(this);
    }

    @Override
    public void fetchDatas() {
        mFavoritePresenter.loadFavorites();
    }

    @Override
    public void onRefresh() {
        hideLoading();
    }

}
