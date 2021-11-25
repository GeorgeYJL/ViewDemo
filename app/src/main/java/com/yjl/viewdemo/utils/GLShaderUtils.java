package com.yjl.viewdemo.utils;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GLShaderUtils {


    /**
     * float数组缓冲数据
     *
     * @param vertexs 顶点
     * @return 获取浮点形缓冲数据
     */
    public static FloatBuffer getFloatBuffer(float[] vertexs) {
        FloatBuffer buffer;
        ///每个浮点数:坐标个数* 4字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertexs.length * 4);
        //使用本机硬件设备的字节顺序
        qbb.order(ByteOrder.nativeOrder());
        // 从字节缓冲区创建浮点缓冲区
        buffer = qbb.asFloatBuffer();
        // 将坐标添加到FloatBuffer
        buffer.put(vertexs);
        //设置缓冲区以读取第一个坐标
        buffer.position(0);
        return buffer;
    }

    public static  int loadAndInitProgramFromAssets(Context ctx, String vertName, String fragName,int glVersion){
        int program=-1;
        if(glVersion ==2){
            ////顶点着色
            int vertexShader = loadShaderAssets(ctx, GLES20.GL_VERTEX_SHADER, vertName,glVersion);
            //片元着色
            int fragmentShader = loadShaderAssets(ctx, GLES20.GL_FRAGMENT_SHADER, fragName,glVersion);
            program = GLES20.glCreateProgram();//创建空的OpenGL ES 程序
            if(program == 0){
                return -1;
            }
            GLES20.glAttachShader(program, vertexShader);//加入顶点着色器
            GLES20.glAttachShader(program, fragmentShader);//加入片元着色器

            GLES20.glLinkProgram(program);//创建可执行的OpenGL ES项目

            // 检查链接状态
            int[] linkedState = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkedState, 0);

            if (linkedState[0] == 0) {
                Log.e("ES20_ERROR", "链接 program错误: " + GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                return -1;
            }
        }else if(glVersion ==3){
            ////顶点着色
            int vertexShader = loadShaderAssets(ctx, GLES30.GL_VERTEX_SHADER, vertName,glVersion);
            //片元着色
            int fragmentShader = loadShaderAssets(ctx, GLES30.GL_FRAGMENT_SHADER, fragName,glVersion);
            program = GLES30.glCreateProgram();//创建空的OpenGL ES 程序
            if (program == 0) {
                return -1;
            }
            GLES30.glAttachShader(program, vertexShader);//加入顶点着色器
            GLES30.glAttachShader(program, fragmentShader);//加入片元着色器
            GLES30.glLinkProgram(program);//创建可执行的OpenGL ES项目

            // 检查链接状态
            int[] linkedState = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkedState, 0);

            if (linkedState[0] == 0) {
                Log.e("ES30_ERROR", "链接 program错误: " + GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                return -1;
            }
        }

        return program;
    }
    //从sh脚本中加载shader内容的方法
    private static int loadShaderAssets(Context ctx, int type, String name,int glVersion) {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int len;
        try (InputStream is = ctx.getAssets().open(name)) {
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadShader(type, sb.toString(),glVersion);
    }

    /**
     * @description 加载shader
     * @param type 类型 shaderCode 源码 glVersion 版本号
     * @return
     * @author yangjianglong
     * @time 2021/11/24 8:27 下午
     */
    private static int loadShader(int type, String shaderCode,int glVersion) {
        int shader = -1;
        if(glVersion ==2){
            shader = GLES20.glCreateShader(type);
            //加载失败直接返回
            if (shader == 0) {
                return 0;
            }
            GLES20.glShaderSource(shader,shaderCode);
            GLES20.glCompileShader(shader);

            //存放编译成功 shader 数量的数组
            int[] compiled = new int[1];
            //获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            //若编译失败则显示错误日志并删除此shader
            if (compiled[0] == 0) {
                Log.e("ES30_COMPILE_ERROR",
                        "无法编译着色器: " + GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }

        }else if(glVersion == 3){
            shader = GLES30.glCreateShader(type);
            //加载失败直接返回
            if (shader == 0) {
                return 0;
            }
            GLES30.glShaderSource(shader,shaderCode);
            GLES30.glCompileShader(shader);

            //存放编译成功 shader 数量的数组
            int[] compiled = new int[1];
            //获取Shader的编译情况
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            //若编译失败则显示错误日志并删除此shader
            if (compiled[0] == 0) {
                Log.e("ES30_COMPILE_ERROR",
                        "无法编译着色器: " + GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;

    }


}
