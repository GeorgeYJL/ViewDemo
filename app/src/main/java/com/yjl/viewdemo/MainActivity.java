package com.yjl.viewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yjl.viewdemo.cameraview.Camera1SurfaceView;
import com.yjl.viewdemo.cameraview.Camera2GLSurfaceView;
import com.yjl.viewdemo.cameraview.Camera2SurfaceView;
import com.yjl.viewdemo.videoview.GL20VideoView;
import com.yjl.viewdemo.videoview2.GL30VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//        Camera1SurfaceView camerView1 = new Camera1SurfaceView(this);
//        setContentView(camerView1);

//        Camera2SurfaceView camerView2 = new Camera2SurfaceView(this);
//        setContentView(camerView2);

//        Camera2GLSurfaceView camerView2 = new Camera2GLSurfaceView(this);
//        setContentView(camerView2);

        GL20VideoView view = new GL20VideoView(this);

//        GL30VideoView view = new GL30VideoView(this);
        setContentView(view);
    }
}