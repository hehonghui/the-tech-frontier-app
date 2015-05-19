
package com.tech.frontier.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tech.frontier.R;
import com.tech.frontier.adapters.ArticleAdapter;
import com.tech.frontier.adapters.ArticleAdapter.OnItemClickListener;
import com.tech.frontier.adapters.MenuAdapter;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.models.entities.MenuItem;
import com.tech.frontier.net.mgr.RequestQueueMgr;
import com.tech.frontier.presenters.ArticlePresenter;
import com.tech.frontier.ui.interfaces.ArticleViewInterface;
import com.tech.frontier.widgets.AutoLoadRecyclerView;
import com.tech.frontier.widgets.AutoLoadRecyclerView.OnLoadListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 文章列表首页,上拉刷新获取最新的20篇文章，每次加载更多获取20篇文章.
 * 
 * @author mrsimple
 */
public class ArticlesActivity extends BaseActionBarActivity implements
        ArticleViewInterface, OnRefreshListener, OnLoadListener {

    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AutoLoadRecyclerView mRecyclerView;
    private List<Article> mArticles = new LinkedList<Article>();
    private ArticleAdapter mAdapter;
    private ArticlePresenter mPresenter;
    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView mMenuRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        RequestQueueMgr.init(getApplicationContext());
        mPresenter = new ArticlePresenter(this);
        mPresenter.fetchArticles();
        mSwipeRefreshLayout.setRefreshing(true);
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
                loadArticle(article.post_id);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setOnLoadListener(this);
        setupToolbar();
        /* findView */
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        initMenuLayout();
    }

    private void initMenuLayout() {
        // mUserImageView = (ImageView) findViewById(R.id.user_icon_imageview);
        // mUsrNameTextView = (TextView) findViewById(R.id.username_tv);

        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setupMenuRecyclerView();
    }

    private void setupMenuRecyclerView() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem("Android", R.drawable.android));
        menuItems.add(new MenuItem("iOS", R.drawable.ios));
        menuItems.add(new MenuItem("招聘信息", R.drawable.hire));
        menuItems.add(new MenuItem("收藏", R.drawable.favorite));
        menuItems.add(new MenuItem("关于", R.drawable.about));
        menuItems.add(new MenuItem("设置", R.drawable.setting));
        menuItems.add(new MenuItem("退出", R.drawable.exit));
        MenuAdapter menuAdapter = new MenuAdapter(menuItems);
        menuAdapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {

            @Override
            public void onClick(MenuItem item) {
                clickMenuItem(item);
            }
        });
        mMenuRecyclerView.setAdapter(menuAdapter);
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

    private void loadArticle(String postId) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra("post_id", postId);
        startActivity(intent);
    }

    private void clickMenuItem(MenuItem item) {
        Toast.makeText(getApplicationContext(), item.text, Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();

        switch (item.iconResId) {
            case R.drawable.android:
                break;
            case R.drawable.ios:

                break;
            case R.drawable.hire:
                loadArticle("21");
                break;
            case R.drawable.favorite:

                break;
            case R.drawable.about:
                loadArticle("19");
                break;
            case R.drawable.setting:

                break;

            case R.drawable.exit:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestQueueMgr.getRequestQueue().stop();
    }

}
