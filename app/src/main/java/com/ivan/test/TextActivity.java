package com.ivan.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.ivan.tool.IniFile;
import com.ivan.usbhost.R;
import com.ivan.usbhost.USB_Service;

/**
 * 项目名称：UsbHostDemo
 * 类描述：在Service中实现USB数据读取功能 在此界面显示
 * 创建人：Michael-hj
 * 创建时间：2016/4/29 0029 11:25
 * 修改人：Michael-hj
 * 修改时间：2016/4/29 0029 11:25
 * 修改备注：
 */
public class TextActivity extends Activity {
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //启动USB处理服务
        Intent intent = new Intent(this, USB_Service.class);
        startService(intent);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        text = (TextView) findViewById(R.id.info);
        //====================设置广播接收器======================================
        IntentFilter filter = new IntentFilter();
        //USBservice广播接收器
        filter.addAction(USB_Service.SystemDataKey);
        registerReceiver(externalStorageReceiver, filter);
        //系统广播接收器
        IntentFilter systemFilter = new IntentFilter();
        systemFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        systemFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        systemFilter.addAction(Intent.ACTION_MEDIA_SHARED);
        // 必须添加，否则无法接收到广播
        systemFilter.addDataScheme("file");
        registerReceiver(systemBroadcastReceiver, systemFilter);
    }

    /**
     * USBservice广播接收器
     */
    private BroadcastReceiver externalStorageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (USB_Service.SystemDataKey.equals(action)) {
                IniFile.Section section = (IniFile.Section) intent.getSerializableExtra("section");
                if (section != null)
                    text.setText("----------------Device=" + section.get("Device") +
                            "\n" + "----------------Version=" + section.get("Version") +
                            "\n" + "----------------Address=" + section.get("Address"));
                else text.setText("数据读取失败");
            }
        }
    };
    /**
     * 系统广播接收器
     */
    private BroadcastReceiver systemBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
                // sd 卡不可用
                text.setText("sd 卡不可用");
            } else if (Intent.ACTION_MEDIA_REMOVED.equals(action)) {
                // sd 卡已经被移除卡槽
                text.setText("sd 卡已经被移除卡槽");
            } else if (Intent.ACTION_MEDIA_SHARED.equals(action)) {
                // 选择通过 usb 共享
                text.setText("选择通过 usb 共享");
            }
        }
    };

}
