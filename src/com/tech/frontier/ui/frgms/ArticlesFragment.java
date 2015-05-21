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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.frontier.R;
import com.tech.frontier.adapters.ArticleAdapter;
import com.tech.frontier.adapters.ArticleAdapter.OnItemClickListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.presenters.ArticlePresenter;
import com.tech.frontier.ui.ArticleDetailActivity;
import com.tech.frontier.ui.interfaces.ArticleViewInterface;
import com.tech.frontier.widgets.AutoLoadRecyclerView;
import com.tech.frontier.widgets.AutoLoadRecyclerView.OnLoadListener;

import java.util.LinkedList;
import java.util.List;

public class ArticlesFragment extends Fragment implements OnRefreshListener, ArticleViewInterface,
        OnLoadListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private List<Article> mArticles = new LinkedList<Article>();
    private ArticleAdapter mAdapter;
    private ArticlePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (AutoLoadRecyclerView) rootView.findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                .getApplicationContext()));
        mAdapter = new ArticleAdapter(mArticles);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(Article article) {
                loadArticle(article);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setOnLoadListener(this);

        mPresenter = new ArticlePresenter(this);
        mPresenter.fetchArticles();

        mSwipeRefreshLayout.setRefreshing(true);
        return rootView;
    }


    @Override
    public void showArticles(List<Article> result) {
        mArticles.clear();
        mArticles.addAll(result);
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
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        intent.putExtra("title", article.title);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mPresenter.fetchArticles();
    }

    @Override
    public void onLoad() {
        mPresenter.loadModeArticles();
    }
}
