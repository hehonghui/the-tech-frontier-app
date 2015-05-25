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

package org.tech.frontier.db.models;

import android.content.ContentValues;
import android.database.Cursor;

import org.tech.frontier.db.AbsDBAPI;
import org.tech.frontier.db.helper.DatabaseHelper;
import org.tech.frontier.entities.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章相关的数据库 API
 * 
 * @author mrsimple
 */
class ArticleModel extends AbsDBAPI<Article> {

    public ArticleModel() {
        super(DatabaseHelper.TABLE_ARTICLES);
    }

    @Override
    protected String loadDatasOrderBy() {
        return " publish_time DESC";
    }

    @Override
    protected List<Article> parseResult(Cursor cursor) {
        List<Article> articles = new ArrayList<Article>();
        while (cursor.moveToNext()) {
            Article item = new Article();
            item.post_id = cursor.getString(0);
            item.author = cursor.getString(1);
            item.title = cursor.getString(2);
            item.category = cursor.getInt(3);
            item.publishTime = cursor.getString(4);
            // 解析数据
            articles.add(item);
        }
        return articles;
    }

    /**
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(Article item) {
        ContentValues newValues = new ContentValues();
        newValues.put("post_id", item.post_id);
        newValues.put("author", item.author);
        newValues.put("title", item.title);
        newValues.put("category", item.category);
        newValues.put("publish_time", item.publishTime);
        return newValues;
    }
}
