package com.yjl.viewdemo.myglsurfaceview;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.myglsurfaceview
 * @CreateDate:2021/11/28
 * @author: yangjianglong
 */
public interface MyGLRenderer {
    void onSurfaceCreated();
    boolean onDrawFrame();
    void onDispose();
}
