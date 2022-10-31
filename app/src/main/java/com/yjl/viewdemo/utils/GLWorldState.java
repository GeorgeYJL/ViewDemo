package com.yjl.viewdemo.utils;

import java.nio.FloatBuffer;

public class GLWorldState {
    ////////----------设置光源
    private static float[] lightLocation = new float[]{0, 0, 0};//定位光光源位置
    public static FloatBuffer lightPositionFB;

    //设置灯光位置的方法
    public static void setLightLocation(float x, float y, float z) {
        lightLocation[0] = x;
        lightLocation[1] = y;
        lightLocation[2] = z;
        lightPositionFB = GLLoaderUtils.getFloatBuffer(lightLocation);
    }


    ////////----------设置相机位置
    static float[] cameraLocation = new float[3];//摄像机位置
    public static FloatBuffer cameraFB;

    //设置灯光位置的方法
    public static void setCameraLocation(float x, float y, float z) {
        cameraLocation[0] = x;
        cameraLocation[1] = y;
        cameraLocation[2] = z;
        cameraFB = GLLoaderUtils.getFloatBuffer(cameraLocation);
    }

    ////////----------环境光
    static float[] eviLight = new float[4];//摄像机位置
    public static FloatBuffer eviLightFB;

    //设置灯光位置的方法
    public static void setEviLight(float r, float g, float b,float a) {
        eviLight[0] = r;
        eviLight[1] = g;
        eviLight[2] = b;
        eviLight[3] = a;
        eviLightFB = GLLoaderUtils.getFloatBuffer(eviLight);
    }


}
