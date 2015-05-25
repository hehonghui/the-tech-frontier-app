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

import android.content.Intent;

import com.android.volley.VolleyError;

import org.tech.frontier.adapters.ArticleAdapter;
import org.tech.frontier.adapters.ArticleWithHeaderAdapter;
import org.tech.frontier.entities.Article;
import org.tech.frontier.entities.Recommend;
import org.tech.frontier.listeners.OnItemClickListener;
import org.tech.frontier.presenters.ArticlePresenter;
import org.tech.frontier.ui.DetailActivity;
import org.tech.frontier.ui.interfaces.ArticleViewInterface;

import java.util.Iterator;
import java.util.List;

/**
 * 文章列表主界面,包含自动滚动广告栏、文章列表
 * 
 * @author mrsimple
 */
public class ArticlesFragment extends RecyclerViewFragment<Article> implements
        ArticleViewInterface {
    protected ArticleAdapter mAdapter;
    private ArticlePresenter mPresenter;
    protected int mCategory = Article.ALL;

    @Override
    protected void initAdapter() {
        mAdapter = new ArticleWithHeaderAdapter(mDataSet);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Article>() {

            @Override
            public void onClick(Article article) {
                if (article != null) {
                    loadArticle(article);
                }
            }
        });
        ((ArticleWithHeaderAdapter) mAdapter)
                .setRecommendClickListener(new OnItemClickListener<Recommend>() {

                    @Override
                    public void onClick(Recommend item) {
                        loadRecommendTargetUrl(item);
                    }
                });
        // 设置Adapter
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ArticlePresenter(this);
        mPresenter.loadArticlesFromDB();

    }

    public void setArticleCategory(int category) {
        mCategory = category;
    }

    private void filterArticleByCategory() {
        if ((mCategory == Article.ANDROID
                || mCategory == Article.iOS)
                && mDataSet.size() > 0) {
            Iterator<Article> it = mDataSet.iterator();
            while (it.hasNext()) {
                Article item = it.next();
                if (item.category != mCategory) {
                    it.remove();
                }
            }
        }
    }

    @Override
    public void fetchDatas() {
        filterArticleByCategory();
        mPresenter.fetchArticles(mCategory);
    }

    @Override
    public void fetchedData(List<Article> result) {
        mDataSet.clear();
        mDataSet.addAll(result);
        filterArticleByCategory();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setLoading(false);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    protected void loadArticle(Article article) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        intent.putExtra("title", article.title);
        startActivity(intent);
    }

    private void loadRecommendTargetUrl(Recommend recommend) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("url", recommend.url);
        intent.putExtra("title", recommend.title);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPresenter.fetchArticles(mCategory);
    }

    @Override
    public void onLoad() {
        mPresenter.loadModeArticles(mCategory);
    }

    @Override
    public void onError(VolleyError error) {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
