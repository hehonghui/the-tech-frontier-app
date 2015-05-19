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

package com.tech.frontier.net;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.net.handlers.ArticlesHandler;
import com.tech.frontier.net.mgr.RequestQueueMgr;

import org.json.JSONArray;

import java.util.List;

public class ArticleAPIImpl implements ArticleAPI {

    private int mPage = 0;
    ArticlesHandler mJsonHandler = new ArticlesHandler();

    @Override
    public void fetchArticles(final DataListener<List<Article>> listener) {
        performRequest(0, listener);
    }

    @Override
    public void loadMode(DataListener<List<Article>> listener) {
        performRequest(++mPage, listener);
    }

    private void performRequest(final int page, final DataListener<List<Article>> listener) {
        JsonArrayRequest request = new JsonArrayRequest(
                "http://www.devtf.cn/articles.php?page=" + page + "&count=50",
                new Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if (listener != null) {
                            // 解析结果
                            listener.onComplete(mJsonHandler.parse(jsonArray));
                        }
                    }
                }, null);
        RequestQueueMgr.getRequestQueue().add(request);
    }
}
