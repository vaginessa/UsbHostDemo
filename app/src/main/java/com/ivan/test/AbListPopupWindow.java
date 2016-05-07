package com.ivan.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.adutils.ABLogUtil;
import com.adutils.phone.ABDensityUtil;
import com.ivan.usbhost.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class AbListPopupWindow implements OnItemClickListener {
    private static Context context;
    private static boolean isNeedCircular;
    public static AbListPopupWindow application;

    public AbListPopupWindow(Context c) {
        context = c;
    }

    public static AbListPopupWindow getInstance(Context c) {
        if (application == null)
            application = new AbListPopupWindow(c);
        context = c;
        return application;
    }

    private View mBaseView;
    private PopupWindow mPopupWindow;
    private ListView mLV_pop;
    private InsideListAdapter mInsideListAdapter;


    public AbListPopupWindow(Context context, boolean isNeedCircular, final View baseView, int textSize, List<String> listString, int height, MyItemClickListener myItemClickListener) {
        this.context = context;
        openPopupWindow(isNeedCircular, baseView, listString, myItemClickListener);
    }

    public AbListPopupWindow(Context context, boolean isNeedCircular, final View baseView, int baseViewWidth, int baseViewHeight, MyItemClickListener myItemClickListener) {
        this.context = context;
        InitPopupWindow(isNeedCircular, baseView, myItemClickListener);
    }

    /**
     * 开启下拉框
     *
     * @param isNewPopopWindow    是否新建一个pop
     * @param baseView            the baseView
     * @param listString          the listString
     * @param onItemClickListener the onItemClickListener
     */
    public void openPopupWindow(boolean isNeedCircular, final View baseView, List<String> listString, MyItemClickListener myItemClickListener) {
        if (this.mBaseView == null || !this.mBaseView.equals(baseView)) {
            if (mPopupWindow != null && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            mPopupWindow = null;
            InitPopupWindow(isNeedCircular, baseView, myItemClickListener);
        } else {
            if (mPopupWindow == null)
                InitPopupWindow(isNeedCircular, baseView, myItemClickListener);
            else if (mPopupWindow.isShowing())
                mPopupWindow.dismiss();
        }
        //数据更新=======================================
        if (isNeedCircular)
            addCircular();
        setStringData(listString);
        mInsideListAdapter.notifyDataSetChanged();
        mPopupWindow.showAsDropDown(mBaseView, mBaseView.getWidth() + 5, -mBaseView.getHeight());
    }

    private View popView;

    /**
     * 初始化下拉框
     *
     * @param isNeedCircular
     * @param baseView            the baseView
     * @param onItemClickListener the onItemClickListener
     */
    private void InitPopupWindow(boolean isNeedCircular, final View baseView, MyItemClickListener myItemClickListener) {
        this.context = context;
        this.mBaseView = baseView;
        this.isNeedCircular = isNeedCircular;
        this.myItemClickListener = myItemClickListener;
        LayoutInflater inflater = (LayoutInflater) baseView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.widget_popupwindow, null, false);
        mLV_pop = (ListView) popView.findViewById(R.id.popupwindow_list);
        if (isNeedCircular) {
            popView.findViewById(R.id.circular_gl).setVisibility(View.VISIBLE);
            popView.findViewById(R.id.line).setVisibility(View.VISIBLE);
            addCircular();
        } else {
            popView.findViewById(R.id.circular_gl).setVisibility(View.GONE);
            popView.findViewById(R.id.line).setVisibility(View.GONE);
        }
        mLV_pop.setOnItemClickListener(this);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mInsideListAdapter = new InsideListAdapter(baseView.getContext());
        mLV_pop.setAdapter(mInsideListAdapter);
    }

    private int circularIndex = 1;

    /**
     * 小圆相关
     *
     * @param popView
     */
    private void addCircular() {
        LinearLayout circular_gl = (LinearLayout) popView.findViewById(R.id.circular_gl);
        if (circular_gl != null)
            circular_gl.removeAllViews();
        for (int i = 1; i < 4; i++) {
            final MCircular circular = new MCircular(context);
            circular.setIndex(i);
            circular.setCheck(i == circularIndex);
            circular.setSize(ABDensityUtil.dip2px(context, 10 + i * 10));
            circular.setCircularColorDrawable(R.drawable.circular_gray, R.drawable.circular_gray_b);
            circular_gl.addView(circular);
            circular.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    circularIndex = circular.getIndex();
                    if (myItemClickListener != null)
                        myItemClickListener.onCircularItemSelected(circular.getIndex());
                    if (mPopupWindow.isShowing())
                        mPopupWindow.dismiss();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listItemIndex = position;
        if (myItemClickListener != null)
            myItemClickListener.onListItemSelected(parent, view, position, id);
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }

    public void setEntityData(List<MyPopupWindowData> myPopupWindowDatas) {
        setData(myPopupWindowDatas, 0);
    }

    public void setStringData(List<String> listString) {
        setData(getEntityData(listString), 0);
    }

    /**
     * 获取数据（数据组装）
     *
     * @param listString
     * @return
     */
    private ArrayList<MyPopupWindowData> getEntityData(List<String> listString) {
        ArrayList<MyPopupWindowData> myPopupWindowDatas = new ArrayList<MyPopupWindowData>();
        if (listString == null || listString.size() < 1)
            return new ArrayList<MyPopupWindowData>();
        MyPopupWindowData data;
        for (int i = 0; i < listString.size(); i++) {
            data = new MyPopupWindowData();
            if (!isNeedCircular && i == (listString.size() - 1))
                data.setHaveImg(false);
            data.setmPopItemName(listString.get(i) + "");
            data.setmPopItemCode(i);
            myPopupWindowDatas.add(data);
        }
        return myPopupWindowDatas;
    }

    /**
     * 设置数据
     *
     * @param myPopupWindowDatas
     * @param defaultIndex
     */
    public void setData(List<MyPopupWindowData> myPopupWindowDatas, int defaultIndex) {
        if (myPopupWindowDatas != null && myPopupWindowDatas.size() != 0) {
            mInsideListAdapter.setData(myPopupWindowDatas);
            mInsideListAdapter.notifyDataSetChanged();
            showPopupWindow();
        }
    }

    /**
     * 是否显示mPopupWindow
     */
    public void showPopupWindow() {
        if (mPopupWindow.isShowing())
            mPopupWindow.dismiss();
        mInsideListAdapter.notifyDataSetChanged();
        mPopupWindow.showAsDropDown(mBaseView, mBaseView.getWidth() + 5, -mBaseView.getHeight());
    }

    /**
     * MyPopupWindow数据实体类
     */
    public static class MyPopupWindowData implements Serializable {
        public String mPopItemName = "";
        public int mPopItemCode = 0;
        public boolean haveImg = true;

        public int getmPopItemCode() {
            return mPopItemCode;
        }

        public void setmPopItemCode(int mPopItemCode) {
            this.mPopItemCode = mPopItemCode;
        }

        public String getmPopItemName() {
            return mPopItemName;
        }

        public void setmPopItemName(String mPopItemName) {
            this.mPopItemName = mPopItemName;
        }

        public boolean isHaveImg() {
            return haveImg;
        }

        public void setHaveImg(boolean haveImg) {
            this.haveImg = haveImg;
        }
    }

    private int listItemIndex = 1;

    /**
     * MyPopupWindow列表数据适配器
     */
    private class InsideListAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater;
        private List<MyPopupWindowData> myPopupWindowDatas;

        public InsideListAdapter(Context context) {
            layoutInflater = LayoutInflater.from(context);
        }

        public void setData(List<MyPopupWindowData> myPopupWindowDatas) {
            this.myPopupWindowDatas = myPopupWindowDatas;
        }

        public int getCount() {
            if (myPopupWindowDatas == null)
                return 0;
            return myPopupWindowDatas.size();
        }

        public MyPopupWindowData getItem(int position) {
            return myPopupWindowDatas.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        private class ViewHoler {
            ImageView im_pop_item;
            TextView tv_pop_item;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHoler();
                convertView = LayoutInflater.from(context).inflate(R.layout.widget_popup_list_item_lay, null, true);
                viewHolder.im_pop_item = (ImageView) convertView.findViewById(R.id.pop_item_iv);
                viewHolder.tv_pop_item = (TextView) convertView.findViewById(R.id.pop_item_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHoler) convertView.getTag();
            }
            ABLogUtil.i("listItemIndex:=============" + listItemIndex);
            if (isNeedCircular) {
                viewHolder.tv_pop_item.setTextColor(context.getResources().getColor(R.color.font_style_colors));
                convertView.setBackgroundResource(position == listItemIndex ? R.color.pen_item_h : R.color.pen_item_n);
            } else {
                viewHolder.tv_pop_item.setTextColor(context.getResources().getColor(R.color.cl_black));
                convertView.setBackgroundResource((position == listItemIndex ? R.color.eraser_item_h : R.color.eraser_item_n));
            }
            viewHolder.im_pop_item.setVisibility(getItem(position).haveImg ? View.VISIBLE : View.INVISIBLE);
            viewHolder.tv_pop_item.setText(getItem(position).mPopItemName);
            return convertView;
        }
    }

    public interface MyItemClickListener {
        /**
         * listView item选中信息
         *
         * @param index
         */
        void onListItemSelected(AdapterView<?> parent, View view, int position, long id);

        /**
         * 当前选中的小圆信息
         *
         * @param index
         */
        void onCircularItemSelected(int index);
    }

    /**
     * 数据读取接口对象
     */
    private MyItemClickListener myItemClickListener;

    public void onMyItemClick(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }
}
