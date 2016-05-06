package com.ivan.usbhost;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.ivan.tool.IniFile;

/**
 * 项目名称：UsbHostDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/5 0005 10:21
 * 修改人：Michael-hj
 * 修改时间：2016/5/5 0005 10:21
 * 修改备注：
 */
public class USBModular {

    private static Context context;
    private static USBModular application;

    /**
     * USBModular单例
     *
     * @param c
     * @return
     */
    public static USBModular getInstance(Context c) {
        context = c;
        if (application == null) {
            application = new USBModular();
        }
        InitData();
        return application;
    }

    /**
     * 初始化数据开启服务
     */
    private static void InitData() {
        //初始化数据
        init();
        //启动USB处理服务
        Intent intent = new Intent(context, USB_Service.class);
        context.startService(intent);
    }

    /**
     * 初始化
     */
    private static void init() {
        //====================设置广播接收器======================================
        IntentFilter filter = new IntentFilter();
        //USBservice广播接收器
        filter.addAction(USB_Service.SystemDataKey);
        context.registerReceiver(externalStorageReceiver, filter);
        //系统广播接收器
        IntentFilter systemFilter = new IntentFilter();
        systemFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        systemFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        systemFilter.addAction(Intent.ACTION_MEDIA_SHARED);
        // 必须添加，否则无法接收到广播
        systemFilter.addDataScheme("file");
        context.registerReceiver(systemBroadcastReceiver, systemFilter);
    }

    /**
     * USBservice广播接收器
     */
    private static BroadcastReceiver externalStorageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (USB_Service.SystemDataKey.equals(action)) {
                IniFile.Section section = (IniFile.Section) intent.getSerializableExtra("section");
                if (myCallBackListener != null) {
                    if (section != null)
                        myCallBackListener.callBack_OK(section);
                    else
                        myCallBackListener.callBack_Fail("数据读取失败");
                }
            }
        }
    };
    /**
     * 系统广播接收器
     */
    private static BroadcastReceiver systemBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (myCallBackListener != null) {
                if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
                    // sd 卡不可用
                    myCallBackListener.callBack_Fail("sd 卡不可用");
                } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)) {
                    // sd 卡已经被移除卡槽
                    myCallBackListener.callBack_Fail("sd 卡已经被移除卡槽");
                } else if (Intent.ACTION_MEDIA_SHARED.equals(action)) {
                    // 选择通过 usb 共享
                    myCallBackListener.callBack_Fail("选择通过 usb 共享");
                }
            }
        }
    };

    /**
     * 数据读取
     */
    public interface MyCallBackListener {
        /**
         * 数据读取成功
         *
         * @param section
         */
        void callBack_OK(IniFile.Section section);

        /**
         * 数据读取失败
         *
         * @param json
         */
        void callBack_Fail(String json);
    }

    /**
     * 数据读取接口对象
     */
    private static MyCallBackListener myCallBackListener;

    /**
     * 初始化数据接口
     *
     * @param myCallBackListener
     */
    public void setOnCallBackListener(MyCallBackListener myCallBackListener) {
        this.myCallBackListener = myCallBackListener;
    }
}
