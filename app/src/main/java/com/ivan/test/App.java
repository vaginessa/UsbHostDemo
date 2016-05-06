package com.ivan.test;

import android.app.Application;
import android.content.Intent;

import com.ivan.usbhost.USB_Service;

/**
 * 项目名称：UsbHostDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/5 0005 11:30
 * 修改人：Michael-hj
 * 修改时间：2016/5/5 0005 11:30
 * 修改备注：
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent =new Intent(this, USB_Service.class);
        startService(intent);
    }
}
