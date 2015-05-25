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

package org.tech.frontier.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author mrsimple
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "tech_frontier.db";
    static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ARTICLES_TABLE_SQL);
        db.execSQL(CREATE_ARTICLE_CONTENT_TABLE_SQL);
        db.execSQL(CREATE_FAVORITES_TABLE_SQL);
        db.execSQL(CREATE_JOBS_TABLE_SQL);
        db.execSQL(CREATE_RECOMMENDS_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_ARTICLES);
        db.execSQL("DROP TABLE " + TABLE_ARTICLE_CONTENT);
        db.execSQL("DROP TABLE " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE " + TABLE_JOBS);
        db.execSQL("DROP TABLE " + TABLE_RECOMMENDS);
        onCreate(db);
    }

    public static final String TABLE_ARTICLES = "articles";
    public static final String TABLE_ARTICLE_CONTENT = "article_content";
    public static final String TABLE_FAVORITES = "favorites";
    public static final String TABLE_JOBS = "jobs";
    public static final String TABLE_RECOMMENDS = "recommends";

    private static final String CREATE_ARTICLES_TABLE_SQL = "CREATE TABLE articles (  "
            + " post_id INTEGER PRIMARY KEY UNIQUE, "
            + " author VARCHAR(30) NOT NULL ,"
            + " title VARCHAR(50) NOT NULL,"
            + " category INTEGER ,"
            + " publish_time VARCHAR(50) "
            + " )";

    private static final String CREATE_ARTICLE_CONTENT_TABLE_SQL = "CREATE TABLE article_content (  "
            + " post_id INTEGER PRIMARY KEY UNIQUE, "
            + " content TEXT NOT NULL "
            + " )";

    private final static String CREATE_FAVORITES_TABLE_SQL = "CREATE TABLE favorites (  "
            + " aid INTEGER PRIMARY KEY UNIQUE, "
            + " uid INTEGER , UNIQUE(aid,uid)"
            + " )";

    private final static String CREATE_JOBS_TABLE_SQL = "CREATE TABLE jobs (  "
            + " company varchar(30) NOT NULL, "
            + " job varchar(30) NOT NULL, "
            + " job_desc varchar(50), "
            + " email varchar(30) NOT NULL,"
            + " url varchar(100) NOT NULL,"
            + "UNIQUE(company,job)"
            + " )";

    private final static String CREATE_RECOMMENDS_TABLE_SQL = "CREATE TABLE recommends (  "
            + " title varchar(50) NOT NULL UNIQUE, "
            + " url varchar(100) NOT NULL, "
            + " img_url varchar(200) "
            + " )";

}
