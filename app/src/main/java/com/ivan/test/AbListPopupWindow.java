package com.ivan.test;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

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
    }

    public static AbListPopupWindow getInstance(Context c) {
        if (application == null)
            application = new AbListPopupWindow(c);
        context = c;
        return application;
    }

    private View mBaseView;
    private OnItemClickListener mOnItemClickListener;
    private PopupWindow mPopupWindow;
    private ListView mLV_pop;
    private InsideListAdapter mInsideListAdapter;
    private float TextSize = 14;
    private int TextGravity = Gravity.CENTER;

    public float getTextSize() {
        return TextSize;
    }

    public void setTextSize(float textSize) {
        TextSize = ABDensityUtil.px2sp(context, textSize);
    }

    public int getTextGravity() {
        return TextGravity;
    }

    public void setTextGravity(int textGravity) {
        TextGravity = textGravity;
    }

    public AbListPopupWindow(Context context, boolean isNeedCircular, final View baseView, int textSize, List<String> listString, int height, OnItemClickListener onItemClickListener) {
        this.context = context;
        openPopupWindow(isNeedCircular, baseView, textSize, listString, onItemClickListener);
    }

    public AbListPopupWindow(Context context, boolean isNeedCircular, final View baseView, int baseViewWidth, int baseViewHeight, OnItemClickListener onItemClickListener) {
        this.context = context;
        InitPopupWindow(isNeedCircular, baseView, onItemClickListener);
    }

    /**
     * 开启下拉框
     *
     * @param isNewPopopWindow    是否新建一个pop
     * @param baseView            the baseView
     * @param textSize            the textSize
     * @param listString          the listString
     * @param onItemClickListener the onItemClickListener
     */
    public void openPopupWindow(boolean isNeedCircular, final View baseView, int textSize, List<String> listString, OnItemClickListener onItemClickListener) {
        if (this.mBaseView == null || !this.mBaseView.equals(baseView)) {
            if (mPopupWindow != null && mPopupWindow.isShowing())
                mPopupWindow.dismiss();
            mPopupWindow = null;
            InitPopupWindow(isNeedCircular, baseView, onItemClickListener);
        } else {
            if (mPopupWindow == null)
                InitPopupWindow(isNeedCircular, baseView, onItemClickListener);
            else if (mPopupWindow.isShowing())
                mPopupWindow.dismiss();
        }
        setStringData(listString);
        mInsideListAdapter.notifyDataSetChanged();
        mPopupWindow.showAsDropDown(mBaseView, mBaseView.getWidth() + 5, -mBaseView.getHeight());
    }

    /**
     * 初始化下拉框
     *
     * @param baseView            the baseView
     * @param onItemClickListener the onItemClickListener
     */
    private void InitPopupWindow(boolean isNeedCircular, final View baseView, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.mBaseView = baseView;
        this.isNeedCircular = isNeedCircular;
        this.mOnItemClickListener = onItemClickListener;
        LayoutInflater inflater = (LayoutInflater) baseView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popView = inflater.inflate(R.layout.popupwindow, null, false);
        mLV_pop = (ListView) popView.findViewById(R.id.popupwindow_list);
        if (isNeedCircular) {
            popView.findViewById(R.id.abRadioGroup).setVisibility(View.VISIBLE);
            addCircular(popView);
        } else {
            popView.findViewById(R.id.abRadioGroup).setVisibility(View.GONE);
        }
        mLV_pop.setOnItemClickListener(this);
        mPopupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mInsideListAdapter = new InsideListAdapter(baseView.getContext());
        mLV_pop.setAdapter(mInsideListAdapter);
    }

    /**
     * 小圆相关
     *
     * @param popView
     */
    private void addCircular(View popView) {
        ABRadioGroup radioGroup = (ABRadioGroup) popView.findViewById(R.id.abRadioGroup);
        radioGroup.setOnCheckedChangeListener(new ABRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ABRadioGroup group, int checkedId) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(parent, view, position, id);
        }
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
        mPopupWindow.showAsDropDown(mBaseView, mBaseView.getWidth() + 5, -mBaseView.getHeight());
    }

    /**
     * MyPopupWindow数据实体类
     */
    public static class MyPopupWindowData implements Serializable {
        public String mPopItemName = "";
        public int mPopItemCode = 0;

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
    }

    private int index = 0;

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
            TextView tV_pop_item;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHoler viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHoler();
                convertView = LayoutInflater.from(context).inflate(R.layout.abpopup_list_item_lay, null, true);
                viewHolder.tV_pop_item = (TextView) convertView.findViewById(R.id.pop_item_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHoler) convertView.getTag();
            }
            if (isNeedCircular) {
                convertView.setBackgroundResource(position==index?R.drawable.circular_pen_h:R.drawable.circular_pen_n);
            } else {
                convertView.setBackgroundResource(R.drawable.hall_test_eraser);
            }

            if (position == 1)
                convertView.setSelected(true);
            viewHolder.tV_pop_item.setText(getItem(position).mPopItemName);
            return convertView;
        }
    }


//    /**
//     * 枚举像普通的类一样可以添加属性和方法，可以为它添加静态和非静态的属性或方法
//     */
//    public enum SeasonEnum {
//        pen, eraser;
//
//        public static SeasonEnum getSeason(SeasonEnum seasonEnum) {
//            if (eraser.equals(seasonEnum))
//                return eraser;
//            else
//                return pen;
//        }
//    }
}
