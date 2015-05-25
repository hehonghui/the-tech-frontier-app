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

package org.tech.frontier.ui.frgms;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tech.frontier.R;
import org.tech.frontier.widgets.AutoLoadRecyclerView;
import org.tech.frontier.widgets.AutoLoadRecyclerView.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewFragment<T> extends Fragment implements OnRefreshListener,
        OnLoadListener {
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected AutoLoadRecyclerView mRecyclerView;
    final protected List<T> mDataSet = new ArrayList<T>();

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        initRefreshView(rootView);
        initAdapter();
        initPresenter();
        fetchDatas();
        mSwipeRefreshLayout.setRefreshing(true);
        return rootView;
    }

    protected void initRefreshView(View rootView) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (AutoLoadRecyclerView) rootView.findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()
                .getApplicationContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setVisibility(View.VISIBLE);
        mRecyclerView.setOnLoadListener(this);
    }

    protected abstract void initPresenter();

    protected abstract void initAdapter();

    public abstract void fetchDatas();

}
