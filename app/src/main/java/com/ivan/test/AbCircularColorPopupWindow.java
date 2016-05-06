package com.ivan.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ivan.usbhost.R;


/**
 * @author Michael_hj
 * @ClassName: AbCircularColorPopupWindow
 * @Description: TODO(画图板颜色选择框)
 * @date 2016-5-6 下午1:20:38
 */
public class AbCircularColorPopupWindow {
    private static Context context;
    public static AbCircularColorPopupWindow application;

    public AbCircularColorPopupWindow(Context c) {
    }

    public static AbCircularColorPopupWindow getInstance(Context c) {
        if (application == null)
            application = new AbCircularColorPopupWindow(c);
        context = c;
        return application;
    }

    private View mBaseView;
    private PopupWindow mPopupWindow;

    public AbCircularColorPopupWindow(Context context, final View baseView, MyItemClickListener onItemClickListener) {
        this.context = context;
        openPopupWindow(true, baseView, onItemClickListener);
    }

    /**
     * 开启下拉框
     *
     * @param isNewPopopWindow
     * @param baseView
     */
    public void openPopupWindow(boolean isNewPopopWindow, final View baseView, MyItemClickListener onItemClickListener) {
        if (this.mBaseView == null || isNewPopopWindow || !this.mBaseView.equals(baseView)) {
            if (mPopupWindow != null && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            mPopupWindow = null;
            InitPopupWindow(baseView);
        } else {
            if (mPopupWindow == null)
                InitPopupWindow(baseView);
            else if (mPopupWindow.isShowing())
                mPopupWindow.dismiss();
        }
        mPopupWindow.showAsDropDown(mBaseView, mBaseView.getWidth() + 5, -mBaseView.getHeight());
    }

    public void showPopupWindow() {
        if (mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        mPopupWindow.showAsDropDown(mBaseView, mBaseView.getWidth() + 5, -mBaseView.getHeight());
    }

    /**
     * 初始化下拉框
     *
     * @param baseView
     */
    private void InitPopupWindow(final View baseView) {
        this.mBaseView = baseView;
        LayoutInflater inflater = (LayoutInflater) baseView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.widget_cicular_popup, null, false);
        GridLayout gridLayout = (GridLayout) popView.findViewById(R.id.gridLayout);
        updateUI(gridLayout, 9, 3);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
    }

    /**
     * 当前选中小圆圈
     */
    private int indexCheck = 0;

    /**
     * 更新界面
     *
     * @param gridLayout
     * @param totleCircular
     * @param columnCount
     */
    private void updateUI(final GridLayout gridLayout, final int totleCircular, final int columnCount) {
        gridLayout.setColumnCount(columnCount);
        gridLayout.setBackgroundResource(R.color.cl_gray);
        if (gridLayout != null)
            gridLayout.removeAllViews();
        for (int i = 0; i < totleCircular; i++) {
            //当前行数
            int h = 0;
            final MCircular circular = new MCircular(context);
            circular.setIndex(i);
            circular.setCheck(i == indexCheck);
            if (columnCount != 0)
                h = i / columnCount;
            if (i == (0 + columnCount * h))
                circular.setCircularColorDrawable(R.drawable.circular_red, R.drawable.circular_red_b);
            if (i == (1 + columnCount * h))
                circular.setCircularColorDrawable(R.drawable.circular_blue, R.drawable.circular_blue_b);
            if (i == (2 + columnCount * h))
                circular.setCircularColorDrawable(R.drawable.circular_green, R.drawable.circular_green_b);
            gridLayout.addView(circular);
            circular.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    indexCheck = circular.getIndex();
                    if (myItemClickListener != null)
                        myItemClickListener.onItemSelected(circular.getIndex());
                    if (mPopupWindow.isShowing())
                        mPopupWindow.dismiss();
                }
            });
        }
    }

    public interface MyItemClickListener {
        /**
         * 当前选中的小圆信息
         *
         * @param index
         */
        void onItemSelected(int index);
    }

    /**
     * 数据读取接口对象
     */
    private MyItemClickListener myItemClickListener;

    public void onMyItemClick(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }
}
