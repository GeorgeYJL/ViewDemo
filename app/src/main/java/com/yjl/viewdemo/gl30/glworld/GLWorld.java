package com.yjl.viewdemo.gl30.glworld;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.gl30.glworld
 * @CreateDate:2021/12/15
 * @author: yangjianglong
 */
public class GLWorld extends GLSurfaceView {
    private GLWorldRenderer mRenderer;
    public GLWorld(Context context) {
        this(context,null);
    }

    public GLWorld(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        setEGLContextClientVersion(3);
        mRenderer = new GLWorldRenderer (getContext());
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }
}
