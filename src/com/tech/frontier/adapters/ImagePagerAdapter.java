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
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.utils.RecyclingPagerAdapter;
import com.squareup.picasso.Picasso;
import com.tech.frontier.R;
import com.tech.frontier.models.entities.Recomend;

import java.util.List;

public class ImagePagerAdapter extends RecyclingPagerAdapter {

    private Context context;
    private List<Recomend> imageIdList;

    private int size;
    private boolean isInfiniteLoop;
    ViewPager mViewPager;

    public ImagePagerAdapter(ViewPager viewPager, List<Recomend> imageIdList) {
        mViewPager = viewPager;
        this.context = mViewPager.getContext();
        this.imageIdList = imageIdList;
        this.size = imageIdList.size();
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        // Infinite loop
        return isInfiniteLoop ? Integer.MAX_VALUE : size;
    }

    /**
     * get really position
     * 
     * @param position
     * @return
     */
    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.recommend_item, container, false);
            holder.imageView = (ImageView) view.findViewById(R.id.recommand_imageview);
            holder.titleTextView = (TextView) view.findViewById(R.id.recommend_title_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        Recomend item = getItem(position);
        holder.titleTextView.setText(item.title);
        Picasso.with(container.getContext())
                .load(item.imgUrl)
                .fit()
                .into(holder.imageView);
        return view;
    }

    private Recomend getItem(int position) {
        return imageIdList.get(getPosition(position));
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView titleTextView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}
