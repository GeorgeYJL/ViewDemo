package com.yjl.viewdemo.gl20.glworld.shapes;

import android.content.Context;
import android.opengl.GLES20;

import com.yjl.viewdemo.gl20.glworld.GLWorldRenderAble;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.gl20.glworld.shapes
 * @CreateDate:2021/12/15
 * @author: yangjianglong
 */
public class Coordinates extends GLWorldRenderAble {
    private Context mContext;
    private RenderableShape mCoordinateShape;
    private float[] VERTEX_COO = {//坐标轴
            0.0f, 0.0f, 0.0f,//Z轴
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f,//X轴
            1.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,//Y轴
            0.0f, 1.0f, 0.0f,
    };

    private float[] COLOR_COO = {//坐标轴颜色
            0.0f, 0.0f, 1.0f, 1.0f,//Z轴:蓝色
            0.0f, 0.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 0.0f, 1.0f,//X轴：黄色
            1.0f, 1.0f, 0.0f, 1.0f,
            0.0f, 1.0f, 0.0f, 1.0f,//Y轴：绿色
            0.0f, 1.0f, 0.0f, 1.0f,
    };

    public Coordinates(Context context) {
        this.mContext = context;

        BaseShape coordinate = new BaseShape(VERTEX_COO,COLOR_COO, GLES20.GL_LINES);

        mCoordinateShape = new RenderableShape(mContext,coordinate);
    }

    @Override
    public void draw(float[] mvpMatrix) {
        mCoordinateShape.draw(mvpMatrix);
    }
}
