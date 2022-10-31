package com.yjl.viewdemo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.yjl.viewdemo.cube3d.CubeHolographicView;
import com.yjl.viewdemo.ftp.ftpHelper;
import com.yjl.viewdemo.gl20.glworld.GLWorld;
import com.yjl.viewdemo.myglsurfaceview.MyGLSurfaceView;
import com.yjl.viewdemo.videoview.GL20VideoView;
import com.yjl.viewdemo.videoview2.GL30VideoView;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.ssl.SslConfigurationFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@RequiresApi(api = Build.VERSION_CODES.R)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "YYYYY";
    private FtpServer mFtpServer;
    private String ftpConfigDir;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mFtpServer) {
            mFtpServer.stop();
            mFtpServer = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Camera1SurfaceView camerView1 = new Camera1SurfaceView(this);
//        setContentView(camerView1);

//        Camera2SurfaceView camerView2 = new Camera2SurfaceView(this);
//        setContentView(camerView2);

//        Camera2GLSurfaceView camerView2 = new Camera2GLSurfaceView(this);
//        setContentView(camerView2);

//        GL20VideoView view = new GL20VideoView(this);
//        GL30VideoView view = new GL30VideoView(this);

//        MyGLSurfaceView view = new MyGLSurfaceView(this);
//        setContentView(view);

//        GLWorld view = new GLWorld(this);


        CubeHolographicView view = new CubeHolographicView(this);
        setContentView(view);




//        if(Environment.isExternalStorageManager()){
//            Log.d(TAG, "isExternalStorageManager: true");
//        }else{
//            Intent intent = new Intent();
//            intent.setAction( Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
//            this.startActivity(intent);
//        }


//        ftpConfigDir = ftpHelper.getAppExternalStoragePath(getApplicationContext());
//        Log.d(TAG, "onCreate: "+ ftpConfigDir);
//
//
//        File file = ftpHelper.createConfigFile(ftpConfigDir,"user.properties");
//
//        mFtpServer = ftpHelper.ConfigAndCreateFtpServer(file);
//
//        if(mFtpServer!= null){
//            TextView tv=(TextView)findViewById(R.id.text);
//            String info="请通过浏览器或者我的电脑访问以下地址\n"+"ftp://"+ftpHelper.getLocalIpAddress()+":2221\n";
//            tv.setText(info);
//        }







    }



}