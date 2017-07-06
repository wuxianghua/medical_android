package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.ExFloorModel;

import java.util.ArrayList;

/**
 * Created by 王天明 on 2016/5/3.
 * 用于显示楼层选择的适配器
 */
public class FloorListAdapter extends BaseAdapter {

    private ArrayList<ExFloorModel> data = new ArrayList<>();

    private Context context;

    private LayoutInflater inflater;

    private long selectFloorId = -1;

    private int selectIndex = 0;

    public FloorListAdapter(Context context, ArrayList<ExFloorModel> locationList, long selectFloorId) {
        if (locationList != null) {
            this.data.addAll(locationList);
        }
        this.context = context;
        this.selectFloorId = selectFloorId;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ExFloorModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_floor, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ExFloorModel floorModel = getItem(position);

        viewHolder.tvFloorName.setText(floorModel.getShortName());
        long curId = floorModel.getId();

        if (this.selectFloorId == curId) {
            selectIndex = position;
            viewHolder.tvFloorName.setSelected(true);
        }
        return convertView;
    }

    public String getLocationModelName(long floorId) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == floorId) {
                return data.get(i).getShortName();
            }
        }
        return "E";
    }

    public int getSelectIndex() {
//        return selectIndex;
        for (int i = 0; i < getCount(); i++) {
            long floorId = getItem(i).getId();
            if (floorId == selectFloorId) {
                return i;
            }
        }
        return 0;
    }

    public long getSelectFloorId() {
        return selectFloorId;
    }

    public void setSelectFloorId(long selectFloorId) {
        this.selectFloorId = selectFloorId;
    }

    public void replaceData(ArrayList<ExFloorModel> floorModelList, long selectFloorId) {
        this.selectFloorId = selectFloorId;
        this.data.clear();
        if (floorModelList != null) {
            this.data.addAll(floorModelList);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvFloorName;
        ViewHolder(View view) {
            tvFloorName = (TextView) view.findViewById(R.id.tv_floorName);
        }
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public boolean checkFloorId(long floorId){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == floorId) {
                return true;
            }
        }
        return false;
    }

}
