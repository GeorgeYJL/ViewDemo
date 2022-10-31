package com.yjl.viewdemo.ftp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

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

/**
 * @Project：ViewDemo
 * @Package：com.yjl.viewdemo.ftp
 * @CreateDate:2022/3/11
 * @author: yangjianglong
 */
public class ftpHelper {

    private static final String TAG = "ftpHelper";

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    private static boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

    public static String getAppExternalStoragePath(Context context) {
        if(isExternalStorageWritable()){
//            File[] externalStorageVolumes = ContextCompat.getExternalFilesDirs(context, null);
//            File primaryExternalStorage = externalStorageVolumes[0];
            return  context.getExternalFilesDir(null).getAbsolutePath();
        }
        return null;
    }
    public static File createConfigFile(String path,String name) {
        String str = "ftpserver.user.admin.userpassword=21232F297A57A5A743894A0E4A801FC3\n" +
                "ftpserver.user.admin.homedirectory=/storage/emulated/0/Android/data/com.yjl.viewdemo/files\n"+
                "ftpserver.user.admin.enableflag=true\n"+
                "ftpserver.user.admin.writepermission=true\n"+
                "ftpserver.user.admin.maxloginnumber=0\n"+
                "ftpserver.user.admin.maxloginperip=0\n"+
                "ftpserver.user.admin.idletime=0\n"+
                "ftpserver.user.admin.uploadrate=0\n"+
                "ftpserver.user.admin.downloadrate=0\n" +
                "ftpserver.user.admin.username=admin\n";
//                "ftpserver.user.anonymous.userpassword=admin\n"+
//                "ftpserver.user.anonymous.homedirectory=/storage/emulated/0/Android/data/com.yjl.viewdemo/files\n"+
//                "ftpserver.user.anonymous.enableflag=true\n"+
//                "ftpserver.user.anonymous.writepermission=true\n"+
//                "ftpserver.user.anonymous.maxloginnumber=20\n"+
//                "ftpserver.user.anonymous.maxloginperip=2\n"+
//                "ftpserver.user.anonymous.idletime=300\n"+
//                "ftpserver.user.anonymous.uploadrate=4800\n"+
//                "ftpserver.user.anonymous.downloadrate=4800\n";

        File file = null;
        try {
            file = new File(path,name);
            if(file.exists()){
                return file;
            }else{
                file.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = str.getBytes(StandardCharsets.UTF_8);
            fos.write(buffer,0,buffer.length);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static FtpServer ConfigAndCreateFtpServer(File file){
        if(!file.exists()){
            return null;
        }

        FtpServerFactory serverFactory = new FtpServerFactory();
        ListenerFactory factory = new ListenerFactory();
        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();

        userManagerFactory.setFile(file);
        serverFactory.setUserManager(userManagerFactory.createUserManager());
        // set the port of the listener
        factory.setPort(2221);

        // replace the default listener
        serverFactory.addListener("default", factory.createListener());

        // start the server
        FtpServer server = serverFactory.createServer();

        try {
            server.start();
            return server;
        } catch (FtpException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getLocalIpAddress() {
        String strIP=null;
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        strIP= inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
        }
        return strIP;
    }

//    private void copyResourceFile(int rid, String targetFile){
//        InputStream fin = ((Context)this).getResources().openRawResource(rid);
//        FileOutputStream fos=null;
//        int length;
//        try {
//            fos = new FileOutputStream(targetFile);
//            byte[] buffer = new byte[1024];
//            while( (length = fin.read(buffer)) != -1){
//                fos.write(buffer,0,length);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally{
//            if(fin!=null){
//                try {
//                    fin.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if(fos!=null){
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//

//
//    void Config2(){
////      Now, let's make it possible for a client to use FTPS (FTP over SSL) for the default listener.
//
//
//        FtpServerFactory serverFactory = new FtpServerFactory();
//
//        ListenerFactory factory = new ListenerFactory();
//
//        // set the port of the listener
//        factory.setPort(2221);
//
//        // define SSL configuration
//        SslConfigurationFactory ssl = new SslConfigurationFactory();
//        ssl.setKeystoreFile(new File(ftpConfigDir+"ftpserver.jks"));
//        ssl.setKeystorePassword("password");
//
//        // set the SSL configuration for the listener
//        factory.setSslConfiguration(ssl.createSslConfiguration());
//        factory.setImplicitSsl(true);
//
//        // replace the default listener
//        serverFactory.addListener("default", factory.createListener());
//
//        PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
//        userManagerFactory.setFile(new File(ftpConfigDir+ "raw/users.properties"));
//
//        serverFactory.setUserManager(userManagerFactory.createUserManager());
//
//        // start the server
//        FtpServer server = serverFactory.createServer();
//        this.mFtpServer = server;
//        try {
//            server.start();
//        } catch (FtpException e) {
//            e.printStackTrace();
//        }
//    }
}
