package com.ivan.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ivan.usbhost.R;

/**
 * 项目名称：UsbHostDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/6 0006 8:47
 * 修改人：Michael-hj
 * 修改时间：2016/5/6 0006 8:47
 * 修改备注：
 */
public class MCircular extends BaseCircular {
    private ImageView img_circular;
    private ImageView img_circular_bg;
    private int index;

    public MCircular(Context context) {
        super(context, null);
    }

    public MCircular(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    @Override
    public void setClickListener(OnClickListener onClickListener) {
    }

    @Override
    public void setCheck(boolean isChecked) {
        super.setCheck(isChecked);
        img_circular_bg.setVisibility(isChecked ? VISIBLE : INVISIBLE);
        img_circular.setVisibility(isChecked ? INVISIBLE : VISIBLE);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void setSize(int size) {
        LayoutParams ps = (LayoutParams) img_circular.getLayoutParams();
        ps.height = size;
        ps.width = size;
        img_circular.setLayoutParams(ps);
    }

    @Override
    public void setCircularColorDrawable(int drawable, int drawable_bg) {
        if (img_circular != null) {
            img_circular.setBackgroundResource(drawable);
            img_circular_bg.setBackgroundResource(drawable_bg);
        }
    }

    void initViews() {
        LayoutInflater.from(getContext()).inflate(R.layout.circular_lay, this);
        img_circular = (ImageView) findViewById(R.id.img_circular);
        img_circular_bg = (ImageView) findViewById(R.id.img_circular_bg);
    }
}
