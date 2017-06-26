package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palmap.exhibition.R;

import java.util.List;

/**
 * Created by 天明 on 2016/6/29.
 */
public class HistoryListAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private LayoutInflater inflater;

    public HistoryListAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public String getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_history_list, parent, false);
            viewHolder = new ViewHolder(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvKeyWord.setText(getItem(position));
        return convertView;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tvKeyWord;

        ViewHolder(View view) {
            tvKeyWord = (TextView) view.findViewById(R.id.tvHistoryRecord);
            view.setTag(this);
        }
    }
}
