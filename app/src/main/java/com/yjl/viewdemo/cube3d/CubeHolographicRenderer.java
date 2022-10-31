package com.yjl.viewdemo.cube3d;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.Surface;

import com.yjl.viewdemo.utils.GLMatrixStack;

import java.io.File;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.cube3d
 * @CreateDate:2022/7/30
 * @author: yangjianglong
 */
public class CubeHolographicRenderer implements GLSurfaceView.Renderer,MediaPlayer.OnVideoSizeChangedListener, SurfaceTexture.OnFrameAvailableListener {
    private Context mContext;
    private File mVideoFile;

    private MediaPlayer mMediaPlayer;


    private SurfaceTexture mSurfaceTexture;
    private int mTextureId;

    private int mScreenWidth,mScreenHeight,mVideoWidth,mVideoHeight;

    private final float[] mProjectionMatrix = new float[16];
    private float[] sTMatrix = new float[16];
    private boolean mUpdateSurface;


    private CubeHolographicViewDrawer mDrawer;
    public CubeHolographicRenderer(Context mContext, File mVideoFile) {
        this.mContext = mContext;
        this.mVideoFile = mVideoFile;
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
        GLES30.glGenTextures(1,textures,0);
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
        mDrawer = new CubeHolographicViewDrawer(mContext);
        initMediaPlayer();
        mMediaPlayer.start();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mScreenWidth = width;
        mScreenHeight = height;
        GLES30.glViewport(0,0,width,height);
        float ratio = (float) width / height;
        GLMatrixStack.frustum(
                -ratio, ratio, -1, 1,
                3f, 9);

        GLMatrixStack.lookAt(2, 2, -6,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);
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
        mVideoWidth = height;
        mVideoHeight = width;

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
}
