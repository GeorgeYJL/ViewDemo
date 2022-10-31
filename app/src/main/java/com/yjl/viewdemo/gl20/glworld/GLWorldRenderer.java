package com.yjl.viewdemo.gl20.glworld;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.yjl.viewdemo.gl20.glworld.shapes.Coordinates;
import com.yjl.viewdemo.utils.GLMatrixStack;
import com.yjl.viewdemo.utils.GLWorldState;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLWorldRenderer implements GLSurfaceView.Renderer {

    private  Context mContext;
    private  GLWorldRenderAble mRenderShape;

    private  int currDeg = 0;
    public GLWorldRenderer(Context context) {
        this.mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//rgba
        mRenderShape = new Coordinates(mContext);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);//GL视口
        float ratio = (float) width / height;
        GLMatrixStack.frustum(
                -ratio, ratio, -1, 1,
                3f, 9);

        GLMatrixStack.lookAt(2, 2, -6,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);

        GLMatrixStack.reset();

        GLWorldState.setEviLight(1f, 1f, 1f, 1.0f);

    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLWorldState.setLightLocation( 1, 1, -1);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLMatrixStack.save();;
        GLMatrixStack.rotate(currDeg,0,1,0);
        mRenderShape.draw(GLMatrixStack.peek());
        GLMatrixStack.restore();

        currDeg++;
        if (currDeg == 360) {
            currDeg = 0;
        }

        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

    }
}
