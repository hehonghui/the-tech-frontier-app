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

import android.content.Intent;

import com.android.volley.VolleyError;

import org.tech.frontier.adapters.JobAdapter;
import org.tech.frontier.entities.Job;
import org.tech.frontier.listeners.OnItemClickListener;
import org.tech.frontier.presenters.JobPresenter;
import org.tech.frontier.ui.DetailActivity;
import org.tech.frontier.ui.interfaces.JobViewInterface;

import java.util.List;

/**
 * 招聘信息页面
 * 
 * @author mrsimple
 */
public class JobsFragment extends RecyclerViewFragment<Job> implements JobViewInterface {

    JobAdapter mAdapter;
    JobPresenter mJonPresenter;

    @Override
    public void onRefresh() {
        mJonPresenter.fetchJobs();
    }

    @Override
    public void onLoad() {

    }

    @Override
    protected void initPresenter() {
        mJonPresenter = new JobPresenter(this);
    }

    @Override
    public void fetchDatas() {
        mJonPresenter.fetchJobs();
    }

    @Override
    protected void initAdapter() {
        mAdapter = new JobAdapter(mDataSet);
        mAdapter.setOnItemClickListener(new OnItemClickListener<Job>() {

            @Override
            public void onClick(Job item) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("url", item.url);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void fetchedData(List<Job> result) {
        mDataSet.clear();
        mDataSet.addAll(result);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
        mRecyclerView.setLoading(false);
    }

    @Override
    public void showLoading() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onError(VolleyError error) {
        mSwipeRefreshLayout.setRefreshing(false);
    }

}
