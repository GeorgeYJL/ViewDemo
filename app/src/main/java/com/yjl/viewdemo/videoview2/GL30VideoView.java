package com.yjl.viewdemo.videoview2;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;


import java.io.File;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.videoview2
 * @CreateDate:2021/11/25
 * @author: yangjianglong
 */
public class GL30VideoView  extends GLSurfaceView {
    private GL30VideoViewRenderer mRenderer;
    public GL30VideoView(Context context) {
        this(context,null);
    }

    public GL30VideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(3);
        File video = new File("/data/data/com.yjl.viewdemo/cache/test.mp4");
        mRenderer = new GL30VideoViewRenderer(getContext(),video);
        setRenderer(mRenderer);
    }
}
