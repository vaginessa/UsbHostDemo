package com.ivan.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ivan.tool.IniFile;
import com.ivan.usbhost.R;

import java.io.File;

import static java.lang.Thread.sleep;

/**
 * 项目名称：UsbHostDemo
 * 类描述：在activity中实现USB数据读取功能
 * 创建人：Michael-hj
 * 创建时间：2016/4/28 0028 14:58
 * 修改人：Michael-hj
 * 修改时间：2016/4/28 0028 14:58
 * 修改备注：
 */
public class UsbFilesActivity extends Activity {
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (TextView) findViewById(R.id.info);
        registerReceivers();
    }

    private static final int REQUEST_EX = 1;
    private static final String TAG = "UsbFilesActivity";
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

    private BroadcastReceiver externalStorageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final String path = intent.getData().getPath();
            final boolean value = intent.getBooleanExtra("read-only", true);

            Log.d(TAG, "receive action = " + action);
            Log.d(TAG, "external storage path = " + path);
            Log.d(TAG, "external storage value = " + value);
            //===================================================================================================
            if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
                Toast.makeText(UsbFilesActivity.this, "sd 卡不可用  path=======" + path, Toast.LENGTH_SHORT).show();
                // sd 卡不可用
                text.setText("sd 卡不可用");
            } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)) {
                // sd 卡已经被移除卡槽
                Toast.makeText(UsbFilesActivity.this, "sd 卡已经被移除卡槽 path=======" + path, Toast.LENGTH_SHORT).show();
            } else if (Intent.ACTION_MEDIA_SHARED.equals(action)) {
                // 选择通过 usb 共享
                Toast.makeText(UsbFilesActivity.this, "选择通过 usb 共享 path=======" + path, Toast.LENGTH_SHORT).show();
            } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                String fileNamePath = path + "/System/system.ini";
                //  openExDialog(path);
                // sd 卡可用
                Toast.makeText(UsbFilesActivity.this, " sd 卡可用 path=======" + path, Toast.LENGTH_SHORT).show();
                if (runnable == null)
                    runnable = new MyRunnable();
                new Thread(runnable.setFileUrl(fileNamePath)).start();
            }
        }
    };


    /**
     * 开启子线程进行文件扫描
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
            if (!TextUtils.isEmpty(fileNamePath) && (fileNamePath.contains("system.ini") || fileNamePath.contains("System.ini"))) {
                File file = new File(fileNamePath);
                //文件是否存在
                if (file.exists()) {
                    IniFile iniFile = new IniFile(file);
                    if (iniFile != null && iniFile.get("info") != null) {
                        IniFile.Section section = iniFile.get("info");
                        upDateText("fileNamePath: " + fileNamePath +
                                "\n" + "----------------Device=" + section.get("Device") +
                                "\n" + "----------------Version=" + section.get("Version") +
                                "\n" + "----------------Address=" + section.get("Address"));
                    } else {
                        upDateText("fileNamePath: " + fileNamePath);
                    }
                } else if (index < 10) {
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    run();
                } else if (index == 10)
                    Toast.makeText(UsbFilesActivity.this, "信息读取失败", Toast.LENGTH_SHORT).show();
                index++;
            } else {
                upDateText("fileNamePath: " + fileNamePath);
            }
        }
    }

    /**
     * 更新TextView
     *
     * @param data
     */
    private void upDateText(String data) {
        Message msg = new Message();
        msg.obj = data;
        handler.sendMessage(msg);
    }

    /**
     * 通过主线程的handle进行界面更新
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            text.setText((String) msg.obj);
        }
    };

    /**
     * 打开文件管理器
     *
     * @param path
     */
    private void openExDialog(String path) {
        Intent intents = new Intent();
        intents.putExtra("explorer_title", getString(R.string.dialog_read_from_dir));
        intents.setDataAndType(Uri.fromFile(new File(path)), "*/*");
        intents.setClass(UsbFilesActivity.this, ExDialogActivity.class);
        startActivityForResult(intents, REQUEST_EX);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_EX) {
                Uri uri = data.getData();
                path = data.getStringExtra("path");
                TextView text = (TextView) findViewById(R.id.info);
                if (!TextUtils.isEmpty(path) && (path.contains("system.ini") || path.contains("System.ini"))) {
                    IniFile file = new IniFile(new File(path));
                    if (file != null && file.get("info") != null) {
                        IniFile.Section section = file.get("info");
                        text.setText("select: " + uri + "----------------Device=" + section.get("Device") + "----------------Version=" + section.get("Version") + "----------------Address=" + section.get("Address"));
                    } else text.setText("select: " + uri);
                } else text.setText("select: " + uri);
            }
        }
    }
}
