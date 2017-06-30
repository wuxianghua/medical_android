package com.palmap.exhibition.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.palmap.exhibition.R;
import com.palmap.exhibition.iflytek.IFlytekController;
import com.palmap.exhibition.iflytek.SimpleSynthesizerListener;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.view.impl.PalmapViewState;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.ViewAnimUtils;
import com.palmap.library.utils.ViewUtils;

/**
 * Created by 王天明 on 2017/6/28.
 */

public class PoiMenuLayout extends LinearLayout implements IPoiMenu {

    private ViewGroup layout_poi_info;
    private ViewGroup layout_select_start;
    private ViewGroup layout_navi_ready;
    private ViewGroup layout_navi_info;
    private ViewGroup layout_navi_ok;

    private View btn_goHere;
    private View btn_mockNavi;
    private View btn_startNavi;
    private View image_closeNavi;

    private View image_sound;

    private View btn_navi_ok;

    private TextView tv_navi_length;
    private TextView tv_route_info;

    private int height_poi_info = 0;
    private int height_select_start = 0;
    private int height_navi_ready = 0;
    private int height_navi_info = 0;
    private int height_navi_ok = 0;

    private PoiModel poiModel;

    private SpeechSynthesizer naviSpeech;
    /**
     * 是否可以播放语音
     */
    private boolean canSound = true;

    public interface ViewHandler {
        ViewHandler DEFAULE = new ViewHandler() {
            @Override
            public void onGoHereClick() {
            }

            @Override
            public void onMockNaviClick() {
            }

            @Override
            public void onStartNaviClick() {
            }

            @Override
            public void onExitNaviClick() {

            }
        };

        void onGoHereClick();

        void onMockNaviClick();

        void onStartNaviClick();

        void onExitNaviClick();
    }

    private ViewHandler viewHandler = ViewHandler.DEFAULE;


    public PoiMenuLayout(Context context) {
        this(context, null);
    }

    public PoiMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_poi_menu, this);
        bindView();
    }

    private void bindView() {
        layout_poi_info = (ViewGroup) findViewById(R.id.layout_poi_info);
        layout_select_start = (ViewGroup) findViewById(R.id.layout_select_start);
        layout_navi_ready = (ViewGroup) findViewById(R.id.layout_navi_ready);
        layout_navi_info = (ViewGroup) findViewById(R.id.layout_navi_info);
        layout_navi_ok = (ViewGroup) findViewById(R.id.layout_navi_ok);
        btn_goHere = findViewById(R.id.btn_goHere);
        btn_startNavi = findViewById(R.id.btn_startNavi);
        btn_mockNavi = findViewById(R.id.btn_mockNavi);
        image_sound = findViewById(R.id.image_sound);
        btn_navi_ok = findViewById(R.id.btn_navi_ok);
        image_closeNavi = findViewById(R.id.image_closeNavi);
        tv_navi_length = (TextView) findViewById(R.id.tv_navi_length);

        height_poi_info = ViewUtils.measureView(layout_poi_info).y;
        height_select_start = ViewUtils.measureView(layout_select_start).y;
        height_navi_ready = height_select_start;
        height_navi_info = height_poi_info;
        height_navi_ok = ViewUtils.measureView(layout_navi_ok).y;

        btn_goHere.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onGoHereClick();
            }
        });

        btn_mockNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onMockNaviClick();
            }
        });

        btn_startNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onStartNaviClick();
            }
        });

        image_closeNavi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onExitNaviClick();
            }
        });

        image_sound.setSelected(canSound);
        image_sound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                canSound = !canSound;
                image_sound.setSelected(canSound);
                if (!canSound && naviSpeech!= null) {
                    naviSpeech.stopSpeaking();
                    naviSpeech.destroy();
                    naviSpeech = null;
                }
            }
        });

        btn_navi_ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHandler.onExitNaviClick();
            }
        });
    }

    public void setViewHandler(ViewHandler viewHandler) {
        this.viewHandler = viewHandler == null ? ViewHandler.DEFAULE : viewHandler;
    }

    @Override
    public void refreshView(PalmapViewState state) {
        LogUtil.e("state :" + state);
        layout_poi_info.setVisibility(GONE);
        layout_select_start.setVisibility(GONE);
        layout_navi_ready.setVisibility(GONE);
        layout_navi_info.setVisibility(GONE);
        layout_navi_ok.setVisibility(GONE);

        int nextHeight = getHeight();

        switch (state) {
            case Normal:
                layout_poi_info.setVisibility(VISIBLE);
                nextHeight = height_poi_info;
                break;
            case END_SET:
                layout_select_start.setVisibility(VISIBLE);
                nextHeight = height_select_start;
                break;

            case RoutePlanning:
                layout_navi_ready.setVisibility(VISIBLE);
                nextHeight = height_navi_ready;
                break;

            case Navigating:
                layout_navi_info.setVisibility(VISIBLE);
                nextHeight = height_navi_info;
                break;

            case NaviComplete:
                layout_navi_ok.setVisibility(VISIBLE);
                nextHeight = height_navi_ok;
                break;

            default:

                break;
        }
        animShow(nextHeight);
    }

    public void readRemainingLength(String mDynamicNaviExplain, float mRemainingLength) {
        if (layout_navi_info.getVisibility() == VISIBLE) {
            tv_navi_length.setText(String.format("剩余约%d米", (int) mRemainingLength));
            if (!canSound && !TextUtils.isEmpty(mDynamicNaviExplain)) {
                return;
            }
            // TODO: 2017/6/28 语言播报
            if (naviSpeech == null) {
                naviSpeech = IFlytekController.getInstance().obtainSpeechSynthesizer(getContext(), null);
            }
            naviSpeech.startSpeaking(mDynamicNaviExplain, new SimpleSynthesizerListener() {
                @Override
                public void onSpeakBegin() {
                }

                @Override
                public void onCompleted(SpeechError speechError) {
                }
            });
        }
    }

    public void showRouteInfoDetails(String msg) {
        if (tv_route_info == null) {
            tv_route_info = (TextView) findViewById(R.id.tv_route_info);
        }
        if (tv_route_info != null) {
            tv_route_info.setText(msg);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        IFlytekController.getInstance().destroyAllSpeechSynthesizer();
        naviSpeech = null;
    }

    private void animShow(int height) {
        ViewAnimUtils.animHeight(
                this,
                this.getHeight(),
                height,
                300, null);
    }

    public void animHide() {
        animShow(0);
    }

    public PoiModel getPoiModel() {
        return poiModel;
    }

    @Override
    public void refreshView(PoiModel poiModel, PalmapViewState state) {
        this.poiModel = poiModel;
        refreshView(state);
    }
}
