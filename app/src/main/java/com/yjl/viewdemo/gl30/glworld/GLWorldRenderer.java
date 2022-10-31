package com.yjl.viewdemo.gl30.glworld;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import com.yjl.viewdemo.utils.GLMatrixStack;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.gl30.glworld
 * @CreateDate:2021/12/15
 * @author: yangjianglong
 */
public class GLWorldRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    public GLWorldRenderer(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
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
        

    }
}
