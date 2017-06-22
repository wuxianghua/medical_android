package com.palmap.library.base.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by 王天明 on 2016/9/9.
 */
public abstract class BasePreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(setPreferencesId());
    }

    protected abstract int setPreferencesId();
}