package com.ivan.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ivan.usbhost.R;


/**
 * 项目名称：JKP
 * 类描述：
 * 创建人：Michael
 * 创建时间：2016/2/2 15:38
 * 修改人：Michael
 * 修改时间：2016/2/2 15:38
 * 修改备注：
 */
public abstract class BaseCircular extends RelativeLayout {

    public BaseCircular(Context context) {
        this(context, (AttributeSet) null);
    }

    public BaseCircular(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initViews();
        if (null != attrs) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.BaseCircular);

            try {
                int size = arr.getInt(R.styleable.BaseCircular_circular_size, 0);
                this.setSize(size);
                int circularColor = arr.getColor(R.styleable.BaseCircular_circular_drawable, getResources().getColor(R.color.cl_Red));
                int circularColor_bg = arr.getColor(R.styleable.BaseCircular_circular_drawable_bg, getResources().getColor(R.color.cl_Red));
                this.setCircularColorDrawable(circularColor, circularColor_bg);
            } finally {
                arr.recycle();
            }

        }
    }

    protected boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public abstract void setClickListener(OnClickListener var1);

    public abstract void setSize(int size);

    public void setCheck(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public abstract void setCircularColorDrawable(int drawable, int drawable_bg);


    abstract void initViews();
}
