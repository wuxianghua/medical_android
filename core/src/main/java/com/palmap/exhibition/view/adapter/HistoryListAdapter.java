package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.palmap.exhibition.R;

import java.util.List;

/**
 * Created by 天明 on 2016/6/29.
 */
public class HistoryListAdapter extends BaseAdapter {

    private List<String> mData = null;
    private LayoutInflater inflater = null;
    private OnDeleteClickListener mOnDeleteClickListener = null;

    public HistoryListAdapter(Context context, List<String> data) {
        mData = data;
        inflater = LayoutInflater.from(context);
    }

    public synchronized void setHistoryData(List<String> data){
        mData = data;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener){
        mOnDeleteClickListener = listener;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_history_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvHistoryRecord.setText(getItem(position));
        viewHolder.imgVDelHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnDeleteClickListener != null){
                    mOnDeleteClickListener.onDeleteClick(mData.get(position));
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tvHistoryRecord;
        ImageView imgVDelHistory;

        ViewHolder(View view) {
            tvHistoryRecord = (TextView) view.findViewById(R.id.tvHistoryRecord);
            imgVDelHistory = (ImageView) view.findViewById(R.id.imgVDelHistory);
        }
    }

    public interface OnDeleteClickListener{
        void onDeleteClick(String keyword);
    }

}
