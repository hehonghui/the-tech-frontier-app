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
package com.tech.frontier.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 建立数据库和数据表
 * @author sundroid
 * May 21, 2015
 */
public class DataBaseHelper extends SQLiteOpenHelper{
	private final static String DATABASE_NAME = "tech_frontier_app";
	private static DataBaseHelper mDataBaseHelper;
	public synchronized static DataBaseHelper getInstance(Context context) {
		if (mDataBaseHelper == null) {
			mDataBaseHelper = new DataBaseHelper(context);
		}
		
		return mDataBaseHelper;
	}
	private DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists artcles (aid integer primary key autoincrement,author varchar(30),title  varchar(100),atype integer"
				+ ",save_time varchar(100))");
		db.execSQL("create table if not exists favorites (id integer primary key autoincrement,aid  integer,uid integer)");
		db.execSQL("create table if not exists users (uid integer,name  varchar(30),avatar_url varchar(200))");

	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
		
	}





}
