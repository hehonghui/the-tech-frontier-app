
package com.tech.frontier.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tech.frontier.R;
import com.tech.frontier.adapters.MenuAdapter;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.models.entities.MenuItem;
import com.tech.frontier.net.mgr.RequestQueueMgr;
import com.tech.frontier.ui.frgms.AboutFragment;
import com.tech.frontier.ui.frgms.ArticlesFragment;
import com.tech.frontier.ui.frgms.FavoriteFragment;
import com.tech.frontier.ui.frgms.JobsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章列表首页,上拉刷新获取最新的20篇文章，每次加载更多获取20篇文章.
 * 
 * @author mrsimple
 */
public class ArticlesActivity extends BaseActionBarActivity {

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private RecyclerView mMenuRecyclerView;
    ArticlesFragment mArticlesFragment;
    JobsFragment mJobFragment;
    FavoriteFragment mFavoriteFragment;
    AboutFragment mAboutFragment;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置Fragment Container
        setFragmentContainer(R.id.articles_container);
        initViews();

        RequestQueueMgr.init(getApplicationContext());
        context = this;
        mArticlesFragment = new ArticlesFragment(context);
        mArticlesFragment.setRetainInstance(true);
        addFragment(mArticlesFragment);
    }

    private void initViews() {
        setupToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        initMenuLayout();
    }

    private void initMenuLayout() {
        mMenuRecyclerView = (RecyclerView) findViewById(R.id.menu_recyclerview);
        mMenuRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setupMenuRecyclerView();
    }

    private void setupMenuRecyclerView() {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        menuItems.add(new MenuItem("全部", R.drawable.home));
        menuItems.add(new MenuItem("Android", R.drawable.android));
        menuItems.add(new MenuItem("iOS", R.drawable.ios));
        menuItems.add(new MenuItem("招聘信息", R.drawable.hire));
        menuItems.add(new MenuItem("收藏", R.drawable.favorite));
        menuItems.add(new MenuItem("关于", R.drawable.about));
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

    private void clickMenuItem(MenuItem item) {
        mDrawerLayout.closeDrawers();
        switch (item.iconResId) {
            case R.drawable.home: // 全部
                replaceFragment(mArticlesFragment);
                break;
            case R.drawable.android: // android
                replaceFragment(mArticlesFragment);
                mArticlesFragment.setArticleCategory(Article.ANDROID);
                mArticlesFragment.fetchDatas();
                break;
            case R.drawable.ios: // IOS
                replaceFragment(mArticlesFragment);
                mArticlesFragment.setArticleCategory(Article.iOS);
                mArticlesFragment.fetchDatas();
                break;
            case R.drawable.hire: // 招聘信息
                if (mJobFragment == null) {
                    mJobFragment = new JobsFragment();
                }
                replaceFragment(mJobFragment);
                break;
            case R.drawable.favorite: // 收藏
                if (mFavoriteFragment == null) {
                    mFavoriteFragment = new FavoriteFragment(context);
                }
                replaceFragment(mFavoriteFragment);
                break;
            case R.drawable.about: // 关于
                if (mAboutFragment == null) {
                    mAboutFragment = new AboutFragment();
                }
                replaceFragment(mAboutFragment);
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
