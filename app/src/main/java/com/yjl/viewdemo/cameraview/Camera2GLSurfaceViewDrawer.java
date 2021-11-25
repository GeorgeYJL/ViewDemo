package com.yjl.viewdemo.cameraview;/*
 * ************************************************************
 * File：Camera2GLSurfaceViewDrawer.java
 * Project：ViewDemo
 * Create：2021年11月24日 16:04:12
 * Author：yangjianglong
 * Copyright (c) 2021
 * ************************************************************
 *
 */

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;

import com.yjl.viewdemo.utils.GLShaderUtils;

import java.nio.FloatBuffer;

public class Camera2GLSurfaceViewDrawer {
    private static final String VERTEX_ATTRIB_POSITION = "a_Position";
    private static final int VERTEX_ATTRIB_POSITION_SIZE = 3;
    private static final String VERTEX_ATTRIB_TEXTURE_POSITION = "a_texCoord";
    private static final int VERTEX_ATTRIB_TEXTURE_POSITION_SIZE = 2;
    private static final String UNIFORM_TEXTURE = "s_texture";

    private  float[] mVertex ={
            -1f,1f,0.0f,//左上
            -1f,-1f,0.0f,//左下
            1f,-1f,0.0f,//右下
            1f,1f,0.0f//右上
    };

    //纹理坐标，（s,t），t坐标方向和顶点y坐标反着
    public float[] mTextureCoord = {
            0.0f,1.0f,
            1.0f,1.0f,
            1.0f,0.0f,
            0.0f,0.0f
    };

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureCoordBuffer;
    private int mProgram;

    private Context mContext;

    public Camera2GLSurfaceViewDrawer(Context context) {
        this.mContext = context;

        mTextureCoordBuffer = GLShaderUtils.getFloatBuffer(mTextureCoord);
        mVertexBuffer = GLShaderUtils.getFloatBuffer(mVertex);

        mProgram = GLShaderUtils.loadAndInitProgramFromAssets(this.mContext,"camera.vert","camera.frag",3);

    }




    public void draw(int textureId){
        GLES30.glUseProgram(mProgram);
        //初始化句柄
        int vertexLoc = GLES30.glGetAttribLocation(mProgram, VERTEX_ATTRIB_POSITION);
        int textureLoc = GLES30.glGetAttribLocation(mProgram, VERTEX_ATTRIB_TEXTURE_POSITION);

        GLES30.glEnableVertexAttribArray(vertexLoc);
        GLES30.glEnableVertexAttribArray(textureLoc);

        GLES30.glVertexAttribPointer(vertexLoc,
                VERTEX_ATTRIB_POSITION_SIZE,
                GLES30.GL_FLOAT,
                false,
                0,
                mVertexBuffer);

        GLES30.glVertexAttribPointer(textureLoc,
                VERTEX_ATTRIB_TEXTURE_POSITION_SIZE,
                GLES30.GL_FLOAT,
                false,
                0,
                mTextureCoordBuffer);

        //纹理绑定
        GLES30.glActiveTexture( GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);

        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES30.GL_TEXTURE_MAG_FILTER,  GLES30.GL_LINEAR);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES30.GL_TEXTURE_WRAP_S,  GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,  GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);

        int uTextureLoc =  GLES30.glGetUniformLocation(mProgram, UNIFORM_TEXTURE);
        GLES30.glUniform1i(uTextureLoc,0);
        //绘制
        GLES30.glDrawArrays( GLES30.GL_TRIANGLE_FAN,0, mVertex.length / 3);
        //禁用顶点
        GLES30.glDisableVertexAttribArray(vertexLoc);
        GLES30.glDisableVertexAttribArray(textureLoc);
    }

}
