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

package com.tech.frontier.ui.frgms;

import java.util.Iterator;
import java.util.List;

import android.content.Intent;

import com.tech.frontier.adapters.ArticleAdapter;
import com.tech.frontier.adapters.ArticleWithHeaderAdapter;
import com.tech.frontier.listeners.OnItemClickListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.models.entities.Recommend;
import com.tech.frontier.presenters.ArticlePresenter;
import com.tech.frontier.ui.DetailActivity;
import com.tech.frontier.ui.interfaces.ArticleViewInterface;

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
                loadArticle(article);
            }
        });
        ((ArticleWithHeaderAdapter) mAdapter)
                .setRecommendListener(new OnItemClickListener<Recommend>() {

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
    public void showArticles(List<Article> result) {
       
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

    private void loadArticle(Article article) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        intent.putExtra("title", article.title);
        startActivity(intent);
    }

    private void loadRecommendTargetUrl(Recommend recommend) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("url", recommend.url);
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
}
