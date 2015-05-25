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

package org.tech.frontier.net.handlers;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tech.frontier.entities.Article;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ArticlesHandler implements RespHandler<List<Article>, JSONArray> {

    @SuppressLint("SimpleDateFormat")
    @Override
    public List<Article> parse(JSONArray jsonArray) {
        List<Article> articleLists = new LinkedList<Article>();
        int count = jsonArray.length();
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (int i = 0; i < count; i++) {
            JSONObject itemObject = jsonArray.optJSONObject(i);
            Article articleItem = new Article();
            articleItem.title = itemObject.optString("title");
            articleItem.author = itemObject.optString("author");
            articleItem.post_id = itemObject.optString("post_id");
            String category = itemObject.optString("category");
            articleItem.category = TextUtils.isEmpty(category) ? 0 : Integer.valueOf(category);
            articleItem.publishTime = formatDate(dateformat, itemObject.optString("date"));
            Log.d("", "title : " + articleItem.title + ", id = " + articleItem.post_id);
            articleLists.add(articleItem);
        }
        return articleLists;
    }

    private String formatDate(SimpleDateFormat dateFormat, String dateString) {
        try {
            Date date = dateFormat.parse(dateString);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
