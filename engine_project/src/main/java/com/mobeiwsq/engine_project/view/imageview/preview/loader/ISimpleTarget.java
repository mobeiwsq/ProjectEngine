package com.mobeiwsq.engine_project.view.imageview.preview.loader;


import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;

/**
 * 图片加载回调状态接口
 *
 */
public interface ISimpleTarget {
   /**
     * Callback when an image has been successfully loaded.
     * <p>
     * <strong>Note:</strong> You must not recycle the bitmap.
     *
     */
    void onResourceReady();

    /**
     * Callback indicating the image could not be successfully loaded.
     *
     * @param errorRes 内容
     */
    void onLoadFailed(@Nullable Drawable errorRes);


}
