package com.mobeiwsq.engine_project.view.imageview.photoview.gestures;

import android.view.MotionEvent;

/**
 * 手势探测器
 *
 */
public interface IGestureDetector {

    boolean onTouchEvent(MotionEvent ev);

    boolean isScaling();

    boolean isDragging();

    void setOnGestureListener(OnGestureListener listener);

}
