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

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据库实例管理类,引用技术管理数据库实例,当计数为0时关闭数据库
 * 
 * @author mrsimple
 */
public class DatabaseMgr {
    private static SQLiteDatabase sDatabase;
    private static AtomicInteger sDbRef = new AtomicInteger(0);
    static DatabaseHelper sHelper;

    /**
     * 初始化数据库Helper
     * 
     * @param context
     */
    public static void init(Context context) {
        if (sDatabase == null && context != null) {
            synchronized (DatabaseMgr.class) {
                sHelper = new DatabaseHelper(context.getApplicationContext());
                sDatabase = sHelper.getWritableDatabase();
            }
        }
    }

    public static void releaseDatabase() {
        if (sDbRef.decrementAndGet() == 0) {
            closeDatabase();
        }
    }

    public static void closeDatabase() {
        if (sDatabase != null && sDatabase.isOpen()) {
            sDatabase.close();
            sDatabase = null;
        }
    }

    public static SQLiteDatabase getDatabase() {
        if (!sDatabase.isOpen()) {
            sDatabase = sHelper.getWritableDatabase();
        }
        sDbRef.incrementAndGet();
        return sDatabase;
    }

    public static void beginTransaction() {
        if (sDatabase != null) {
            sDatabase.beginTransaction();
        }
    }

    public static void setTransactionSuccess() {
        if (sDatabase != null) {
            sDatabase.setTransactionSuccessful();
        }
    }

    public static void endTransaction() {
        if (sDatabase != null) {
            sDatabase.endTransaction();
        }
    }
}
