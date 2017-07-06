package com.palmap.exhibition.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.widget.imgslider.SliderTypes.BaseSliderView;
import com.palmap.library.utils.DeviceUtils;

import java.util.List;

import static com.palmap.library.utils.ResourceManager.getRidByName;

/**
 * Created by jinhuang.yang on 2017/6/30.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class QuickSearchPanelView extends BaseSliderView {

    private Context mContext = null;
    private List<QuickSearchKeyWordModel.ChildBean> mData = null;
    private ItemViewClickListener mItemViewClickListener = null;
    private int mVerticalSpacing;

    public QuickSearchPanelView(Context context, List<QuickSearchKeyWordModel.ChildBean> data) {
        super(context);
        mContext = context;
        mData = data;
        mVerticalSpacing = DeviceUtils.dip2px(context, 5);
    }

    @Override
    public View getView() {
        GridView view = new GridView(mContext);
        view.setNumColumns(4);
        view.setVerticalSpacing(mVerticalSpacing);
        view.setAdapter(new CustomAdapter());
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mItemViewClickListener.onItemClick(mData.get(i));
            }
        });
        return view;
    }

    public void setItemViewClickListener(ItemViewClickListener listener){
        mItemViewClickListener = listener;
    }

    public interface ItemViewClickListener{
        void onItemClick(QuickSearchKeyWordModel.ChildBean childBean);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData == null ? 0 : mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData == null ? null : mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(
                        R.layout.item_quick_search_panel, viewGroup, false);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            QuickSearchKeyWordModel.ChildBean childBean = mData.get(i);
            if(childBean != null){
                int resID = getRidByName(mContext, childBean.getIconName());
                if (resID == 0) {
                    resID = R.mipmap.ic_search_hot_01;
                }
                holder.imgVIcon.setBackgroundResource(resID);
                holder.tvKeyWordAlias.setText(childBean.getDisplayName());
            }
            return view;
        }

    }

    private static class ViewHolder{
        ImageView imgVIcon;
        TextView tvKeyWordAlias;

        ViewHolder(View rootView){
            imgVIcon = (ImageView)rootView.findViewById(R.id.imgVIcon);
            tvKeyWordAlias = (TextView)rootView.findViewById(R.id.tvKeyWordAlias);
        }
    }

}
