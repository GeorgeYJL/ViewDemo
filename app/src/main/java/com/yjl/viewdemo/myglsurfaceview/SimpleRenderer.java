package com.yjl.viewdemo.myglsurfaceview;

import android.opengl.GLES30;

import java.util.Random;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.myglsurfaceview
 * @CreateDate:2021/12/3
 * @author: yangjianglong
 */
public class SimpleRenderer implements MyGLRenderer{
    @Override
    public void onSurfaceCreated() {

    }

    @Override
    public boolean onDrawFrame() {

        GLES30.glClearColor(new Random().nextFloat(),new Random().nextFloat(),new Random().nextFloat(),1);
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        return true;
    }

    @Override
    public void onDispose() {

    }
}
