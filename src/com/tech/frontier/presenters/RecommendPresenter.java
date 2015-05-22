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

package com.tech.frontier.presenters;

import android.util.Log;

import com.tech.frontier.adapters.AutoSliderViewInterface;
import com.tech.frontier.db.DatabaseAPI;
import com.tech.frontier.db.impl.DbFactory;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.Recommend;
import com.tech.frontier.net.RecomendAPI;
import com.tech.frontier.net.RecomendAPIImpl;

import java.util.List;

public class RecommendPresenter {

    AutoSliderViewInterface mViewInterface;
    /**
     * 推荐网络请求API
     */
    RecomendAPI mRecomendAPI = new RecomendAPIImpl();

    DatabaseAPI<Recommend> mDatabaseAPI = DbFactory.createRecommendDBAPI();

    public RecommendPresenter(AutoSliderViewInterface viewInterface) {
        mViewInterface = viewInterface;
    }

    public void fetchRecomends() {
        mDatabaseAPI.loadDatasFromDB(new DataListener<List<Recommend>>() {

            @Override
            public void onComplete(List<Recommend> result) {
                Log.e("", "### recommends");
                mViewInterface.showRecommends(result);
                mRecomendAPI.fetchRecomends(new DataListener<List<Recommend>>() {

                    @Override
                    public void onComplete(List<Recommend> result) {
                        Log.e("", "### 已经获取header 数据 : ");
                        mViewInterface.showRecommends(result);
                        mDatabaseAPI.saveDatas(result);
                    }
                });
            }
        });

    }

}
