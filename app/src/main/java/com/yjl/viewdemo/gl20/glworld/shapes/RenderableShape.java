package com.yjl.viewdemo.gl20.glworld.shapes;

import android.content.Context;
import android.opengl.GLES20;

import com.yjl.viewdemo.gl20.glworld.GLWorldRenderAble;
import com.yjl.viewdemo.utils.GLLoaderUtils;

import java.nio.FloatBuffer;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.gl20.glworld.renderableshape
 * @CreateDate:2021/12/15
 * @author: yangjianglong
 */
public class RenderableShape extends GLWorldRenderAble {
    private int mProgram;//OpenGL ES 程序


   /*
    * attribute vec3 aPosition;
    * uniform mat4 uMVPMatrix; //总变换矩阵
    * attribute vec4 aColor;//顶点颜色
    * varying  vec4 vColor;//片元颜色
    */
    private int muMVPMatrixHandle;//顶点变换矩阵句柄
    private int maPositionHandle;//位置句柄
    private int maColorHandle;//颜色句柄

    private FloatBuffer mVertexBuffer;//顶点缓冲
    private FloatBuffer mColorBuffer;

    private Context mContext;

    private BaseShape mShape;


    public RenderableShape(Context context, BaseShape shape) {
        this.mContext = context;
        this.mShape = shape;
        mVertexBuffer = GLLoaderUtils.getFloatBuffer(mShape.getmVertex());
        mColorBuffer = GLLoaderUtils.getFloatBuffer(mShape.getmColor());
        initProgram();
    }

    private void initProgram() {
        mProgram = GLLoaderUtils.loadAndInitProgramFromAssets(mContext,"triangle.vert","triangle.frag",2);

        //获取句柄
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

    }

    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        //启用属性句柄
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maColorHandle);

        GLES20.glVertexAttribPointer(
                maPositionHandle,//int indx, 索引
                3,//int size,大小
                GLES20.GL_FLOAT,//int type,类型
                false,//boolean normalized,//是否标准化
                3 * 4,// int stride,//跨度
                mVertexBuffer);// java.nio.Buffer ptr//缓冲
        GLES20.glVertexAttribPointer(
                maColorHandle,
                4,
                GLES20.GL_FLOAT,
                false,
                4 * 4,
                mColorBuffer);


        //句柄传参
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(mShape.getmDrawType(), 0, mShape.getCount());

    }
}
