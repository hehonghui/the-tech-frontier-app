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

package org.tech.frontier.db;

import org.tech.frontier.db.helper.DatabaseHelper;
import org.tech.frontier.entities.Article;
import org.tech.frontier.listeners.DataListener;

/**
 * 收藏文章数据库操作接口
 * 
 * @author mrsimple
 */
public abstract class FavoriteDBAPI extends AbsDBAPI<Article> {
    public FavoriteDBAPI() {
        super(DatabaseHelper.TABLE_FAVORITES);
    }

    /**
     * 对某篇文章取消收藏
     * 
     * @param postId
     */
    public abstract void unfavoriteArticle(String postId);

    /**
     * 判断对某篇文章是否已关注
     * 
     * @param postId
     * @param listener
     */
    public abstract void isFavorited(String postId, DataListener<Boolean> listener);
}
