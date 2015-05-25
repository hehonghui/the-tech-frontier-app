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

package org.tech.frontier.widgets;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class AutoLoadRecyclerView extends RecyclerView {

    OnLoadListener mLoadListener;

    boolean isLoading = false;

    public AutoLoadRecyclerView(Context context) {
        this(context, null);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLoadRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if ( isInEditMode() ) {
            return ;
        }
        init();
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                checkLoadMore(dx, dy);
            }
        });
    }

    public void setOnLoadListener(OnLoadListener listener) {
        mLoadListener = listener;
    }

    private void checkLoadMore(int dx, int dy) {
        if (isBottom(dx, dy) && !isLoading 
                && isValidDelay
                && mLoadListener != null) {
            isValidDelay = false;
            mLoadListener.onLoad();
            Log.e(VIEW_LOG_TAG, "### 加载更多 ");
            postDelayed(new Runnable() {

                @Override
                public void run() {
                    isValidDelay = true;
                }
            }, 1000);
        }
    }

    private boolean isBottom(int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
        int totalItemCount = layoutManager.getItemCount();
        return lastVisibleItem >= totalItemCount - 4 && dy > 0;
    }

    boolean isValidDelay = true;

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

    public static interface OnLoadListener {
        public void onLoad();
    }

}
