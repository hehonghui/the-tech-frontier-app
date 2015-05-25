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

package org.tech.frontier.widgets;


import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * CustomDurationScroller
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-3-2
 */
public class CustomDurationScroller extends Scroller {

    private double scrollFactor = 1;

    public CustomDurationScroller(Context context) {
        super(context);
    }

    public CustomDurationScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    /**
     * not exist in android 2.3
     * 
     * @param context
     * @param interpolator
     * @param flywheel
     */
    // @SuppressLint("NewApi")
    // public CustomDurationScroller(Context context, Interpolator interpolator, boolean flywheel){
    // super(context, interpolator, flywheel);
    // }

    /**
     * Set the factor by which the duration will change
     */
    public void setScrollDurationFactor(double scrollFactor) {
        this.scrollFactor = scrollFactor;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, (int)(duration * scrollFactor));
    }
}