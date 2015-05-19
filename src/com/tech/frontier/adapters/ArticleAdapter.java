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

package com.tech.frontier.adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.frontier.R;
import com.tech.frontier.adapters.ArticleAdapter.ArticleViewHolder;
import com.tech.frontier.models.entities.Article;

import java.util.List;

public class ArticleAdapter extends Adapter<ArticleViewHolder> {

    List<Article> mArticles;
    OnItemClickListener mClickListener;

    public ArticleAdapter(List<Article> dataSet) {
        mArticles = dataSet;
    }

    @Override
    public int getItemCount() {
        return mArticles == null ? 0 : mArticles.size();
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder viewHolder, int position) {
        final Article article = mArticles.get(position);
        viewHolder.titleTv.setText(article.title);
        viewHolder.publishTimeTv.setText(article.publishTime);
        viewHolder.authorTv.setText(article.author);
        viewHolder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(article);
                }
            }
        });
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recyclerview_article_item,
                viewGroup, false);
        return new ArticleViewHolder(itemView);
    }

    public void setOnItemClickListener(OnItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTv;
        public TextView publishTimeTv;
        public TextView authorTv;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            titleTv = (TextView) itemView.findViewById(R.id.article_title_tv);
            publishTimeTv = (TextView) itemView.findViewById(R.id.article_time_tv);
            authorTv = (TextView) itemView.findViewById(R.id.article_author_tv);
        }
    }

    public static interface OnItemClickListener {
        public void onClick(Article article);
    }
}
