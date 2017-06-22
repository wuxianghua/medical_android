package com.palmap.exhibition.other;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.palmap.exhibition.R;


/**
 * Created by 王天明 on 2016/11/3.
 * 地图配色管理器
 */
public class MapStyleManager {

    private Context context;
    private SharedPreferences preference;

    public MapStyleManager(Context context) {
        this.context = context;
        preference = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String readCurrentStyle() {
        return preference.getString(context.getString(R.string.ngr_mapStyleKey), "999");
    }

    public boolean upDataStyle(String style) {
        return preference.edit().putString(context.getString(R.string.ngr_mapStyleKey), style)
                .commit();
    }

}