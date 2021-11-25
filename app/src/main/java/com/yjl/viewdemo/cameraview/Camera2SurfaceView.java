package com.yjl.viewdemo.cameraview;/*
 * ************************************************************
 * File：Camera2SurfaceView.java
 * Project：ViewDemo
 * Create：2021年11月24日 14:05:52
 * Author：yangjianglong
 * Copyright (c) 2021
 * ************************************************************
 *
 */

import static android.os.Looper.getMainLooper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Collections;
import java.util.concurrent.Semaphore;


public class Camera2SurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private String mCameraID;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraDeviceStateCallBack;
    private CameraCaptureSession mCameraCaptureSession;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);//以防止在关闭相机之前应用程序退出

    private Handler mMainHandle;
    private Handler mChildHandle;

    public Camera2SurfaceView(Context context) {
        this(context, null);
    }

    public Camera2SurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Camera2SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        initHandler();
        initCamera();

        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCameraManager.openCamera(mCameraID, mCameraDeviceStateCallBack, mMainHandle);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        mCameraDevice.close();//释放资源;//释放资源
    }


    private void initHandler() {
        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        mMainHandle = new Handler(getMainLooper());//主线程Handler
        mChildHandle = new Handler(handlerThread.getLooper());//子线程Handler
    }

    private void initCamera() {
        mCameraID = "" + CameraCharacteristics.LENS_FACING_FRONT;//后摄像头
        //获取摄像头管理器
        mCameraManager = (CameraManager) getContext().getSystemService(Context.CAMERA_SERVICE);

        mCameraDeviceStateCallBack = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                mCameraOpenCloseLock.release();
                mCameraDevice = camera;
                startPreview();
            }
            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                mCameraOpenCloseLock.release();
                mCameraDevice.close();
            }
            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                mCameraOpenCloseLock.release();
                mCameraDevice.close();
            }
        };
    }

    private void startPreview() {

        try {
            CaptureRequest.Builder requestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface surface = getHolder().getSurface();

            requestBuilder.addTarget(surface);

            CameraCaptureSession.StateCallback stateCallback = new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if(null == mCameraDevice) return;

                    mCameraCaptureSession = session;

                    try {
                        mCameraCaptureSession.setRepeatingRequest(requestBuilder.build(),null,mChildHandle);

                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            };

            mCameraDevice.createCaptureSession(Collections.singletonList(surface),stateCallback,mChildHandle);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


}
