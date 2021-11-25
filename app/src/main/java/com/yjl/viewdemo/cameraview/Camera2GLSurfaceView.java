package com.yjl.viewdemo.cameraview;
/*
 * ************************************************************
 * File：Camera2GLSurfaceView.java
 * Project：ViewDemo
 * Create：2021年11月24日 15:17:53
 * Author：yangjianglong
 * Copyright (c) 2021
 * ************************************************************
 *
 */

import static android.os.Looper.getMainLooper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Camera2GLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer {



    //    Camera2 相关
    private String mCameraID;
    private CameraManager mCameraManager;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraDeviceStateCallBack;
    private CameraCaptureSession mCameraCaptureSession;

    private Semaphore mCameraOpenCloseLock = new Semaphore(1);//以防止在关闭相机之前应用程序退出

    private Handler mMainHandle;
    private Handler mChildHandle;


    private Size mVideoSize;

    //    GL 绘制相关
    private Camera2GLSurfaceViewDrawer mDrawer;
    private SurfaceTexture mSurfaceTexture;
    private Surface mSurface;
    private int[] mTextureID = new int[1];




    public Camera2GLSurfaceView(Context context) {
        this(context,null);
    }

    public Camera2GLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        setRenderer(this);

    }




    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        mDrawer = new Camera2GLSurfaceViewDrawer(getContext());


        //创建纹理对象
        GLES30.glGenTextures(mTextureID.length,mTextureID,0);
        //通过纹理ID 创建SurfaceTexture对象 建立了纹理和SurfaceTexture 的绑定关系
        mSurfaceTexture = new SurfaceTexture(mTextureID[0]);


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
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES30.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        mSurfaceTexture.updateTexImage();

        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        mDrawer.draw(mTextureID[0]);



    }


    /**
     * 根据输出类获取指定相机的输出尺寸列表，降序排序
     */
    public List<Size> getCameraOutputSizes(CameraManager cameraManager, String cameraId, Class clz){
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            List<Size> sizes = Arrays.asList(configs.getOutputSizes(clz));
            Collections.sort(sizes, (o1, o2) -> o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight());
            Collections.reverse(sizes);
            return sizes;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return null;
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

        //获取输出窗口大小
        mVideoSize = getCameraOutputSizes(mCameraManager,mCameraID,SurfaceTexture.class).get(0);

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
        //
        mSurfaceTexture.setDefaultBufferSize(mVideoSize.getWidth(), mVideoSize.getHeight());

        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                requestRender();
            }
        });

        mSurface = new Surface(mSurfaceTexture);



        try {
            CaptureRequest.Builder requestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

//            Surface surface = getHolder().getSurface();

            requestBuilder.addTarget(mSurface);

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

            mCameraDevice.createCaptureSession(Collections.singletonList(mSurface),stateCallback,mChildHandle);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
