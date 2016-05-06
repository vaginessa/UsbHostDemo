package com.ivan.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.adutils.ABLogUtil;
import com.ivan.tool.IniFile;
import com.ivan.usbhost.R;
import com.ivan.usbhost.USBModular;

/**
 * 项目名称：UsbHostDemo
 * 类描述：在Service中实现USB数据读取功能 在此界面显示
 * 创建人：Michael-hj
 * 创建时间：2016/4/29 0029 11:25
 * 修改人：Michael-hj
 * 修改时间：2016/4/29 0029 11:25
 * 修改备注：
 */
public class ABTextActivity extends Activity {
    private TextView text;
    private USBModular usbModular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (TextView) findViewById(R.id.info);
        init();
    }

    private void init() {
        Toast.makeText(this, " InitData()" + this, Toast.LENGTH_SHORT).show();
        usbModular = USBModular.getInstance(this);
        usbModular.setOnCallBackListener(new USBModular.MyCallBackListener() {
            public void callBack_OK(IniFile.Section section) {
                text.setText("----------------Device=" + section.get("Device") +
                        "\n" + "----------------Version=" + section.get("Version") +
                        "\n" + "----------------Address=" + section.get("Address"));
            }

            public void callBack_Fail(String json) {
                text.setText("数据读取失败:" + json);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbModular=null;
    }
}
