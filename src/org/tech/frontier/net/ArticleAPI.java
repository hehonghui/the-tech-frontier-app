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

package org.tech.frontier.net;

import com.android.volley.Response.ErrorListener;

import org.tech.frontier.entities.Article;
import org.tech.frontier.listeners.DataListener;

import java.util.List;

/**
 * 文章相关的网络API接口
 * 
 * @author mrsimple
 */
public interface ArticleAPI {
    /**
     * 根据分类获取文章
     * 
     * @param category 文章分类,2代表Android, 3代表iOS，其他数值为获取所有文章
     * @param listener 监听器
     */
    public void fetchArticles(int category, DataListener<List<Article>> listener,
            ErrorListener errorListener);

    /**
     * 获取某篇文章的内容
     * 
     * @param post_id 文章id
     * @param listener
     */
    public void fetchArticleContent(String post_id, DataListener<String> listener,
            ErrorListener errorListener);

    /**
     * 加载更多文章
     * 
     * @param category 分类
     * @param listener
     */
    public void loadMore(int category, DataListener<List<Article>> listener,
            ErrorListener errorListener);
}
