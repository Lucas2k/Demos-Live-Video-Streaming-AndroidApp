package com.example.cerberus001.exam_livevideostreaming;

import android.graphics.ImageFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.yuneec.videostreaming.RTSPPlayer;
import com.yuneec.videostreaming.VideoPlayer;
import com.yuneec.videostreaming.VideoPlayerException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {

    private static final String TAG = "MainActivity";
    RTSPPlayer videoPlayer;
    SurfaceView mView;
    Surface videoSurface;
    SurfaceHolder videoSurfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mView = (SurfaceView) findViewById(R.id.surfaceview);

        videoSurfaceHolder = mView.getHolder();
        videoSurfaceHolder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                videoSurface = holder.getSurface();
                try {
                    videoPlayer.setSurface(videoSurface);
                } catch (VideoPlayerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                videoSurface = holder.getSurface();
                try {
                    videoPlayer.setSurface(videoSurface);
                    videoPlayer.start();
                } catch (VideoPlayerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                try {
                    videoPlayer.setSurface(null);
                } catch (VideoPlayerException e) {
                    e.printStackTrace();
                }
            }
        });

        videoPlayer = (RTSPPlayer) VideoPlayer.getPlayer(VideoPlayer.PlayerType.LIVE_STREAM);

    }


    private void initVideoPlayer() {

            videoPlayer.initializePlayer();
            try {
                videoPlayer.setDataSource(Common.VideoStreamUrl);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                e.printStackTrace();
            } catch (VideoPlayerException e) {
                e.printStackTrace();
            }

    }

    private void deInitVideoPlayer() {
        try {
            videoPlayer.stop();
            videoPlayer.releasePlayer();
        } catch (VideoPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initVideoPlayer();
    }

    @Override
    public void onStop() {

        super.onStop();
        deInitVideoPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            videoPlayer.stop();
        } catch (VideoPlayerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (!videoPlayer.isPlaying()) {
                videoPlayer.start();
            }
        } catch (VideoPlayerException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
