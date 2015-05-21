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

import android.content.Context;
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
import com.tech.frontier.models.entities.Recomend;
import com.tech.frontier.widgets.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class ArticleAdapter extends Adapter<ViewHolder> {

    List<Article> mArticles;
    OnItemClickListener mClickListener;

    public ArticleAdapter(List<Article> dataSet) {
        mArticles = dataSet;
    }

    @Override
    public int getItemCount() {
        return mArticles == null ? 1 : mArticles.size() + 1;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) viewHolder;
            bindViewForArticle(articleViewHolder, position);
        } else {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;
            bindViewForHeader(headerViewHolder);
        }
    }

    private void bindViewForArticle(ArticleViewHolder articleViewHolder, int position) {
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

    private void bindViewForHeader(HeaderViewHolder headerViewHolder) {
        List<Recomend> imageIdList = new ArrayList<Recomend>();
        imageIdList.add(new Recomend("Android MVP架构实战", "",
                "http://eimg.smzdm.com/201505/20/555be97c655018318.jpg"));
        imageIdList
                .add(new Recomend("Android单元测试难在哪", "",
                        "http://img30.360buyimg.com/da/jfs/t1381/329/75656553/70834/9e68b206/55558b04N3bb2033a.jpg"));
        imageIdList.add(new Recomend("Kotlin自定义View", "",
                "http://img.my.csdn.net/uploads/201407/26/1406383219_5806.jpg"));
        imageIdList.add(new Recomend("Swift的响应式编程", "",
                "http://am.zdmimg.com/201505/20/555be975c74701880.jpg_e600.jpg"));

        AutoScrollViewPager viewPager = headerViewHolder.autoScrollViewPager;
        viewPager.setAdapter(new ImagePagerAdapter(viewPager, imageIdList)
                .setInfiniteLoop(true));

        viewPager.setInterval(15000);
        viewPager.startAutoScroll();
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2
                % imageIdList.size());
    }

    static final int HEADER = 0;

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return 0;
        }

        return 1;
    }

    private HeaderViewHolder createHeaderViewHolder(ViewGroup viewGroup) {
        View headerView = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.auto_slider, viewGroup, false);
        return new HeaderViewHolder(headerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final Context context = viewGroup.getContext();
        if (viewType == HEADER) {
            return createHeaderViewHolder(viewGroup);
        }
        View itemView = LayoutInflater.from(context).inflate(
                R.layout.recyclerview_article_item, viewGroup, false);
        return new ArticleViewHolder(itemView);
    }

    public Article getItem(int position) {
        return mArticles.get(position - 1);
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

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        AutoScrollViewPager autoScrollViewPager;

        public HeaderViewHolder(View view) {
            super(view);
            autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.slide_viewpager);
        }
    }

    public static interface OnItemClickListener {
        public void onClick(Article article);
    }
}
