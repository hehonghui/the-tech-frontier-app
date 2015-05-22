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

package com.tech.frontier.db.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tech.frontier.db.cmd.Command;
import com.tech.frontier.db.helper.DatabaseHelper;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章相关的数据库 API
 * 
 * @author mrsimple
 */
class ArticleDBAPIImpl extends PresentableDBAPI<Article> {

    public ArticleDBAPIImpl() {
        super(DatabaseHelper.TABLE_ARTICLES);
    }

    @Override
    public void saveDatas(final List<Article> datas) {
        sDbExecutor.execute(new Command<Void>() {
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                for (Article article : datas) {
                    database.insertWithOnConflict(mTableName, null,
                            toContentValues(article),
                            SQLiteDatabase.CONFLICT_REPLACE);
                }
                return null;
            }
        });
    }

    @Override
    public void loadDatasFromDB(final DataListener<List<Article>> listener) {
        sDbExecutor.execute(new Command<List<Article>>(listener) {
            @Override
            protected List<Article> doInBackground(SQLiteDatabase database) {
                Cursor cursor = database.query(mTableName, null, null, null, null, null,
                        " publish_time DESC");
                List<Article> result = queryResult(cursor);
                cursor.close();
                return result;
            }
        });
    }

    private List<Article> queryResult(Cursor cursor) {
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
    private ContentValues toContentValues(Article item) {
        ContentValues newValues = new ContentValues();
        newValues.put("post_id", item.post_id);
        newValues.put("author", item.author);
        newValues.put("title", item.title);
        newValues.put("category", item.category);
        newValues.put("publish_time", item.publishTime);
        return newValues;
    }
}
