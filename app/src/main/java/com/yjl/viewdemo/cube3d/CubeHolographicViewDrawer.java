package com.yjl.viewdemo.cube3d;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES30;

import com.yjl.viewdemo.utils.GLLoaderUtils;

import java.nio.FloatBuffer;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.cube3d
 * @CreateDate:2022/7/30
 * @author: yangjianglong
 */
public class CubeHolographicViewDrawer {
    private  float[] mVertex ={
            0f, 116/200f, 0f,
            -144/200f, 64/200f, 0f,
            0f, 0f, 0f,
            0f, 0f, 0f,
            144/200f, 64/200f, 0f,
            0f, 116/200f, 0f,

            -144/200f, 64/200f, 0f,
            -136/200f, -118/200f, 0f,
            0f, -1f, 0f,
            0f, -1f, 0f,
            0f, 0f, 0f,
            -144/200f, 64/200f, 0f,

            0f, 0f, 0f,
            0f, -1f, 0f,
            136/200f, -118/200f, 0f,
            136/200f, -118/200f, 0f,
            144/200f, 64/200f, 0f,
            0f, 0f, 0f,

    };

    //纹理坐标，（s,t），t坐标方向和顶点y坐标反着
    public float[] mTextureCoord = {
//            0.5f, 0.5f,
//            (float) (0.5f+0.3*144/200f), (float) (0.5f-0.3*64/200f),
//            0.5f, (float) (0.5f-0.3*116/200f),
//            0.5f, (float) (0.5f-0.3*116/200f),
//            (float) (0.5f-0.3*144/200f), (float) (0.5f-0.3*64/200f),
//            0.5f, 0.5f,
//
//            0.5f, (float) (0.5f+0.3*116/200f),
//            0.5f, 0.5f,
//            (float) (0.5f-0.3*144/200f), (float) (0.5f-0.3*64/200f),
//            (float) (0.5f-0.3*144/200f), (float) (0.5f-0.3*64/200f),
//            (float) (0.5f-0.3*144/200f), (float) (0.5f+0.3*64/200f),
//            0.5f, (float) (0.5f+0.3*116/200f),
//
//            (float) (0.5f+0.3*144/200f), (float) (0.5f+0.3*64/200f),
//            (float) (0.5f+0.3*144/200f), (float) (0.5f-0.3*64/200f),
//            0.5f, 0.5f,
//            0.5f, 0.5f,
//            0.5f, (float) (0.5f+0.3*116/200f),
//            (float) (0.5f+0.3*144/200f), (float) (0.5f+0.3*64/200f),
            0.5f, (float) (0.5f-0.3*116/200f),
            (float) (0.5f-0.3*144/200f), (float) (0.5f-0.3*64/200f),
            0.5f, 0.5f,
            0.5f, 0.5f,
            (float) (0.5f+0.3*144/200f), (float) (0.5f-0.3*64/200f),
            0.5f, (float) (0.5f-0.3*116/200f),

            (float) (0.5f-0.3*144/200f), (float) (0.5f-0.3*64/200f),
            (float) (0.5f-0.3*144/200f), (float) (0.5f+0.3*64/200f),
            0.5f, (float) (0.5f+0.3*116/200f),
            0.5f, (float) (0.5f+0.3*116/200f),
            0.5f, 0.5f,
            (float) (0.5f-0.3*144/200f), (float) (0.5f-0.3*64/200f),

            0.5f, 0.5f,
            0.5f, (float) (0.5f+0.3*116/200f),
            (float) (0.5f+0.3*144/200f), (float) (0.5f+0.3*64/200f),
            (float) (0.5f+0.3*144/200f), (float) (0.5f+0.3*64/200f),
            (float) (0.5f+0.3*144/200f), (float) (0.5f-0.3*64/200f),
            0.5f, 0.5f,

    };


    private FloatBuffer mVertexBuffer;
    private FloatBuffer mTextureCoordBuffer;
    private int mProgram;


    private final int aPositionLocation = 0;
    private final int aTextureCoordLocation = 1;
    private final int uMatrixLocation = 2;
    private final int uSTMMatrixLocation = 3;
    private final int uSTextureLocation = 4;



    public CubeHolographicViewDrawer(Context context) {
        mVertexBuffer = GLLoaderUtils.getFloatBuffer(mVertex);
        mTextureCoordBuffer = GLLoaderUtils.getFloatBuffer(mTextureCoord);

        mProgram = GLLoaderUtils.loadAndInitProgramFromAssets(context,"video3.vert","video3.frag",3);


    }

    public void draw(int textureId, float[] projectionMatrix, float[] sTMatrix) {
        GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

        GLES30.glUseProgram(mProgram);
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        GLES30.glUniformMatrix4fv(uSTMMatrixLocation, 1, false, sTMatrix, 0);

        GLES30.glEnableVertexAttribArray(aPositionLocation);
        GLES30.glVertexAttribPointer(aPositionLocation, 3, GLES30.GL_FLOAT, false, 12, mVertexBuffer);

        GLES30.glEnableVertexAttribArray(aTextureCoordLocation);
        GLES30.glVertexAttribPointer(aTextureCoordLocation, 2, GLES30.GL_FLOAT, false, 8, mTextureCoordBuffer);
        GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
        GLES30.glUniform1i(uSTextureLocation, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, mVertex.length/3);
    }



}
