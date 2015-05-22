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
import com.tech.frontier.models.entities.Recommend;

import java.util.ArrayList;
import java.util.List;

public class RecommendDBAPIImpl extends AbsDBAPI<Recommend> {

    public RecommendDBAPIImpl() {
        super(DatabaseHelper.TABLE_RECOMMENDS);
    }

    @Override
    public void saveDatas(List<Recommend> datas) {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        DatabaseMgr.beginTransaction();
        for (Recommend item : datas) {
            database.insertWithOnConflict(mTableName, null,
                    toContentValues(item),
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
        DatabaseMgr.setTransactionSuccess();
        DatabaseMgr.endTransaction();
        DatabaseMgr.releaseDatabase();
    }

    @Override
    public void loadDatasFromDB(DataListener<List<Recommend>> listener) {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        DatabaseMgr.beginTransaction();
        Cursor cursor = database.query(mTableName, null, null, null,
                null, null, null);
        listener.onComplete(queryResult(cursor));
        cursor.close();
        DatabaseMgr.setTransactionSuccess();
        DatabaseMgr.endTransaction();
        DatabaseMgr.releaseDatabase();
    }

    private List<Recommend> queryResult(Cursor cursor) {
        List<Recommend> recommends = new ArrayList<Recommend>();
        while (cursor.moveToNext()) {

            String title = cursor.getString(0);
            String url = cursor.getString(1);
            String imgUrl = cursor.getString(2);
            // 解析数据
            recommends.add(new Recommend(title, url, imgUrl));
        }
        return recommends;
    }

    /**
     * @param item
     * @return
     */
    private ContentValues toContentValues(Recommend item) {
        ContentValues newValues = new ContentValues();
        newValues.put("title", item.title);
        newValues.put("url", item.url);
        newValues.put("img_url", item.imgUrl);
        return newValues;
    }

}
