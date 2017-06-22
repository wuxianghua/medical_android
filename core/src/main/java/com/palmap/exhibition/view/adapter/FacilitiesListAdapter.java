package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.FacilityModel;

import java.util.List;

/**
 * Created by 王天明 on 2016/7/18.
 */
public class FacilitiesListAdapter extends BaseAdapter {

    private List<FacilityModel> data;
    private Context context;
    private int selectPosition = -1;

    public FacilitiesListAdapter(Context context, List<FacilityModel> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public FacilityModel getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_facilities, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(getItem(position).getResId());
        viewHolder.imageView.setSelected(position == selectPosition);

        return convertView;
    }

    static class ViewHolder{

        public ImageView imageView;

        public ViewHolder(View convertView) {
            this.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(this);
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }
}
