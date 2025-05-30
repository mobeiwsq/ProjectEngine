
package com.mobeiwsq.engine_project.view.imageview.preview.enitity;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * 图片预览实体类
 *
 */
public class ImageViewInfo implements IPreviewInfo {

    /**
     * 图片地址
     */
    private String mUrl;
    /**
     * 记录坐标
     */
    private Rect mBounds;
    private String mVideoUrl;

    private String mDescription = "描述信息";

    public static List<ImageViewInfo> newInstance(String url, Rect bounds) {
        return Collections.singletonList(new ImageViewInfo(url, bounds));
    }

    public ImageViewInfo(String url) {
        mUrl = url;
    }

    public ImageViewInfo(String url, Rect bounds) {
        mUrl = url;
        mBounds = bounds;
    }

    public ImageViewInfo(String videoUrl, String url) {
        mUrl = url;
        mVideoUrl = videoUrl;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mUrl);
        dest.writeParcelable(mBounds, flags);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
    }

    protected ImageViewInfo(Parcel in) {
        mUrl = in.readString();
        mBounds = in.readParcelable(Rect.class.getClassLoader());
        mDescription = in.readString();
        mVideoUrl = in.readString();
    }

    public static final Parcelable.Creator<ImageViewInfo> CREATOR = new Parcelable.Creator<ImageViewInfo>() {
        @Override
        public ImageViewInfo createFromParcel(Parcel source) {
            return new ImageViewInfo(source);
        }

        @Override
        public ImageViewInfo[] newArray(int size) {
            return new ImageViewInfo[size];
        }
    };
}
