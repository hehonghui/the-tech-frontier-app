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

package org.tech.frontier.db.cmd;

import android.database.sqlite.SQLiteDatabase;

import org.tech.frontier.db.helper.DatabaseMgr;
import org.tech.frontier.entities.Article;
import org.tech.frontier.entities.Job;
import org.tech.frontier.entities.Recommend;
import org.tech.frontier.listeners.DataListener;

import java.util.List;

public abstract class Command<T> {

    public DataListener<T> dataListener;

    public Command() {
    }

    public Command(DataListener<T> listener) {
        dataListener = listener;
    }

    public final T execute() {
        SQLiteDatabase database = DatabaseMgr.getDatabase();
        DatabaseMgr.beginTransaction();
        T result = doInBackground(database);
        DatabaseMgr.setTransactionSuccess();
        DatabaseMgr.endTransaction();
        database.releaseReference();
        return result;
    }

    protected abstract T doInBackground(SQLiteDatabase database);

    /**
     * 无返回值的数据库命令
     * 
     * @author mrsimple
     */
    public static abstract class NoReturnCmd extends Command<Void> {
    }

    /**
     * 返回文章列表的数据库命令
     * 
     * @author mrsimple
     */
    public static abstract class ArticlesCommand extends Command<List<Article>> {
        public ArticlesCommand(DataListener<List<Article>> listener) {
            dataListener = listener;
        }
    }

    /**
     * 返回招聘信息列表的数据库命令
     * 
     * @author mrsimple
     */
    public static abstract class JobsCommand extends Command<List<Job>> {
        public JobsCommand(DataListener<List<Job>> listener) {
            dataListener = listener;
        }
    }

    /**
     * 返回推荐文章列表的数据库命令
     * 
     * @author mrsimple
     */
    public static abstract class RecommendCmd extends Command<List<Recommend>> {
        public RecommendCmd(DataListener<List<Recommend>> listener) {
            dataListener = listener;
        }
    }
}
