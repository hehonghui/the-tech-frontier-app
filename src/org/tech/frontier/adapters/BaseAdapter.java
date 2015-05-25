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

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import org.tech.frontier.listeners.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配器基类
 * 
 * @author mrsimple
 * @param <T> 数据类型
 * @param <VH> ViewHolder类型
 */
public abstract class BaseAdapter<T, VH extends ViewHolder> extends Adapter<VH> {

    List<T> mDataSet = new ArrayList<T>();
    OnItemClickListener<T> mItemClickListener;

    public BaseAdapter(List<T> dataSet) {
        mDataSet = dataSet;
    }

    protected T getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId) {
        return inflateItemView(viewGroup, layoutId, false);
    }

    protected View inflateItemView(ViewGroup viewGroup, int layoutId, boolean attach) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, attach);
    }

    @Override
    public final void onBindViewHolder(VH viewHolder, int position) {
        final T item = getItem(position);
        bindDataToItemView(viewHolder, item);
        setupItemViewClickListener(viewHolder, item);
    }

    protected abstract void bindDataToItemView(VH viewHolder, T item);

    public void setOnItemClickListener(OnItemClickListener<T> mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * ItemView的点击事件
     * 
     * @param viewHolder
     * @param position
     */
    protected void setupItemViewClickListener(VH viewHolder, final T item) {
        viewHolder.itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(item);
                }
            }
        });
    }

}
