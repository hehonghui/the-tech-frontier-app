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
import android.database.sqlite.SQLiteDatabase;

import org.tech.frontier.db.FavoriteDBAPI;
import org.tech.frontier.db.cmd.Command;
import org.tech.frontier.db.cmd.Command.ArticlesCommand;
import org.tech.frontier.db.helper.DatabaseHelper;
import org.tech.frontier.entities.Article;
import org.tech.frontier.listeners.DataListener;
import org.tech.frontier.utils.LoginSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏相关的数据库操作类，包含收藏某篇文章、取消收藏、加载收藏列表、判断某篇文章是否已经被我收藏
 * 
 * @author mrsimple
 */
class FavoriteModel extends FavoriteDBAPI {

    @Override
    protected ContentValues toContentValues(Article article) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("aid", article.post_id);
        contentValues.put("uid", LoginSession.getLoginSession().getUserInfo().uid);
        return contentValues;
    }

    @Override
    public void loadDatasFromDB(DataListener<List<Article>> listener) {
        if (listener != null) {
            sDbExecutor.execute(new ArticlesCommand(listener) {
                @Override
                protected List<Article> doInBackground(SQLiteDatabase database) {
                    // 从关系表找到所有文章的id
                    List<String> articleList = findMyFavoriteArticleIds(database);
                    // 根据文章id找到文章的详细信息
                    return loadArticles(database, articleList);
                }
            });
        }
    }

    private List<Article> loadArticles(SQLiteDatabase database, List<String> articleIds) {
        List<Article> result = new ArrayList<Article>();
        for (String post_id : articleIds) {
            Article item = queryArticleWithId(database, post_id);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * 查找我关注的所有文章id
     * 
     * @param database
     * @return
     */
    private List<String> findMyFavoriteArticleIds(SQLiteDatabase database) {
        String[] columns = new String[] {
                "aid"
        };
        String[] selectionArgs = new String[] {
                LoginSession.getLoginSession().getUserInfo().uid
        };

        Cursor cursor = database.query(DatabaseHelper.TABLE_FAVORITES, columns, "uid=?",
                selectionArgs, null, null, null);

        List<String> articleList = parseArticlePostIds(cursor);
        cursor.close();
        return articleList;
    }

    /**
     * 通过文章id到articles表中查询文章的完整信息
     * 
     * @param database
     * @param postId
     * @return
     */
    private Article queryArticleWithId(SQLiteDatabase database, String postId) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_ARTICLES, null, "post_id=?",
                new String[] {
                    postId
                }, null, null, null);
        if (cursor.moveToNext()) {
            return parseArticle(cursor);
        }

        return null;
    }

    /**
     * 从Cursor中解析文章的id
     * 
     * @param cursor
     * @return
     */
    private List<String> parseArticlePostIds(Cursor cursor) {
        List<String> articlesList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            articlesList.add(cursor.getString(0));
        }
        return articlesList;
    }

    /**
     * 解析某篇文章
     * 
     * @param cursor
     * @return
     */
    private Article parseArticle(Cursor cursor) {
        Article article = new Article();
        article.post_id = cursor.getString(0);
        article.author = cursor.getString(1);
        article.title = cursor.getString(2);
        article.category = cursor.getInt(3);
        article.publishTime = cursor.getString(4);
        cursor.close();
        // 解析数据
        return article;
    }

    @Override
    public void isFavorited(final String postId, DataListener<Boolean> listener) {
        sDbExecutor.execute(new Command<Boolean>(listener) {
            @Override
            protected Boolean doInBackground(SQLiteDatabase database) {
                final String[] selectArgs = new String[] {
                        postId, LoginSession.getLoginSession().getUserInfo().uid
                };
                // 根据post id在收藏表中是否有记录
                Cursor cursor = database.rawQuery("select * from " + DatabaseHelper.TABLE_FAVORITES
                        + " where aid = ? AND uid=?", selectArgs);
                int result = cursor.getCount();
                cursor.close();
                return result > 0;
            }
        });
    }

    @Override
    public void unfavoriteArticle(final String postId) {
        deleteWithWhereArgs(" where aid=" + postId);
    }
}
