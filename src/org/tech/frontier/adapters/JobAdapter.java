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

package org.tech.frontier.adapters;

import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.tech.frontier.R;
import org.tech.frontier.adapters.JobAdapter.JobViewHolder;
import org.tech.frontier.entities.Job;

import java.util.List;

/**
 * 招聘信息Adapter
 * 
 * @author mrsimple
 */
public class JobAdapter extends BaseAdapter<Job, JobViewHolder> {

    public JobAdapter(List<Job> dataSet) {
        super(dataSet);
    }

    @Override
    protected void bindDataToItemView(JobViewHolder viewHolder, Job item) {
        viewHolder.companyTextView.setText(item.company);
        viewHolder.jobTextView.setText(item.job);
        viewHolder.jobDescTextView.setText(item.desc);
        viewHolder.emailTextView.setText(item.email);
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new JobViewHolder(inflateItemView(viewGroup, R.layout.job_item));
    }

    static class JobViewHolder extends ViewHolder {
        TextView companyTextView;
        TextView jobTextView;
        TextView jobDescTextView;
        TextView emailTextView;

        public JobViewHolder(View itemView) {
            super(itemView);
            companyTextView = (TextView) itemView.findViewById(R.id.company_tv);
            jobTextView = (TextView) itemView.findViewById(R.id.job_text_tv);
            jobDescTextView = (TextView) itemView.findViewById(R.id.job_desc_tv);
            emailTextView = (TextView) itemView.findViewById(R.id.job_email_tv);
        }

    }
}
