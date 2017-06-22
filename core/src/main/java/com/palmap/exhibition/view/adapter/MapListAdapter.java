package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by aoc on 2016/8/18.
 */
public class MapListAdapter extends BaseAdapter {

    private Context context;
    private HashMap<String, Long> mapVaule = new HashMap<>();

    public MapListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mapVaule.size();
    }

    @Override
    public Map.Entry<String, Long> getItem(int position) {
        Set<Map.Entry<String, Long>> entrySet = mapVaule.entrySet();
        int i = 0;
        Iterator<Map.Entry<String, Long>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            if (i == position) {
                return iterator.next();
            }
            i++;
            iterator.next();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        Map.Entry<String, Long> item = getItem(position);
        if (item != null) {
            return item.getValue();
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        TextView textView = (TextView) convertView;

        textView.setText(getItem(position).getKey() + "");
        return textView;
    }

    public void add(String name, Long id) {
        mapVaule.put(name, id);
        notifyDataSetChanged();
    }
}
