package com.tech.frontier.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tech.frontier.R;
import com.tech.frontier.adapters.MenuAdapter;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.models.entities.MenuItem;
import com.tech.frontier.models.entities.UserInfo;
import com.tech.frontier.net.LruImageCache;
import com.tech.frontier.net.UserAPI;
import com.tech.frontier.net.UserAPIImpl;
import com.tech.frontier.net.mgr.RequestQueueMgr;
import com.tech.frontier.ui.frgms.AboutFragment;
import com.tech.frontier.ui.frgms.ArticlesFragment;
import com.tech.frontier.ui.frgms.FavoriteFragment;
import com.tech.frontier.ui.frgms.JobsFragment;
import com.tech.frontier.utils.Constants;
import com.tech.frontier.utils.SharePreferUtil;

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

    private AuthInfo mAuthInfo;

	private String uid = "";
	private String token = "";
	UserAPI userAPI = new UserAPIImpl();
	private NetworkImageView user_icon_imageview;

	private TextView username_tv;

	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;

	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 设置Fragment Container
        setFragmentContainer(R.id.articles_container);
        initViews();
        
        initWeiBo();
        
        SharePreferUtil.init(getApplicationContext());
        
        mArticlesFragment = new ArticlesFragment();
        mArticlesFragment.setRetainInstance(true);
        addFragment(mArticlesFragment);
    }

    private void initWeiBo() {
    	mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		mSsoHandler = new SsoHandler(ArticlesActivity.this, mAuthInfo);
		
	}

	private void initViews() {
        setupToolbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        user_icon_imageview = (NetworkImageView) findViewById(R.id.user_icon_imageview);
		user_icon_imageview.setDefaultImageResId(R.drawable.user_default);
	
		username_tv = (TextView) findViewById(R.id.username_tv);
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
        menuItems.add(new MenuItem("Android", R.drawable.android_icon));
        menuItems.add(new MenuItem("iOS", R.drawable.ios_icon));
        menuItems.add(new MenuItem("招聘信息", R.drawable.hire_icon));
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
                mArticlesFragment.setArticleCategory(Article.ALL);
                mArticlesFragment.fetchDatas();
                replaceFragment(mArticlesFragment);
                break;
            case R.drawable.android_icon: // android
                replaceFragment(mArticlesFragment);
                mArticlesFragment.setArticleCategory(Article.ANDROID);
                mArticlesFragment.fetchDatas();
                break;
            case R.drawable.ios_icon: // IOS
                replaceFragment(mArticlesFragment);
                mArticlesFragment.setArticleCategory(Article.iOS);
                mArticlesFragment.fetchDatas();
                break;
            case R.drawable.hire_icon: // 招聘信息
                if (mJobFragment == null) {
                    mJobFragment = new JobsFragment();
                }
                replaceFragment(mJobFragment);
                break;
            case R.drawable.favorite: // 收藏
                if (mFavoriteFragment == null) {
                    mFavoriteFragment = new FavoriteFragment();
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
    public void userIconClick(View view) {
		mSsoHandler.authorize(new AuthListener());

	}
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestQueueMgr.getRequestQueue().stop();
    }

    
    
    /**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token

			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				uid = mAccessToken.getUid();
				token = mAccessToken.getToken();

				Log.i("RESULT", mAccessToken.toString());

				userAPI.fetchUserInfo(uid, token, new DataListener<UserInfo>() {

					@Override
					public void onComplete(UserInfo result) {

						fetchDataFinished(result);
					}
				});

			} else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");

			}
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onWeiboException(WeiboException e) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// SSO 授权回调
		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
		if (mSsoHandler != null) {
			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void fetchDataFinished(UserInfo result) {
		LruImageCache lruImageCache = LruImageCache.instance();

		ImageLoader imageLoader = new ImageLoader(
				RequestQueueMgr.getRequestQueue(), lruImageCache);
		user_icon_imageview.setImageUrl(result.profile_image_url, imageLoader);
		username_tv.setText(result.name);

		SharePreferUtil.addUserInfo(result);
	}
}