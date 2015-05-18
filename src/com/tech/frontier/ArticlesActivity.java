
package com.tech.frontier;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.tech.frontier.adapters.ArticleAdapter;
import com.tech.frontier.adapters.ArticleAdapter.OnItemClickListener;
import com.tech.frontier.entities.Article;
import com.tech.frontier.network.mgr.RequestQueueMgr;
import com.tech.frontier.presenters.ArticlePresenter;

import java.util.LinkedList;
import java.util.List;

public class ArticlesActivity extends Activity implements ArticleViewInterface {

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
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
        mRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleAdapter(mArticles);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onClick(Article article) {
                clickArticle(article);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
    }

    @Override
    public void showArticles(List<Article> result) {
        mArticles.addAll(result);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
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
