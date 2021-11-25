package com.yjl.viewdemo.videoview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.io.File;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.videoview
 * @CreateDate:2021/11/25
 * @author: yangjianglong
 */
public class GL20VideoView extends GLSurfaceView {

    private  GL20VideoViewRenderer mRenderer;
    public GL20VideoView(Context context) {
        this(context,null);
    }

    public GL20VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        File video = new File("/data/data/com.yjl.viewdemo/cache/test.mp4");

        mRenderer = new GL20VideoViewRenderer(getContext(),video);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

    }
}
