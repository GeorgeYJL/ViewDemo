package com.yjl.viewdemo.videoview;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import java.nio.FloatBuffer;
import com.yjl.viewdemo.utils.GLShaderUtils;
/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.videoview
 * @CreateDate:2021/11/25
 * @author: yangjianglong
 */
public class GL20VideoViewDrawer {
    private  float[] mVertex ={
            1f, -1f, 0f,
            -1f, -1f, 0f,
            1f, 1f, 0f,
            -1f, 1f, 0f
    };

    //纹理坐标，（s,t），t坐标方向和顶点y坐标反着
    public float[] mTextureCoord = {
            1f, 0f,
            0f, 0f,
            1f, 1f,
            0f, 1f
    };

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureCoordBuffer;
    private int mProgram;


//    attribute vec4 aPosition;//顶点位置
//    attribute vec4 aTexCoord;//纹理坐标
//    uniform mat4 uMatrix; //顶点变换矩阵
//    uniform mat4 uSTMatrix; //纹理变换矩阵
//
//    varying  vec2 vTexCoord2Frag;//片元颜色
//    varying vec2 vTexCoord2Frag;

//    uniform samplerExternalOES uSTexture;

    private int aPositionHandle;//顶点位置句柄
    private int aTexCoordHandle;//纹理坐标句柄

    private int uMatrixHandle;//顶点变化矩阵句柄

    private int uSTMatrixHandle;//纹理变换矩阵句柄
    private int uSTextureHandle;

    public GL20VideoViewDrawer(Context context) {

        mVertexBuffer = GLShaderUtils.getFloatBuffer(mVertex);
        mTextureCoordBuffer = GLShaderUtils.getFloatBuffer(mTextureCoord);

        mProgram = GLShaderUtils.loadAndInitProgramFromAssets(context,"video.vert","video.frag",2);


        aPositionHandle = GLES20.glGetAttribLocation(mProgram,"aPosition");
        aTexCoordHandle = GLES20.glGetAttribLocation(mProgram,"aTexCoord");

        uMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uMatrix");
        uSTMatrixHandle = GLES20.glGetUniformLocation(mProgram,"uSTMatrix");

        uSTextureHandle = GLES20.glGetUniformLocation(mProgram,"uSTexture");
    }

    public void draw(int textureId,float[] projectionMatrix, float[] sTMatrix){
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        GLES20.glUniformMatrix4fv(uMatrixHandle,1,false, projectionMatrix,0);
        GLES20.glUniformMatrix4fv(uSTMatrixHandle, 1, false, sTMatrix, 0);


        GLES20.glEnableVertexAttribArray(aPositionHandle);

        GLES20.glVertexAttribPointer(aPositionHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                12,
                mVertexBuffer);


        GLES20.glEnableVertexAttribArray(aTexCoordHandle);

        GLES20.glVertexAttribPointer(aTexCoordHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                8,
                mTextureCoordBuffer);



//        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
//        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES20.GL_TEXTURE_MAG_FILTER,  GLES20.GL_LINEAR);
//        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES20.GL_TEXTURE_WRAP_S,  GLES20.GL_CLAMP_TO_EDGE);
//        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //纹理绑定
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);


        GLES20.glUniform1i(uSTextureHandle,0);
        //绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP,0, 4);
        //禁用顶点
        GLES20.glDisableVertexAttribArray(aPositionHandle);
        GLES20.glDisableVertexAttribArray(aTexCoordHandle);
    }

}
