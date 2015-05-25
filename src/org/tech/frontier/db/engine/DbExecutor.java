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

package org.tech.frontier.db.engine;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import org.tech.frontier.db.cmd.Command;
import org.tech.frontier.listeners.DataListener;

/**
 * 数据库操作执行引擎
 */
public class DbExecutor {

    // HandlerThread内部封装了自己的Handler和Thead，有单独的Looper和消息队列
    private static final HandlerThread HT = new HandlerThread(
            DbExecutor.class.getName(),
            android.os.Process.THREAD_PRIORITY_BACKGROUND);
    static {
        HT.start();
    }

    // 异步线程的Handler
    final static Handler sAsyncHandler = new Handler(HT.getLooper());

    // 主线程消息队列的Handler
    final static Handler mUIHandler = new Handler(Looper.getMainLooper());

    static DbExecutor mDispatcher = new DbExecutor();

    private DbExecutor() {
    }

    public static DbExecutor getExecutor() {
        return mDispatcher;
    }

    public Handler getUIHandler() {
        return mUIHandler;
    }

    public void submit(Runnable runnable) {
        sAsyncHandler.post(runnable);
    }

    /**
     * 执行请求
     *
     * @param command
     */
    public <T> void execute(final Command<T> command) {
        sAsyncHandler.post(new Runnable() {

            @Override
            public void run() {
                T result = command.execute();
                if (command.dataListener != null) {
                    // 执行数据库操作请求,将结果投递到UI线程
                    postResult(result, command.dataListener);
                }
            }
        });
    }

    /**
     * 将结果投递到UI线程
     *
     * @param result
     * @param listener
     */
    private <T> void postResult(final T result,
            final DataListener<T> listener) {
        mUIHandler.post(new Runnable() {

            @Override
            public void run() {
                listener.onComplete(result);
            }
        });
    }
}
