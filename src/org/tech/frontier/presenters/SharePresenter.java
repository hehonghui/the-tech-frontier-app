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

package org.tech.frontier.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;

import org.tech.frontier.R;
import org.tech.frontier.utils.Constants;

public class SharePresenter implements Response {

    IWeiboShareAPI mWeiboShareAPI;

    public SharePresenter(Context context) {
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context, Constants.APP_KEY);
        mWeiboShareAPI.registerApp();
    }

    /**
     * 分享文章到新浪微博
     * 
     * @param title
     * @param url
     */
    public void share(Activity activity, String title, String url) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();// 初始化微博的分享消息
        TextObject textObject = new TextObject();
        textObject.text = "《" + title + "》" + "这篇文章不错, 文章地址 : " + url
                + "。本文来自: 开发技术前线, http://www.devtf.cn 。";
        weiboMessage.textObject = textObject;
        ImageObject imageObject = new ImageObject();
        imageObject.setImageObject(BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.ic_launcher));
        weiboMessage.imageObject = imageObject;
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;
        mWeiboShareAPI.sendRequest(activity, request); // 发送请求消息到微博,唤起微博分享界面
    }

    public void handleWeiboResponse(Intent intent) {
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    @Override
    public void onResponse(BaseResponse response) {
        switch (response.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
                break;
        }
    }
}
