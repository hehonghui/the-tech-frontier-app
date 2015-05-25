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

import org.tech.frontier.db.AbsDBAPI;
import org.tech.frontier.db.helper.DatabaseHelper;
import org.tech.frontier.entities.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * 招聘信息相关的数据库操作类
 * 
 * @author mrsimple
 */
class JobsModel extends AbsDBAPI<Job> {

    public JobsModel() {
        super(DatabaseHelper.TABLE_JOBS);
    }

    @Override
    protected List<Job> parseResult(Cursor cursor) {
        List<Job> jobs = new ArrayList<Job>();
        while (cursor.moveToNext()) {
            Job item = new Job();
            item.company = cursor.getString(0);
            item.job = cursor.getString(1);
            item.desc = cursor.getString(2);
            item.email = cursor.getString(3);
            item.url = cursor.getString(4);
            // 解析数据
            jobs.add(item);
        }
        return jobs;
    }

    /**
     * @param item
     * @return
     */
    @Override
    protected ContentValues toContentValues(Job item) {
        ContentValues newValues = new ContentValues();
        newValues.put("company", item.company);
        newValues.put("job", item.job);
        newValues.put("job_desc", item.desc);
        newValues.put("email", item.email);
        newValues.put("url", item.url);
        return newValues;
    }

}
