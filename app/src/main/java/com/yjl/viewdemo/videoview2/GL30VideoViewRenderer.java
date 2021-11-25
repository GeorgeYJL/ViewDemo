package com.yjl.viewdemo.videoview2;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Surface;


import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.videoview2
 * @CreateDate:2021/11/25
 * @author: yangjianglong
 */
public class GL30VideoViewRenderer implements GLSurfaceView.Renderer, MediaPlayer.OnVideoSizeChangedListener, SurfaceTexture.OnFrameAvailableListener {


    private Context mContext;
    private File mVideoFile;

    //播放器相关
    private MediaPlayer mMediaPlayer;

    //渲染相关
    private GL30VideoViewDrawer mDrawer;
    private SurfaceTexture mSurfaceTexture;
    private int mTextureId;

    private volatile boolean mUpdateSurface;

    private int mViewWidth,mViewHeight,mVideoWidth,mVideoHeight;

    private final float[] mProjectionMatrix = new float[16];
    private float[] sTMatrix = new float[16];

    public GL30VideoViewRenderer(Context context, File video) {
        mContext = context;
        mVideoFile = video;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mDrawer = new GL30VideoViewDrawer(mContext);
        initMediaPlayer();
        mMediaPlayer.start();
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();

        try {
            mMediaPlayer.setDataSource(mContext, Uri.fromFile(mVideoFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setOnVideoSizeChangedListener(this);

        int []textures = new int[1];
        GLES30.glGenTextures(1,textures,0);
        mTextureId = textures[0];
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,mTextureId);

        mSurfaceTexture = new SurfaceTexture(mTextureId);
        mSurfaceTexture.setOnFrameAvailableListener(this);

        Surface surface = new Surface(mSurfaceTexture);
        mMediaPlayer.setSurface(surface);
        surface.release();

        try {
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        mViewWidth = width;
        mViewHeight = height;

        updateProjection();
        GLES30.glViewport(0,0,mViewWidth,mViewHeight);
    }

    private void updateProjection() {
        float viewRatio = (float) mViewWidth / mViewHeight;
        float videoRatio = (float) mVideoWidth / mVideoWidth;
        //正交投影矩阵
        Matrix.orthoM(mProjectionMatrix, 0,
                - 1, 1, -1, 1,
                -1f, 1f);
    }



    @Override
    public void onDrawFrame(GL10 gl) {
        synchronized (this){
            if(mUpdateSurface){
                mSurfaceTexture.updateTexImage();
                mSurfaceTexture.getTransformMatrix(sTMatrix);
                mUpdateSurface = false;
            }
        }

        mDrawer.draw(mTextureId,mProjectionMatrix,sTMatrix);

    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

        mUpdateSurface = true;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        mVideoWidth = width;
        mVideoHeight = height;
    }
}
