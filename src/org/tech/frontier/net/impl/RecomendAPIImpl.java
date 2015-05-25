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

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.tech.frontier.entities.Recommend;
import org.tech.frontier.listeners.DataListener;
import org.tech.frontier.net.RecomendAPI;
import org.tech.frontier.net.handlers.RecommendHandler;

import java.util.List;

/**
 * 推荐API实现类
 * 
 * @author mrsimple
 */
public class RecomendAPIImpl extends AbsNetwork<List<Recommend>, JSONArray> implements RecomendAPI {

    public RecomendAPIImpl() {
        mRespHandler = new RecommendHandler();
    }

    @Override
    public void fetchRecomends(final DataListener<List<Recommend>> listener) {
        JsonArrayRequest request = new JsonArrayRequest(
                "http://www.devtf.cn/api/v1/?type=recommends",
                new Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray jsonArray) {
                        if (listener != null) {
                            // 解析结果
                            listener.onComplete(mRespHandler.parse(jsonArray));
                        }
                    }
                }, null);
        performRequest(request);
    }

}
