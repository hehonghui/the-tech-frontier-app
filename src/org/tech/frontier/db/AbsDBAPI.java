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

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.tech.frontier.db.cmd.Command;
import org.tech.frontier.db.cmd.Command.NoReturnCmd;
import org.tech.frontier.db.engine.DbExecutor;
import org.tech.frontier.listeners.DataListener;

import java.util.List;

/**
 * @author mrsimple
 * @param <T>
 */
public abstract class AbsDBAPI<T> {
    /**
     * 数据库执行引擎
     */
    protected static DbExecutor sDbExecutor = DbExecutor.getExecutor();
    /**
     * 表名
     */
    protected String mTableName;

    public AbsDBAPI(String table) {
        mTableName = table;
    }

    /**
     * 保存数据
     * 
     * @param item
     */
    public void saveItem(final T item) {
        sDbExecutor.execute(new NoReturnCmd() {
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                database.insertWithOnConflict(mTableName, null, toContentValues(item),
                        SQLiteDatabase.CONFLICT_REPLACE);
                return null;
            }
        });
    }

    protected ContentValues toContentValues(T item) {
        return null;
    }

    /**
     * 保存数据到数据库
     * 
     * @param articles
     */
    public void saveItems(List<T> datas) {
        for (T item : datas) {
            saveItem(item);
        }
    }

    /**
     * 加载所有缓存
     * 
     * @param listener
     */
    public void loadDatasFromDB(DataListener<List<T>> listener) {
        sDbExecutor.execute(new Command<List<T>>(listener) {

            @Override
            protected List<T> doInBackground(SQLiteDatabase database) {
                Cursor cursor = database.query(mTableName, null, null, null,
                        null, null, loadDatasOrderBy());
                List<T> result = parseResult(cursor);
                cursor.close();
                return result;
            }
        });
    }

    protected String loadDatasOrderBy() {
        return "";
    }

    /**
     * 从Cursor中解析数据
     * 
     * @param cursor
     * @return
     */
    protected List<T> parseResult(Cursor cursor) {
        return null;
    }

    /**
     * 删除符合特定条件的数据
     */
    public void deleteWithWhereArgs(final String whereArgs) {
        sDbExecutor.execute(new Command<Void>() {
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                database.execSQL("delete from " + mTableName + whereArgs);
                return null;
            }
        });
    }

    public void deleteAll() {
        sDbExecutor.execute(new Command<Void>() {
            @Override
            protected Void doInBackground(SQLiteDatabase database) {
                database.execSQL("delete from " + mTableName);
                return null;
            }
        });
    }
}
