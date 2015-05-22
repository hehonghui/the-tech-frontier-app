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

package com.tech.frontier.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tech.frontier.db.helper.DatabaseHelper;
import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.utils.LoginSession;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDBAPIImpl implements FavoriteDBAPI {

    @Override
    public void saveFavoriteArticles(String postId) {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        database.insertWithOnConflict(DatabaseHelper.TABLE_FAVORITES, null,
                toContentValues(postId),
                SQLiteDatabase.CONFLICT_REPLACE);
        DatabaseMgr.releaseDatabase();
    }

    private ContentValues toContentValues(String postId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("aid", postId);
        contentValues.put("uid", LoginSession.getLoginSession().getUserInfo().uid);
        return contentValues;
    }

    @Override
    public void loadFavoriteArticles(DataListener<List<Article>> listener) {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        String[] columns = new String[] {
                "aid"
        };
        String[] selectionArgs = new String[] {
                LoginSession.getLoginSession().getUserInfo().uid
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, columns, "uid=?",
                selectionArgs, null, null, null);

        List<String> articleList = queryArticlePostIds(cursor);
        cursor.close();

        List<Article> result = new ArrayList<Article>();
        for (String post_id : articleList) {
            // cursor = database.query(DatabaseHelper.TABLE_ARTICLES, null,
            // "post_id=?", new String[] {
            // post_id
            // }, null, null, null);
            // if (cursor.moveToNext()) {
            // result.add(queryArticle(cursor));
            // }
            Article item = queryArticleWithId(database, cursor, post_id);
            if (item != null) {
                result.add(item);
            }
        }

        if (listener != null) {
            listener.onComplete(result);
        }
        cursor.close();
    }

    private Article queryArticleWithId(SQLiteDatabase database, Cursor cursor, String postId) {
        cursor = database.query(DatabaseHelper.TABLE_ARTICLES, null, "post_id=?", new String[] {
                postId
        }, null, null, null);
        if (cursor.moveToNext()) {
            return queryArticle(cursor);
        }

        return null;
    }

    private List<String> queryArticlePostIds(Cursor cursor) {
        List<String> articlesList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            articlesList.add(cursor.getString(0));
        }
        return articlesList;
    }

    private Article queryArticle(Cursor cursor) {
        Article article = new Article();
        article.post_id = cursor.getString(0);
        article.author = cursor.getString(1);
        article.title = cursor.getString(2);
        article.category = cursor.getInt(3);
        article.publishTime = cursor.getString(4);
        // 解析数据
        return article;
    }
}
