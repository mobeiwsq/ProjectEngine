
package com.mobeiwsq.engine_project.view.progress.materialprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

abstract class BaseSingleCircularProgressDrawable extends BaseProgressDrawable {

    private static final RectF RECT_BOUND = new RectF(-21, -21, 21, 21);
    private static final RectF RECT_PADDED_BOUND = new RectF(-24, -24, 24, 24);
    private static final RectF RECT_PROGRESS = new RectF(-19, -19, 19, 19);

    @Override
    protected void onPreparePaint(Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
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

        onDrawRing(canvas, paint);
    }

    protected abstract void onDrawRing(Canvas canvas, Paint paint);

    protected void drawRing(Canvas canvas, Paint paint, float startAngle, float sweepAngle) {
        // startAngle starts at 3 o'clock on a watch.
        canvas.drawArc(RECT_PROGRESS, -90 + startAngle, sweepAngle, false, paint);
    }
}
