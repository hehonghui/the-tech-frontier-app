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

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

import org.tech.frontier.R;
import org.tech.frontier.entities.Article;
import org.tech.frontier.entities.Recommend;
import org.tech.frontier.listeners.OnItemClickListener;
import org.tech.frontier.presenters.RecommendPresenter;
import org.tech.frontier.widgets.AutoScrollViewPager;

import java.util.List;

/**
 * 主页文章列表的Adapter,第一项元素为HeaderView，因为RecyclerView并不默认支持Header，因此需要设置viewType，
 * 将第一项设置为Header View。这里需要注意是因为Header
 * View暂用了第一项，因此根据position获取数据时需要把position减去1，这样获取到的数据才是正确的.
 * 
 * @author mrsimple
 */
public class ArticleWithHeaderAdapter extends ArticleAdapter {

    /**
     * 
     */
    private static final int HEADER_TYPE = 0;

    /**
     * 广告栏ViewHolder
     */
    HeaderViewHolder mHeaderViewHolder;
    /**
     * 推荐相关的Presenter
     */
    RecommendPresenter mPresenter;
    /**
     * 推荐文章的点击事件
     */
    OnItemClickListener<Recommend> mRecmItemClickListener;

    public ArticleWithHeaderAdapter(List<Article> dataSet) {
        super(dataSet);
    }

    @Override
    protected void bindDataToItemView(ViewHolder viewHolder, Article item) {
        if (viewHolder instanceof ArticleViewHolder) {
            bindArticleToItemView((ArticleViewHolder) viewHolder, item);
        } else if (viewHolder instanceof HeaderViewHolder
                && mPresenter == null) { // 获取一次数据之后不再进行重新请求
            mHeaderViewHolder = (HeaderViewHolder) viewHolder;
            mPresenter = new RecommendPresenter(mHeaderViewHolder);
            mPresenter.setRecommendClickListener(mRecmItemClickListener);
            mPresenter.fetchRecomends();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER_TYPE) {
            return new HeaderViewHolder(inflateItemView(viewGroup, R.layout.auto_slider));
        }
        return createArticleViewHolder(viewGroup);
    }

    public void setRecommendClickListener(OnItemClickListener<Recommend> listener) {
        mRecmItemClickListener = listener;
    }

    /*
     * 多个了一个Header
     * @see com.tech.frontier.adapters.BaseArticleAdapter#getItemCount()
     */
    @Override
    public int getItemCount() {
        return mDataSet == null ? 1 : mDataSet.size() + 1;
    }

    @Override
    protected Article getItem(int position) {
        // 因为第一个添加了一个Header,因此数据索引要减去1
        int itemIndex = position - 1;
        return itemIndex >= 0 ? mDataSet.get(itemIndex) : null;
    }

    @Override
    public int getItemViewType(int position) {
        if (HEADER_TYPE == position) {
            return 0;
        }
        return 1;
    }

    /**
     * 顶部的自动滚动ViewPager的ViewHolder
     * 
     * @author mrsimple
     */
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public AutoScrollViewPager autoScrollViewPager;
        public CirclePageIndicator mIndicator;

        public HeaderViewHolder(View view) {
            super(view);
            autoScrollViewPager = (AutoScrollViewPager) view.findViewById(R.id.slide_viewpager);
            mIndicator = (CirclePageIndicator) view.findViewById(R.id.recommend_indicator);
        }
    }
}
