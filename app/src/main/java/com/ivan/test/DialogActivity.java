package com.ivan.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.adutils.ABLogUtil;
import com.ivan.usbhost.R;
import com.ivan.usbhost.USBModular;

import java.util.ArrayList;

/**
 * 项目名称：UsbHostDemo
 * 类描述：在Service中实现USB数据读取功能 在此界面显示
 * 创建人：Michael-hj
 * 创建时间：2016/4/29 0029 11:25
 * 修改人：Michael-hj
 * 修改时间：2016/4/29 0029 11:25
 * 修改备注：
 */
public class DialogActivity extends Activity implements View.OnClickListener {
    private TextView text;
    private USBModular usbModular;
    private MypopuWindow mypopuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text = (TextView) findViewById(R.id.info);
        text.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info:
//                AbCircularColorPopupWindow.getInstance(this).openPopupWindow(true, text, new AbCircularColorPopupWindow.MyItemClickListener() {
//                    @Override
//                    public void onItemSelected(int index) {
//
//                    }
//                });
                ArrayList<String> testList = new ArrayList<String>();
                testList.add("钢笔");
                testList.add("荧光笔");
                testList.add("粉笔");
                AbListPopupWindow.getInstance(this).openPopupWindow(false, text, 14, testList, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        ABLogUtil.i("i=====" + i);
                    }
                });
                break;
            default:
                break;
        }
    }
}
