
package com.yjl.viewdemo.cameraview;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.io.IOException;
/*
 * ************************************************************
 * File：Camera1SurfaceView.java
 * Project：ViewDemo
 * Create：2021年11月24日 13:51:41
 * Author：yangjianglong
 * Copyright (c) 2021
 * ************************************************************
 *
 */

public class Camera1SurfaceView extends SurfaceView implements SurfaceHolder.Callback  {
    private Camera mCamera;

    public Camera1SurfaceView(Context context) {
        this(context,null);
    }

    public Camera1SurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Camera1SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

        initCamera(holder);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        mCamera.release();
    }
    private void initCamera(SurfaceHolder holder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
