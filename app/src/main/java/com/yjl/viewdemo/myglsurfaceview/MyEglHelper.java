package com.yjl.viewdemo.myglsurfaceview;

import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.GLUtils;
import android.view.Surface;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.myglsurfaceview
 * @CreateDate:2021/11/28
 * @author: yangjianglong
 *
 *
 * 1、得到Egl实例
 * 2、得到默认的显示设备（就是窗口）
 * 3、初始化默认显示设备
 * 4、设置显示设备的属性
 * 5、从系统中获取对应属性的配置
 * 6、创建EglContext
 * 7、创建渲染的Surface
 * 8、绑定EglContext和Surface到显示设备中
 * 9、刷新数据，显示渲染场景
 *
 */
public class MyEglHelper {
    private static final String TAG = "MyEglHelper";

    private EGL10 mEgl;
    private EGLDisplay mEglDisplay;
    private EGLContext mEglContext;
    private EGLSurface mEglSurface;

    private Object mNativeWindow;

    public MyEglHelper( Object nativeWindow) {
        this.mNativeWindow = nativeWindow;
    }

    public int swap() {
        if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
            return mEgl.eglGetError();
        }
        return EGL10.EGL_SUCCESS;
    }
    public void initEgl() {


        // 1.获取egl
        mEgl = (EGL10) EGLContext.getEGL();

        // 2.创建与原生窗口的连接
        mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if(mEglDisplay == EGL10.EGL_NO_DISPLAY){
            throw new RuntimeException("eglGetDisplay Failed");
        }

        // 3.初始化  获取EGL 版本
        int[] version = new int[2];  // majorVersion minorVersion
        boolean initSuccess = mEgl.eglInitialize(mEglDisplay,version);
        if(!initSuccess)throw new RuntimeException("eglInitialize Failed");

        //////////////////////////////////////////////////////////////////////
        //// 	确定可用Surface配置 有两种方法。
        //      1. 先使用 eglGetConfigs 查询每个配置，再使用 eglGetConfigAttrib 找出最好的选择
        //	    2. 指定一组需求，使用 eglChooseChofig 让 EGL 推荐最佳配置
        /////////////////////////////////////////////////////////////////////

        // 4.通过指定 需求的方式 让EGL 推荐最佳配置
        int[] configAttribList = new int[]{
                EGL10.EGL_RENDERABLE_TYPE,4,
                EGL10.EGL_RED_SIZE,8,
                EGL10.EGL_GREEN_SIZE,8,
                EGL10.EGL_BLUE_SIZE,8,
                EGL10.EGL_ALPHA_SIZE,8,
                EGL10.EGL_DEPTH_SIZE,16,
                EGL10.EGL_STENCIL_SIZE,0,
                EGL10.EGL_SAMPLE_BUFFERS,1,
                EGL10.EGL_SAMPLES,4,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1]; // 用于接收符合条件的configs
        int[]configCount = new int[1];   // 设置 需要符合条件的配置个数  只需要一个
        boolean chooseConfigSuccess = mEgl.eglChooseConfig(mEglDisplay,configAttribList,configs,1,configCount);
        if(!chooseConfigSuccess) throw new RuntimeException("Failed to choose config");
        EGLConfig config;
        if(configCount[0]>0){
            config = configs[0];
        }else{
            config = null;
        }

        // 5.创建EgLContext
        int [] attribList = new int[]{
                EGL14.EGL_CONTEXT_CLIENT_VERSION,2,EGL10.EGL_NONE
        };
        mEglContext = mEgl.eglCreateContext(mEglDisplay,config,EGL10.EGL_NO_CONTEXT,attribList);
        if(mEglContext == EGL10.EGL_NO_CONTEXT){

        }

        // 6.创建 eglSurface
        mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay,config,mNativeWindow,null);
        if(mEglSurface ==null||mEglSurface == EGL10.EGL_NO_SURFACE){
            throw new RuntimeException("GL Error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));
        }

        // 7.关联上下文
        //////////////////////////////////////////////////////////////////////
        //// 关联上下文，
        // 参数二 表示绘制的Surface
        // 参数三 表示读取的Surface
        // 可以设置为同一个Surface
        /////////////////////////////////////////////////////////////////////

        boolean makeCurrentSuccess = mEgl.eglMakeCurrent(mEglDisplay,mEglSurface,mEglSurface,mEglContext);
        if(!makeCurrentSuccess) throw new RuntimeException("GL make current error: " + GLUtils.getEGLErrorString(mEgl.eglGetError()));

    }

    public void disposeEGL(){
        //重置关联上下文
        mEgl.eglMakeCurrent(mEglDisplay,EGL10.EGL_NO_SURFACE,EGL10.EGL_NO_SURFACE,EGL10.EGL_NO_CONTEXT);

        mEgl.eglDestroySurface(mEglDisplay,mEglSurface);

        mEgl.eglDestroyContext(mEglDisplay,mEglContext);

        mEgl.eglTerminate(mEglDisplay);
    }


}
