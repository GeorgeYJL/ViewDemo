package com.yjl.viewdemo.videoview2;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;

import com.yjl.viewdemo.utils.GLLoaderUtils;

import java.nio.FloatBuffer;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.videoview2
 * @CreateDate:2021/11/25
 * @author: yangjianglong
 */
public class GL30VideoViewDrawer {

    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureCoordBuffer;

    private float[] mVertexData = {
            1f, -1f, 0f,
            -1f, -1f, 0f,
            1f, 1f, 0f,
            -1f, 1f, 0f
    };

    private  float[] mTextureCoordData = {
            1f, 0f,
            0f, 0f,
            1f, 1f,
            0f, 1f
    };

    private final int aPositionLocation = 0;
    private final int aTextureCoordLocation = 1;
    private final int uMatrixLocation = 2;
    private final int uSTMMatrixLocation = 3;
    private final int uSTextureLocation = 4;
    private final int uProgressLocation = 5;

    private  int mProgramID;
    private  int counter = 0;
    private  float progress = 0.0f;



    public GL30VideoViewDrawer(Context mContext) {
        mVertexBuffer = GLLoaderUtils.getFloatBuffer(mVertexData);
        mTextureCoordBuffer = GLLoaderUtils.getFloatBuffer(mTextureCoordData);

        mProgramID = GLLoaderUtils.loadAndInitProgramFromAssets(mContext,"video3.vert","video3.frag",3);
    }

    public void draw(int mTextureId, float[] mProjectionMatrix, float[] sTMatrix) {
        progress += 0.02;

        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUseProgram(mProgramID);

        GLES30.glUniform1f(uProgressLocation, progress);


        GLES30.glUniformMatrix4fv(uMatrixLocation,1,false,mProjectionMatrix,0);
        GLES30.glUniformMatrix4fv(uSTMMatrixLocation,1,false,sTMatrix,0);



        GLES30.glEnableVertexAttribArray(aPositionLocation);
        GLES30.glVertexAttribPointer(aPositionLocation, 3, GLES30.GL_FLOAT, false, 12, mVertexBuffer);

        GLES30.glEnableVertexAttribArray(aTextureCoordLocation);
        GLES30.glVertexAttribPointer(aTextureCoordLocation, 2, GLES30.GL_FLOAT, false, 8, mTextureCoordBuffer);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId);
        GLES30.glUniform1i(uSTextureLocation, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);



    }
}
