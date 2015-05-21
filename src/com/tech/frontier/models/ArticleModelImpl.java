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

package com.tech.frontier.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tech.frontier.listeners.DataListener;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.sqlite.DataBaseHelper;

/**
 * 数据存储在内存,TODO : 修改为存储到数据库
 * 
 * @author mrsimple
 */
public class ArticleModelImpl implements ArticleModel {
    List<Article> mCachedArticles = new LinkedList<Article>();

    @Override
    public void saveArticles(List<Article> articles,Context context) {
		SQLiteDatabase sqLiteDatabase = DataBaseHelper.getInstance(context)
				.getWritableDatabase();
		for (Article article : articles) {
			ContentValues contentValues = new ContentValues();
			contentValues.put("title",article.title);
			contentValues.put("author",article.author);
			contentValues.put("atype",article.category);
			contentValues.put("save_time",article.publishTime);
			Log.i("SAVE", "======"+article.toString());
			sqLiteDatabase.insert("artcles", null, contentValues);
		}
		sqLiteDatabase.close();
		

    }

    @Override
    public void loadArticlesFromCache(DataListener<List<Article>> listener,Context context) {
    	mCachedArticles = getArticles(context);
    	
    	listener.onComplete(mCachedArticles);
    }

    
    

	@Override
	public void saveArticle(Article article, Context context) {
		SQLiteDatabase sqLiteDatabase = DataBaseHelper.getInstance(context)
				.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put("title",article.title);
		contentValues.put("author",article.author);
		contentValues.put("atype",article.category);
		contentValues.put("save_time",article.publishTime);
		Log.i("SAVEA", "======"+article.toString());
		if (sqLiteDatabase!=null) {
			sqLiteDatabase.insert("artcles", null, contentValues);
			sqLiteDatabase.close();
		}
	}

	@Override
	public List<Article> getArticles(Context context) {
		List<Article> articles = new ArrayList<Article>();
		SQLiteDatabase sqLiteDatabase = DataBaseHelper.getInstance(context)
				.getReadableDatabase();

		if (sqLiteDatabase != null) {
			Cursor cursor = sqLiteDatabase.query("artcles", null, null,
					null, null, null, null);
			while (cursor.moveToNext()) {
				Article article = new Article();
				article.author = cursor.getString(cursor
						.getColumnIndex("author"));
				article.category = cursor.getInt(cursor
						.getColumnIndex("atype"));
				article.publishTime =  cursor.getString(cursor
						.getColumnIndex("save_time"));
				article.title =  cursor.getString(cursor
						.getColumnIndex("title"));
				Log.i("READ", "======"+article.toString());
				articles.add(article);
			}
			sqLiteDatabase.close();
			
		}
		return articles;

	}

}
