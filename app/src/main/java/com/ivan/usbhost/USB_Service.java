package com.ivan.usbhost;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.adutils.ABLogUtil;
import com.ivan.tool.IniFile;

import java.io.File;

import static java.lang.Thread.sleep;

/**
 * 项目名称：UsbHostDemo
 * 类描述：在Service中实现USB数据读取功能
 * 创建人：Michael-hj
 * 创建时间：2016/5/4 0004 18:04
 * 修改人：Michael-hj
 * 修改时间：2016/5/4 0004 18:04
 * 修改备注：
 */
public class USB_Service extends Service {
    private static final String TAG = "USB_Service";
    /**
     * 广播Action Key
     */
    public static final String SystemDataKey = "USB_Service.SystemData";
    /**
     * 文件相对路径
     */
    private String fileName = "system.ini";
    private String filePath = "/System/" + getFileName();

    /**
     * System文件名
     *
     * @return
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * System文件名
     *
     * @param fileName
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取文件相对路径
     *
     * @return
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * 设置文件相对路径
     *
     * @param filePath
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate------");
        registerReceivers();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy------");
        super.onDestroy();
        unregisterReceivers();
    }

    /**
     * 取消注册
     */
    private void unregisterReceivers() {
        if (externalStorageReceiver != null) {
            unregisterReceiver(externalStorageReceiver);
            externalStorageReceiver = null;
        }
    }

    /**
     * 注册广播
     */
    private void registerReceivers() {
        //============================================广播标签设置=======================================================
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_BUTTON);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_NOFS);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        filter.addAction(Intent.ACTION_MEDIA_SCANNER_STARTED);
        filter.addAction(Intent.ACTION_MEDIA_SHARED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        // 必须添加，否则无法接收到广播
        filter.addDataScheme("file");
        //===================================================================================================
        registerReceiver(externalStorageReceiver, filter);
    }

    /**
     * 系统USB拔插广播接受者
     */
    private BroadcastReceiver externalStorageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final String path = intent.getData().getPath();
            final boolean value = intent.getBooleanExtra("read-only", true);

            ABLogUtil.i("receive action = " + action);
            ABLogUtil.i( "external storage path = " + path);
            ABLogUtil.i( "external storage value = " + value);
            //===================================================================================================
            if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
                // sd 卡不可用
                Toast.makeText(USB_Service.this, "sd 卡不可用", Toast.LENGTH_SHORT).show();
            } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)) {
                // sd 卡已经被移除卡槽
                Toast.makeText(USB_Service.this, "sd 卡已经被移除卡槽", Toast.LENGTH_SHORT).show();
            } else if (Intent.ACTION_MEDIA_SHARED.equals(action)) {
                // 选择通过 usb 共享
                Toast.makeText(USB_Service.this, "选择通过 usb 共享", Toast.LENGTH_SHORT).show();
            } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                // sd 卡可用
                Toast.makeText(USB_Service.this, " sd 卡可用 ", Toast.LENGTH_SHORT).show();
                if (runnable == null)
                    //初始化文件扫描线程
                    runnable = new MyRunnable();
                //启动文件扫描线程
                new Thread(runnable.setFileUrl(path + getFilePath())).start();
            }
        }
    };


    /**
     * 文件扫描子线程
     */
    private MyRunnable runnable = null;

    /**
     * 自定义文件读取Runnable
     */
    protected class MyRunnable implements Runnable {
        private int index = 0;
        private String fileNamePath = "";

        public MyRunnable setFileUrl(String fileNamePath) {
            this.fileNamePath = fileNamePath;
            return runnable;
        }

        public void run() {
            if (!TextUtils.isEmpty(fileNamePath) && (fileNamePath.contains(getFileName()) || fileNamePath.contains(getFileName()))) {
                File file = new File(fileNamePath);
                //文件是否存在
                if (file.exists()) {
                    IniFile iniFile = new IniFile(file);
                    if (iniFile != null && iniFile.get("info") != null) {
                        IniFile.Section section = iniFile.get("info");
                        upDateData(true, section, "fileNamePath: " + fileNamePath);
                    } else {
                        upDateData(false, null, "fileNamePath: " + fileNamePath);
                    }
                } else if (index < 10) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    run();
                } else if (index == 10) {
                    upDateData(false, null, "fileNamePath: " + fileNamePath);
                    Toast.makeText(USB_Service.this, "信息读取失败", Toast.LENGTH_SHORT).show();
                }
                index++;
            } else {
                upDateData(false, null, "fileNamePath: " + fileNamePath);
            }
        }
    }

    /**
     * 数据读取成功与否的处理
     *
     * @param isSuccess
     * @param section
     * @param ErrorMsg
     */
    private void upDateData(boolean isSuccess, IniFile.Section section, String ErrorMsg) {
        ABLogUtil.i("upDateData:===========" + section);
        Intent intent = new Intent();
        intent.setAction(SystemDataKey);
        intent.putExtra("section", isSuccess ? section : new IniFile.Section());
        sendBroadcast(intent); //发送广播
    }
}