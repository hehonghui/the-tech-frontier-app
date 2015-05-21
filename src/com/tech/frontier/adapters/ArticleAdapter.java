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
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tech.frontier.R;
import com.tech.frontier.models.entities.Article;

import java.util.List;

/**
 * 主页文章列表的Adapter
 * 
 * @author mrsimple
 */
public class ArticleAdapter extends Adapter<ViewHolder> {

    List<Article> mArticles;
    OnItemClickListener mClickListener;

    public ArticleAdapter(List<Article> dataSet) {
        mArticles = dataSet;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ArticleViewHolder) {
            bindViewForArticle(viewHolder, position);
        }
    }

    protected void bindViewForArticle(ViewHolder viewHolder, int position) {
        ArticleViewHolder articleViewHolder = (ArticleViewHolder) viewHolder;
        final Article article = getItem(position);
        articleViewHolder.titleTv.setText(article.title);
        articleViewHolder.publishTimeTv.setText(article.publishTime);
        articleViewHolder.authorTv.setText(article.author);
        articleViewHolder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mClickListener != null) {
                    mClickListener.onClick(article);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return createArticleViewHolder(viewGroup);
    }

    protected ViewHolder createArticleViewHolder(ViewGroup viewGroup) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recyclerview_article_item, viewGroup, false);
        return new ArticleViewHolder(itemView);
    }

    protected Article getItem(int position) {
        return mArticles.get(position);
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
