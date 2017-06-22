package com.palmap.exhibition.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.R;
import com.palmap.exhibition.api.ActivityInfoService;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.factory.ServiceFactory;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.model.SearchResultModel;
import com.palmap.exhibition.view.adapter.SearchListAdapter;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by 王天明 on 2016/9/20.
 */
public class MeetingAtyView extends LinearLayout implements View.OnClickListener {

    public interface Listener {
        void onListItemClick(SearchResultModel searchResultModel);
    }

    private Listener listener;
    private ListView listView;
    private TextView txt_tab_meeting;
    private TextView txt_tab_aty;
    private long floorId;

    private SearchListAdapter layout_activityAdapter;
    private SearchListAdapter layout_meetingAdapter;

    public MeetingAtyView(Context context) {
        this(context, null);
    }

    public MeetingAtyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.pop_meeting_aty, this);

        listView = (ListView) findViewById(R.id.listView);
        txt_tab_meeting = (TextView) findViewById(R.id.txt_tab_meeting);
        txt_tab_aty = (TextView) findViewById(R.id.txt_tab_aty);

        txt_tab_meeting.setOnClickListener(this);
        txt_tab_aty.setOnClickListener(this);

        txt_tab_meeting.setSelected(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    SearchResultModel searchResultModel = ((SearchListAdapter) parent.getAdapter()).getItem(position);
                    listener.onListItemClick(searchResultModel);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.isSelected()) return;
        v.setSelected(true);
        if (v.getId() == R.id.txt_tab_meeting) {
            txt_tab_aty.setSelected(false);
            if (layout_meetingAdapter != null) {
                listView.setAdapter(layout_meetingAdapter);
                return;
            }
            listView.setAdapter(null);
            requestMeetingData();
        }
        if (v.getId() == R.id.txt_tab_aty) {
            txt_tab_meeting.setSelected(false);
            if (layout_activityAdapter != null) {
                listView.setAdapter(layout_activityAdapter);
                return;
            }
            listView.setAdapter(null);
            requestAtyData();
        }
    }

    private void requestAtyData() {
        requestActivityInfoApi("1", floorId, true)
                .subscribe(new Action1<Api_ActivityInfo>() {
                    @Override
                    public void call(Api_ActivityInfo api_activityInfo) {
                        layout_activityAdapter = new SearchListAdapter(getContext(), null);
                        layout_activityAdapter.addMeetingData(api_activityInfo.getObj());
                        listView.setAdapter(layout_activityAdapter);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    private void requestMeetingData() {
        requestActivityInfoApi("0", floorId, true)
                .subscribe(new Action1<Api_ActivityInfo>() {
                    @Override
                    public void call(Api_ActivityInfo api_activityInfo) {
                        layout_meetingAdapter = new SearchListAdapter(getContext(), null);
                        layout_meetingAdapter.addMeetingData(api_activityInfo.getObj());
                        listView.setAdapter(layout_meetingAdapter);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                    }
                });
    }

    private Observable<Api_ActivityInfo> requestActivityInfoApi(String type, long floorId, boolean isExt) {
        return ServiceFactory.create(ActivityInfoService.class)
                .requestActivityInfoWithFloorId(Config.getLanguagePara(),
                        floorId,
                        AndroidApplication.getInstance().getLocationMapId(),
                        type, null, isExt)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setFloorId(long floorId) {
        this.floorId = floorId;
        if (layout_meetingAdapter != null) {
            layout_meetingAdapter.clear();
            layout_meetingAdapter = null;
        }
        if (layout_activityAdapter != null) {
            layout_activityAdapter.clear();
            layout_activityAdapter = null;
        }
        listView.setAdapter(null);

        txt_tab_aty.setSelected(false);
        txt_tab_meeting.setSelected(true);

        requestMeetingData();
    }
}