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

package com.tech.frontier.network;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;
import com.tech.frontier.entities.Article;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.network.handler.ArticleJsonArrayHandler;
import com.tech.frontier.network.mgr.RequestQueueMgr;

import org.json.JSONArray;

import java.util.List;

public class ArticleAPIImpl implements ArticleAPI {

    @Override
    public void fetchArticles(final DataListener<List<Article>> listener) {
        JsonArrayRequest request = new JsonArrayRequest(
                "http://www.devtf.cn/tech.php?page=1&count=20", new Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if (listener != null) {
                            ArticleJsonArrayHandler handler = new ArticleJsonArrayHandler();
                            // 解析结果
                            listener.onComplete(handler.parse(jsonArray));
                        }
                    }
                }, null);
        RequestQueueMgr.getRequestQueue().add(request);
    }

}
