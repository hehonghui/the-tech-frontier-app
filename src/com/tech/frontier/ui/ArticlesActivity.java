
package com.tech.frontier.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;

import com.tech.frontier.R;
import com.tech.frontier.adapters.ArticleAdapter;
import com.tech.frontier.adapters.ArticleAdapter.OnItemClickListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.net.mgr.RequestQueueMgr;
import com.tech.frontier.presenters.ArticlePresenter;
import com.tech.frontier.ui.interfaces.ArticleViewInterface;
import com.tech.frontier.widgets.AutoLoadRecyclerView;
import com.tech.frontier.widgets.AutoLoadRecyclerView.OnLoadListener;

import java.util.LinkedList;
import java.util.List;

/**
 * 文章列表首页,上拉刷新获取最新的20篇文章，每次加载更多获取20篇文章.
 * 
 * @author mrsimple
 */
public class ArticlesActivity extends Activity implements ArticleViewInterface, OnRefreshListener,
        OnLoadListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    AutoLoadRecyclerView mRecyclerView;
    List<Article> mArticles = new LinkedList<Article>();
    ArticleAdapter mAdapter;
    ArticlePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        RequestQueueMgr.init(getApplicationContext());
        mPresenter = new ArticlePresenter(this);
        mPresenter.fetchArticles();
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (AutoLoadRecyclerView) findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleAdapter(mArticles);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(Article article) {
                clickArticle(article);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnLoadListener(this);
    }

    @Override
    public void onRefresh() {
        mPresenter.fetchArticles();
    }

    @Override
    public void onLoad() {
        mPresenter.loadModeArticles();
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

    private void clickArticle(Article article) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra("post_id", article.post_id);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestQueueMgr.getRequestQueue().stop();
    }

}
