package com.yjl.viewdemo.gl20.glworld.renderableshape;

import android.content.Context;
import android.opengl.GLES20;

import com.yjl.viewdemo.gl20.glworld.GLWorldRenderAble;
import com.yjl.viewdemo.utils.GLMatrixStack;
import com.yjl.viewdemo.utils.GLLoaderUtils;
import com.yjl.viewdemo.utils.GLWorldState;

import java.nio.FloatBuffer;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.gl20.glworld.renderableshape
 * @CreateDate:2021/12/15
 * @author: yangjianglong
 */
public abstract class BaseRenderable extends GLWorldRenderAble {
    private int mProgram;//OpenGL ES 程序
    
    private int mVertexCount;    //顶点坐标个数
    
    private int maPositionHandle;//位置句柄
    private int muMVPMatrixHandle;//顶点变换矩阵句柄
    private int muAmbientHandle;//环境光句柄
    private int maNormalHandle;//顶点法向量句柄
    private int maLightLocationHandle;//光源位置句柄
    private int muMMatrixHandle;//操作矩阵句柄
    private int maCameraHandle;//相机句柄
    private int muShininessHandle;//粗糙度句柄
    private int maTextureHandle;//纹理句柄

    private FloatBuffer vertexBuffer;//顶点缓冲
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mTexCoorBuffer;

    private int mTextureId;
    private int mDrawType;

    private Context mContext;


    public BaseRenderable(int mTextureId, int mDrawType, Context mContext) {
        this.mTextureId = mTextureId;
        this.mDrawType = mDrawType;
        this.mContext = mContext;
        
        initProgram();
    }

    private void initProgram() {
        mProgram = GLLoaderUtils.loadAndInitProgramFromAssets(mContext,"baseRenderShape.vert","baseRenderShape.frag",2);

        //获取句柄
        maPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        maNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
        maTextureHandle = GLES20.glGetAttribLocation(mProgram, "aTexture");

        muMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        muAmbientHandle = GLES20.glGetUniformLocation(mProgram, "uAmbient");
        maLightLocationHandle = GLES20.glGetUniformLocation(mProgram, "uLightLocation");
        muMMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMMatrix");
        maCameraHandle = GLES20.glGetUniformLocation(mProgram, "uCamera");
        muShininessHandle = GLES20.glGetUniformLocation(mProgram, "uShininess");
    }

    protected void init(float[] vertex, float[] texture, float[] normal) {
        vertexBuffer = GLLoaderUtils.getFloatBuffer(vertex);
        mTexCoorBuffer = GLLoaderUtils.getFloatBuffer(texture);
        mNormalBuffer = GLLoaderUtils.getFloatBuffer(normal);
        mVertexCount = vertex.length / 3;
    }
    @Override
    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);

        //启用属性句柄
        GLES20.glEnableVertexAttribArray(maPositionHandle);
        GLES20.glEnableVertexAttribArray(maNormalHandle);
        GLES20.glEnableVertexAttribArray(maTextureHandle);
        //绑定纹理
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        //准备三角顶点坐标数据
        GLES20.glVertexAttribPointer(
                maPositionHandle,//int indx, 索引
                3,//int size,大小
                GLES20.GL_FLOAT,//int type,类型
                false,//boolean normalized,//是否标准化
                3 * 4,// int stride,//跨度
                vertexBuffer);// java.nio.Buffer ptr//缓冲
        //将顶点法向量数据传入渲染管线
        GLES20.glVertexAttribPointer(
                maNormalHandle,
                3,
                GLES20.GL_FLOAT,
                false,
                3 * 4,
                mNormalBuffer);
        //传送顶点纹理坐标数据
        GLES20.glVertexAttribPointer(
                maTextureHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                2 * 4,
                mTexCoorBuffer);

        //句柄传参
        GLES20.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, mvpMatrix, 0);
        GLES20.glUniformMatrix4fv(muMMatrixHandle, 1, false, GLMatrixStack.getOpMatrix(), 0);
        GLES20.glUniform1f(muShininessHandle, 30);
        GLES20.glUniform3fv(maLightLocationHandle, 1, GLWorldState.lightPositionFB);
        GLES20.glUniform3fv(maCameraHandle, 1, GLWorldState.cameraFB);


        GLES20.glDrawArrays(mDrawType, 0, mVertexCount);

    }
}
