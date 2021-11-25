package com.yjl.viewdemo.videoview;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.videoview
 * @CreateDate:2021/11/25
 * @author: yangjianglong
 */
public class GL20VideoViewRenderer implements GLSurfaceView.Renderer,MediaPlayer.OnVideoSizeChangedListener, SurfaceTexture.OnFrameAvailableListener {

    private Context mContext;
    private File mVideoFile;

    //播放器相关
    private MediaPlayer mMediaPlayer;


    //渲染相关
    private GL20VideoViewDrawer mDrawer;
    private SurfaceTexture mSurfaceTexture;
    private int mTextureId;

    private boolean mUpdateSurface;
    private boolean mPlayerPrepared;

    private int mScreenWidth,mScreenHeight,mVideoWidth,mVideoHeight;

    private final float[] mProjectionMatrix = new float[16];
    private float[] sTMatrix = new float[16];

    public GL20VideoViewRenderer(Context context, File videoFile) {
        mVideoFile = videoFile;
        mContext = context;

    }
    private void initMediaPlayer() {
        // 创建播放器
        mMediaPlayer = new MediaPlayer();

        try {
            // 设置数据源
            mMediaPlayer.setDataSource(mContext, Uri.fromFile(mVideoFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mMediaPlayer.setLooping(true);
        mMediaPlayer.setOnVideoSizeChangedListener(this);

        int [] textures = new int[1];
        GLES20.glGenTextures(1,textures,0);
        mTextureId = textures[0];
        // 绑定纹理
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,mTextureId);
        // 创建Surface
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
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.i("GL20VideoViewRenderer", "onSurfaceCreated: ");
        mDrawer = new GL20VideoViewDrawer(mContext);
        initMediaPlayer();
        mMediaPlayer.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;

//        float videoRatio = (float) mVideoWidth / mVideoHeight;
//        float viewRatio = (float) mScreenWidth / mScreenHeight;
//        Matrix.orthoM(mProjectionMatrix,0,-viewRatio,viewRatio,-viewRatio,viewRatio,-1,1);

        GLES20.glViewport(0,0, mScreenWidth,mScreenHeight);
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
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        mVideoWidth = height;
        mVideoHeight = width;

        updateProjection();

    }
    private void updateProjection(){
        float screenRatio=(float)mScreenWidth/mScreenHeight;
        float videoRatio=(float)mVideoWidth/mVideoHeight;
        if (videoRatio>screenRatio){
            Matrix.orthoM(mProjectionMatrix,0,
                    -1f,1f,-videoRatio/screenRatio,videoRatio/screenRatio,
                    -1f,1f);
        }else {
            Matrix.orthoM(mProjectionMatrix,0,
                    -screenRatio/videoRatio,screenRatio/videoRatio,-1f,1f,
                    -1f,1f);
        }
    }


    @Override
    synchronized public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        mUpdateSurface = true;

    }
}
