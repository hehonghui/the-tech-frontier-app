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
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tech.frontier.R;
import com.tech.frontier.models.entities.Article;
import com.tech.frontier.models.entities.Recomend;
import com.tech.frontier.widgets.AutoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class ArticleWithHeaderAdapter extends ArticleAdapter {

    private static final int HEADER = 0;

    public ArticleWithHeaderAdapter(List<Article> dataSet) {
        super(dataSet);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ArticleViewHolder) {
            bindViewForArticle(viewHolder, position);
        } else {
            bindViewForHeader(viewHolder);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER) {
            return createHeaderViewHolder(viewGroup);
        }
        return createArticleViewHolder(viewGroup);
    }

    private HeaderViewHolder createHeaderViewHolder(ViewGroup viewGroup) {
        View headerView = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.auto_slider, viewGroup, false);
        return new HeaderViewHolder(headerView);
    }

    private void bindViewForHeader(ViewHolder viewHolder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) viewHolder;

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

    /*
     * 多个了一个Header
     * @see com.tech.frontier.adapters.BaseArticleAdapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return mArticles == null ? 1 : mArticles.size() + 1;
    }

    @Override
    protected Article getItem(int position) {
        // 因为第一个添加了一个Header,因此数据索引要减去1
        return mArticles.get(position - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return 0;
        }

        return 1;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        AutoScrollViewPager autoScrollViewPager;

        public HeaderViewHolder(View view) {
            super(view);
            autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.slide_viewpager);
        }
    }

}
