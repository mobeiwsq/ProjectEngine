package com.mobeiwsq.engine_project.view.imageview.preview.ui;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.mobeiwsq.engine_project.R;


/**
 * 视频播放界面
 *
 */
public class VideoPlayerActivity extends AppCompatActivity {

    public static final String KEY_URL = "com.mobeiwsq.engine.widget.preview.KEY_URL";

    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity_video_player);
        mVideoView = findViewById(R.id.video);

        String videoPath = getIntent().getStringExtra(KEY_URL);
        if (TextUtils.isEmpty(videoPath)) {
            Toast.makeText(VideoPlayerActivity.this, R.string.preview_video_path_error, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mVideoView.setVideoPath(videoPath);
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoPlayerActivity.this, R.string.preview_play_failed, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mVideoView.start();

        findViewById(R.id.rl_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                recycle();
            }
        });
    }

    private void recycle() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null && !mVideoView.isPlaying()) {
            mVideoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mVideoView != null) {
            mVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        if (isFinishing()) {
            recycle();
        }
        super.onStop();
    }

    /***
     * 启动播放视频
     * @param fragment context
     * @param url url
     **/
    public static void start(Fragment fragment, String url) {
        Intent intent = new Intent(fragment.getContext(), VideoPlayerActivity.class);
        intent.putExtra(KEY_URL, url);
        fragment.startActivity(intent);
    }
}
