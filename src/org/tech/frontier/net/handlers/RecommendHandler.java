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

import org.json.JSONArray;
import org.json.JSONObject;
import org.tech.frontier.entities.Recommend;

import java.util.ArrayList;
import java.util.List;

public class RecommendHandler implements RespHandler<List<Recommend>, JSONArray> {

    @Override
    public List<Recommend> parse(JSONArray data) {
        List<Recommend> recomendList = new ArrayList<Recommend>();
        int length = data.length();
        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = data.optJSONObject(i);
            String title = jsonObject.optString("title");
            String imgUrl = jsonObject.optString("img_url");
            String url = jsonObject.optString("url");
            recomendList.add(new Recommend(title, url, imgUrl));
        }
        return recomendList;
    }

}
