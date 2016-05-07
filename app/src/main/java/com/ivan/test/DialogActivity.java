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
    private TextView text_a;
    private TextView text_b;
    private TextView text_c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        text_a = (TextView) findViewById(R.id.info_a);
        text_a.setOnClickListener(this);
        text_b = (TextView) findViewById(R.id.info_b);
        text_b.setOnClickListener(this);
        text_c = (TextView) findViewById(R.id.info_c);
        text_c.setOnClickListener(this);
    }

    ArrayList<String> testList;

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.info_a:
                AbCircularColorPopupWindow.getInstance(this).openPopupWindow(true, text_a, new AbCircularColorPopupWindow.MyItemClickListener() {
                    @Override
                    public void onItemSelected(int index) {

                    }
                });
                break;
            case R.id.info_b:
                testList = new ArrayList<String>();
                testList.add("钢笔");
                testList.add("荧光笔");
                testList.add("粉笔");
                AbListPopupWindow.getInstance(this).openPopupWindow(true, text_b, testList, new AbListPopupWindow.MyItemClickListener() {
                    @Override
                    public void onListItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ABLogUtil.i("i=====" + position);
                    }

                    @Override
                    public void onCircularItemSelected(int index) {
                        ABLogUtil.i("i=====" + index);
                    }
                });
                break;
            case R.id.info_c:
                testList = new ArrayList<String>();
                testList.add("橡皮");
                testList.add("(清空)");
                AbListPopupWindow.getInstance(this).openPopupWindow(false, text_c, testList, new AbListPopupWindow.MyItemClickListener() {
                    @Override
                    public void onListItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ABLogUtil.i("i=====" + position);
                    }

                    @Override
                    public void onCircularItemSelected(int index) {
                        ABLogUtil.i("i=====" + index);
                    }
                });
                break;
            default:
                break;
        }
    }
}
