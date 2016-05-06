package com.ivan.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ivan.usbhost.R;

/**
 * 项目名称：UsbHostDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/5 0005 16:55
 * 修改人：Michael-hj
 * 修改时间：2016/5/5 0005 16:55
 * 修改备注：
 */
public class MypopuWindow extends PopupWindow {
    public MypopuWindow(Activity context) {
        super(context);
        init(context, null);
    }

    public MypopuWindow(Activity context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MypopuWindow(Activity context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public MypopuWindow(Activity context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Activity context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View conentView = inflater.inflate(R.layout.add_popup_dialog, null);
//        int h = context.getWindowManager().getDefaultDisplay().getHeight();
//        int w = context.getWindowManager().getDefaultDisplay().getWidth();
//        // 设置SelectPicPopupWindow的View
//        this.setContentView(conentView);
//        // 设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(w / 2 + 50);
//        // 设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
//        this.setOutsideTouchable(true);
//        // 刷新状态
//        this.update();
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
//        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.AnimationPreview);
//        GridLayout gridLayout = (GridLayout) conentView.findViewById(R.id.gridLayout);
//        for (int i = 0; i < 9; i++) {
//            ImageView imageView = new ImageView(context);
//            gridLayout.addView(imageView);
//        }
    }
}
