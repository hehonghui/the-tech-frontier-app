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

package org.tech.frontier.presenters;

import android.util.Log;

import org.tech.frontier.adapters.HeaderRecommendAdapter;
import org.tech.frontier.adapters.ArticleWithHeaderAdapter.HeaderViewHolder;
import org.tech.frontier.db.AbsDBAPI;
import org.tech.frontier.db.models.DbFactory;
import org.tech.frontier.entities.Recommend;
import org.tech.frontier.listeners.DataListener;
import org.tech.frontier.listeners.OnItemClickListener;
import org.tech.frontier.net.RecomendAPI;
import org.tech.frontier.net.impl.RecomendAPIImpl;
import org.tech.frontier.widgets.AutoScrollViewPager;

import java.util.List;

public class RecommendPresenter {
    /**
     * 推荐网络请求API
     */
    RecomendAPI mRecomendAPI = new RecomendAPIImpl();
    /**
     * 操作推荐文章的数据库对象
     */
    AbsDBAPI<Recommend> mDatabaseAPI = DbFactory.createRecommendModel();

    /**
     * 
     */
    OnItemClickListener<Recommend> mRecommendListener;
    /**
     * 
     */
    private HeaderViewHolder mHeaderViewHolder;
    /**
     * Header View中的ViewPager Adapter
     */
    HeaderRecommendAdapter mRecommendAdapter;

    public RecommendPresenter(HeaderViewHolder headerViewHolder) {
        this.mHeaderViewHolder = headerViewHolder;
    }

    public void fetchRecomends() {
        mDatabaseAPI.loadDatasFromDB(new DataListener<List<Recommend>>() {

            @Override
            public void onComplete(List<Recommend> result) {
                Log.e("", "### recommends");
                initAutoScrollViewPager(result);
                mRecomendAPI.fetchRecomends(new DataListener<List<Recommend>>() {

                    @Override
                    public void onComplete(List<Recommend> netResult) {
                        Log.e("", "### 已经获取header 数据 : " + netResult.size());
                        initAutoScrollViewPager(netResult);
                        mDatabaseAPI.saveItems(netResult);
                    }
                });
            }
        });

    }

    /**
     * 获取到推荐文章的数据之后初始化自动滚动的ViewPager
     * 
     * @param result
     */
    private void initAutoScrollViewPager(List<Recommend> result) {
        if (result == null || result.size() == 0
                || mRecommendAdapter != null) {
            return;
        }

        AutoScrollViewPager viewPager = mHeaderViewHolder.autoScrollViewPager;
        mRecommendAdapter = new HeaderRecommendAdapter(viewPager, result);
        mRecommendAdapter.setOnItemClickListener(mRecommendListener);
        // 设置ViewPager
        viewPager.setInterval(5000);
        if (result.size() > 0) {
            viewPager.startAutoScroll();
            viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                    % result.size());
        }
        viewPager.setAdapter(mRecommendAdapter);
        mHeaderViewHolder.mIndicator.setViewPager(viewPager);
    }

    public void setRecommendClickListener(OnItemClickListener<Recommend> mRecommendListener) {
        this.mRecommendListener = mRecommendListener;
    }
}
