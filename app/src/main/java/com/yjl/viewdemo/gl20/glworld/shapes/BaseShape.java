package com.yjl.viewdemo.gl20.glworld.shapes;

import androidx.annotation.NonNull;

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.gl20.glworld.shapes
 * @CreateDate:2021/12/15
 * @author: yangjianglong
 */
public class BaseShape implements Cloneable{
    private  String mShapeID;
    private float[] mVertex;
    private float[] mColor;
    private int mDrawType;

    public BaseShape(float[] mVertex, float[] mColor, int mDrawType) {
        this.mVertex = mVertex;
        this.mColor = mColor;
        this.mDrawType = mDrawType;
    }


    public BaseShape clone()  {
        BaseShape clone = null;
        try {
            clone = (BaseShape) super.clone();

            float []vertex = new float[mVertex.length];
            float []color = new float[mColor.length];

            System.arraycopy(mVertex, 0, vertex, 0, mVertex.length);
            System.arraycopy(mColor, 0, color, 0, mColor.length);

            clone.mVertex = vertex;
            clone.mColor = color;

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return clone;
    }

    /**
     * 仅移动图形
     * @param x
     * @param y
     * @param z
     */
    public void move(float x, float y, float z) {
        for (int i = 0; i < mVertex.length; i++) {
            if (i % 3 == 0) {//x
                mVertex[i] += x;
            }
            if (i % 3 == 1) {//y
                mVertex[i] += y;
            }
            if (i % 3 == 2) {//y
                mVertex[i] += z;
            }
        }
    }
    /**
     * 移动并创建新图形
     * @param x
     * @param y
     * @param z
     * @return
     */
    public BaseShape moveAndCreate(float x,float y,float z){
        BaseShape clone = clone();
        clone.move(x, y, z);
        return  clone;
    }

    public int getCount() {
        return mVertex.length /3;
    }
    public float[] getmVertex() {
        return mVertex;
    }

    public void setmVertex(float[] mVertex) {
        this.mVertex = mVertex;
    }

    public float[] getmColor() {
        return mColor;
    }

    public void setmColor(float[] mColor) {
        this.mColor = mColor;
    }

    public int getmDrawType() {
        return mDrawType;
    }

    public void setmDrawType(int mDrawType) {
        this.mDrawType = mDrawType;
    }
}
