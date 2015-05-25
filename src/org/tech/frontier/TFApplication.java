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

package org.tech.frontier;

import android.app.Application;

import org.tech.frontier.db.helper.DatabaseMgr;
import org.tech.frontier.net.mgr.RequestQueueMgr;
import org.tech.frontier.utils.LoginSession;

public class TFApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DatabaseMgr.init(this);
        // 初始化网络请求
        RequestQueueMgr.init(this);
        // 初始化
        LoginSession.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        DatabaseMgr.closeDatabase();
        RequestQueueMgr.getRequestQueue().stop();
    }
}
