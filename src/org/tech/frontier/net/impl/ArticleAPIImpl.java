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

package org.tech.frontier.net.impl;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.tech.frontier.entities.Article;
import org.tech.frontier.listeners.DataListener;
import org.tech.frontier.net.ArticleAPI;
import org.tech.frontier.net.handlers.ArticlesHandler;
import org.tech.frontier.net.mgr.RequestQueueMgr;

import java.util.List;

/**
 * 文章相关的网络API实现类
 * 
 * @author mrsimple
 */
public class ArticleAPIImpl extends AbsNetwork<List<Article>, JSONArray> implements ArticleAPI {

    private int mPage = 1;

    public ArticleAPIImpl() {
        mRespHandler = new ArticlesHandler();
    }

    @Override
    public void fetchArticles(int category, final DataListener<List<Article>> listener,
            ErrorListener errorListener) {
        performRequest(1, category, listener, errorListener);
    }

    @Override
    public void loadMore(int category, DataListener<List<Article>> listener,
            ErrorListener errorListener) {
        performRequest(++mPage, category, listener, errorListener);
    }

    // TODO : 网络请求的错误处理,Presenter

    private void performRequest(final int page, int category,
            final DataListener<List<Article>> listener, ErrorListener errorListener) {
        JsonArrayRequest request = new JsonArrayRequest(
                "http://www.devtf.cn/api/v1/?type=articles&page=" + mPage + "&count=20&categoty="
                        + category,
                new Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if (listener != null) {
                            // 解析结果
                            listener.onComplete(mRespHandler.parse(jsonArray));
                        }
                    }
                }, errorListener);
        RequestQueueMgr.getRequestQueue().add(request);
    }

    @Override
    public void fetchArticleContent(String post_id, final DataListener<String> listener,
            ErrorListener errorListener) {
        StringRequest request = new StringRequest(
                "http://www.devtf.cn/api/v1/?type=article&post_id=" + post_id,
                new Listener<String>() {

                    @Override
                    public void onResponse(String html) {
                        listener.onComplete(html);
                    }

                }, errorListener);
        performRequest(request);
    }
}
