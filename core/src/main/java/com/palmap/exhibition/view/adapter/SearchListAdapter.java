package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.config.MapParam;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.SearchResultModel;
import com.palmap.exhibition.view.PoiSearchView;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.data.DataUtil;
import com.palmaplus.nagrand.data.LocationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 天明 on 2016/6/29.
 */
public class SearchListAdapter extends BaseAdapter {

    private Context context;

    private List<SearchResultModel> locationModels = new ArrayList<>();

    private LayoutInflater inflater;

    private PoiSearchView poiSearchView;

    private String keyWord;

    public SearchListAdapter(Context context, String keyWord) {
        this.context = context;
        this.keyWord = keyWord;
        inflater = LayoutInflater.from(context);

        if (context instanceof PoiSearchView) {
            poiSearchView = (PoiSearchView) context;
        }
    }

    @Override
    public int getCount() {
        return locationModels == null ? 0 : locationModels.size();
    }

    @Override
    public SearchResultModel getItem(int position) {
        return locationModels == null ? null : locationModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (getItem(position) == null) return position;
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_atyinfo_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SearchResultModel searchResultModel = getItem(position);

        if (null == searchResultModel.getNameSpan()) {
            viewHolder.tv_itemName.setText(searchResultModel.getName());
        } else {
            viewHolder.tv_itemName.setText(searchResultModel.getNameSpan());
        }

        if (TextUtils.isEmpty(searchResultModel.getVenueName())) {
            if (poiSearchView != null) {
                String address = searchResultModel.getAddress();
                if (TextUtils.isEmpty(address) || "null".equals(address.toLowerCase())) {
                    address = "";
                    viewHolder.tv_itemTime.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_itemTime.setVisibility(View.VISIBLE);
                    viewHolder.tv_itemTime.setText(address);
                }
                viewHolder.tv_itemAddress.setText(poiSearchView.obtainVenueName(searchResultModel.getFloorId()) + " " + address);
            }
        } else {
            viewHolder.tv_itemAddress.setText(searchResultModel.getVenueName());
        }
        return convertView;
    }

    public void clear() {
        locationModels.clear();
        notifyDataSetChanged();
    }

    public synchronized void addPoiData(List<LocationModel> data) {
        for (int i = 0; i < data.size(); i++) {
            LocationModel locationModel = data.get(i);
            Spannable spannable = null;
            String name = MapParam.getDisplay(locationModel);
            //if (!TextUtils.isEmpty(keyWord)) {
            String[] keys = DataUtil.highLight(locationModel);
            int[] offset = DataUtil.getOffset(name, keys);
            if (offset != null) {
                spannable = Spannable.Factory.getInstance().newSpannable(name);
                for (int j = 0; j < offset.length; j += 2) {
                    spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.searchKeyWordColor)),
                            offset[j], offset[j + 1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            //}
            String floorName = poiSearchView.obtainVenueName(LocationModel.parent.get(data.get(i)));
            LogUtil.e("floorName:" + floorName);
            if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(floorName)) {
                locationModels.add(new SearchResultModel(data.get(i), spannable));
            }
        }
        sortData();
        notifyDataSetChanged();
    }

    public synchronized void addMeetingData(List<Api_ActivityInfo.ObjBean> data) {
        for (int i = 0; i < data.size(); i++) {
            locationModels.add(new SearchResultModel(data.get(i)));
        }
        notifyDataSetChanged();
    }

    private void sortData() {
        Collections.sort(locationModels);
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