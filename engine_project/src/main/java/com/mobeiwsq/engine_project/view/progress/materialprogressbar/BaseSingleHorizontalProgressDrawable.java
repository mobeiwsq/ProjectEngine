package com.mobeiwsq.engine_project.view.progress.materialprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

class BaseSingleHorizontalProgressDrawable extends BaseProgressDrawable {

    private static final int PROGRESS_INTRINSIC_HEIGHT_DP = 4;
    private static final int PADDED_INTRINSIC_HEIGHT_DP = 16;
    protected static final RectF RECT_BOUND = new RectF(-180, -1, 180, 1);
    private static final RectF RECT_PADDED_BOUND = new RectF(-180, -4, 180, 4);

    private int mProgressIntrinsicHeight;
    private int mPaddedIntrinsicHeight;

    public BaseSingleHorizontalProgressDrawable(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        mProgressIntrinsicHeight = Math.round(PROGRESS_INTRINSIC_HEIGHT_DP * density);
        mPaddedIntrinsicHeight = Math.round(PADDED_INTRINSIC_HEIGHT_DP * density);
    }

    @Override
    public int getIntrinsicHeight() {
        return mUseIntrinsicPadding ? mPaddedIntrinsicHeight : mProgressIntrinsicHeight;
    }

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height, Paint paint) {

        if (mUseIntrinsicPadding) {
            canvas.scale(width / RECT_PADDED_BOUND.width(), height / RECT_PADDED_BOUND.height());
            canvas.translate(RECT_PADDED_BOUND.width() / 2, RECT_PADDED_BOUND.height() / 2);
        } else {
            canvas.scale(width / RECT_BOUND.width(), height / RECT_BOUND.height());
            canvas.translate(RECT_BOUND.width() / 2, RECT_BOUND.height() / 2);
        }

        onDrawRect(canvas, paint);
    }

    protected void onDrawRect(Canvas canvas, Paint paint) {
        canvas.drawRect(RECT_BOUND, paint);
    }
}
