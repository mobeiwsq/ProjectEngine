package com.mobeiwsq.engine_project.view.imageview.photoview.gestures;

import android.content.Context;
import android.os.Build;

public final class VersionedGestureDetector {

    public static IGestureDetector newInstance(Context context,
                                               OnGestureListener listener) {
        final int sdkVersion = Build.VERSION.SDK_INT;
        IGestureDetector detector;

        if (sdkVersion < Build.VERSION_CODES.ECLAIR) {
            detector = new CupcakeGestureDetector(context);
        } else if (sdkVersion < Build.VERSION_CODES.FROYO) {
            detector = new EclairGestureDetector(context);
        } else {
            detector = new FroyoGestureDetector(context);
        }

        detector.setOnGestureListener(listener);

        return detector;
    }

}