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

import com.tech.frontier.db.AbsDBAPI;
import com.tech.frontier.db.impl.DbFactory;
import com.tech.frontier.entities.Recommend;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.net.RecomendAPI;
import com.tech.frontier.net.impl.RecomendAPIImpl;
import com.tech.frontier.ui.interfaces.BaseViewInterface;

import java.util.List;

public class RecommendPresenter {
    /**
     * View接口
     */
    BaseViewInterface<List<Recommend>> mViewInterface;
    /**
     * 推荐网络请求API
     */
    RecomendAPI mRecomendAPI = new RecomendAPIImpl();
    /**
     * 操作推荐文章的数据库对象
     */
    AbsDBAPI<Recommend> mDatabaseAPI = DbFactory.createRecommendDBAPI();

    public RecommendPresenter(BaseViewInterface<List<Recommend>> viewInterface) {
        mViewInterface = viewInterface;
    }

    public void fetchRecomends() {
        mDatabaseAPI.loadDatasFromDB(new DataListener<List<Recommend>>() {

            @Override
            public void onComplete(List<Recommend> result) {
                Log.e("", "### recommends");
                mViewInterface.fetchedData(result);
                mRecomendAPI.fetchRecomends(new DataListener<List<Recommend>>() {

                    @Override
                    public void onComplete(List<Recommend> netResult) {
                        Log.e("", "### 已经获取header 数据 : " + netResult.size());
                        mViewInterface.fetchedData(netResult);
                        mDatabaseAPI.saveItems(netResult);
                    }
                });
            }
        });

    }

}
