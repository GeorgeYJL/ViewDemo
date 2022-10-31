package com.yjl.viewdemo.cube3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.io.File;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.cube3d
 * @CreateDate:2022/7/30
 * @author: yangjianglong
 */
public class CubeHolographicView extends GLSurfaceView {

    private CubeHolographicRenderer mRenderer;

    public CubeHolographicView(Context context) {
        this(context,null);
    }

    public CubeHolographicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init(){
        File video = new File("/data/data/com.yjl.viewdemo/cache/test.mp4");

        setEGLContextClientVersion(3);
        mRenderer = new CubeHolographicRenderer(getContext(),video);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

}
