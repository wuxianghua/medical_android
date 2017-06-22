package com.palmap.exhibition.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.view.PoiSearchView;

/**
 * Created by 王天明 on 2016/9/19.
 */
public class SearchActivityInfoListAdapter extends BaseAdapter {

    private Api_ActivityInfo activityInfo;
    private LayoutInflater inflater;
    private PoiSearchView poiSearchView;

    public SearchActivityInfoListAdapter(PoiSearchView poiSearchView, Api_ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
        this.poiSearchView = poiSearchView;
        this.inflater = LayoutInflater.from(poiSearchView.getContext());
    }

    @Override
    public int getCount() {
        if (null == activityInfo) return 0;
        return activityInfo.getObj() == null ? 0 : activityInfo.getObj().size();
    }

    @Override
    public Api_ActivityInfo.ObjBean getItem(int position) {
        return activityInfo.getObj().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_atyinfo_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Api_ActivityInfo.ObjBean data = getItem(position);

        holder.tv_itemName.setText(data.getActivityName());
        holder.tv_itemTime.setText(data.getStartTime() + "~" + data.getEndTime());
        holder.tv_itemAddress.setText(
                poiSearchView.obtainFloorNameById(data.getFloorId()) + " "+
                        data.getRoomNumber());

        return convertView;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView tv_itemName;
        public TextView tv_itemTime;
        public TextView tv_itemAddress;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_itemName = (TextView) rootView.findViewById(R.id.tv_itemName);
            this.tv_itemTime = (TextView) rootView.findViewById(R.id.tv_itemTime);
            this.tv_itemAddress = (TextView) rootView.findViewById(R.id.tv_itemAddress);
        }
    }
}
