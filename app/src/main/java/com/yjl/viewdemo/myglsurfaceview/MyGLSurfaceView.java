package com.yjl.viewdemo.myglsurfaceview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import javax.microedition.khronos.egl.EGLContext;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.myglsurfaceview
 * @CreateDate:2021/11/28
 * @author: yangjianglong
 */
public class MyGLSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MyGLRenderer mRenderer;
    private Surface mSurface;
    private EGLContext mEglContext;
    private MyEglHelper myEglHelper;
    private MyEGLThread myEGLThread;


    public MyGLSurfaceView(Context context) {
        this(context,null);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        mRenderer = new SimpleRenderer();
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        assert(mRenderer!=null);
        myEglHelper = new MyEglHelper(holder);
        myEGLThread = new MyEGLThread(myEglHelper,mRenderer);
        myEGLThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {


    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
