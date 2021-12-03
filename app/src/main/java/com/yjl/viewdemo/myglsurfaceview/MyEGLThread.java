package com.yjl.viewdemo.myglsurfaceview;

import android.util.Log;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.myglsurfaceview
 * @CreateDate:2021/11/28
 * @author: yangjianglong
 */
public class MyEGLThread implements Runnable{
    private static final String TAG = "MyEGLThread";

    private MyEglHelper myEglHelper;
    private MyGLRenderer myGLRenderer;

   private boolean running = true;
    public MyEGLThread(MyEglHelper myEglHelper, MyGLRenderer myGLRenderer) {
        this.myEglHelper = myEglHelper;
        this.myGLRenderer = myGLRenderer;
    }

    void start(){
        new Thread(this).start();
    }
    void dispose(){
        running = false;
    }


    @Override
    public void run() {
        myEglHelper.initEgl();

        myGLRenderer.onSurfaceCreated();

        while (running){
            if(myGLRenderer.onDrawFrame()){
                int result = myEglHelper.swap();
                if(result!=1){
                    Log.d(TAG, "eglSwapBuffers error : "+result);
                }
            }
        }

        myGLRenderer.onDispose();
        myEglHelper.disposeEGL();

    }
}
