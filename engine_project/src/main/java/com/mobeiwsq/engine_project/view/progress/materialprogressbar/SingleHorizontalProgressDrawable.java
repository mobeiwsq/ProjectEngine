package com.mobeiwsq.engine_project.view.progress.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

class SingleHorizontalProgressDrawable extends BaseSingleHorizontalProgressDrawable
        implements ShowBackgroundDrawable {

    /**
     * Value from {@link Drawable#getLevel()}
     */
    private static final int LEVEL_MAX = 10000;

    private boolean mShowBackground;

    public SingleHorizontalProgressDrawable(Context context) {
        super(context);
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public boolean getShowBackground() {
        return mShowBackground;
    }

    @Override
    public void setShowBackground(boolean show) {
        if (mShowBackground != show) {
            mShowBackground = show;
            invalidateSelf();
        }
    }

    @Override
    protected void onDrawRect(Canvas canvas, Paint paint) {

        int level = getLevel();
        if (level == 0) {
            return;
        }

        int saveCount = canvas.save();
        canvas.scale((float) level / LEVEL_MAX, 1, RECT_BOUND.left, 0);

        super.onDrawRect(canvas, paint);
        if (mShowBackground) {
            // Draw twice to emulate the background for secondary progress.
            super.onDrawRect(canvas, paint);
        }

        canvas.restoreToCount(saveCount);
    }
}
