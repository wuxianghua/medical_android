package com.palmap.exhibition.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.config.Config;
import com.palmap.exhibition.model.PoiModel;
import com.palmap.exhibition.view.impl.PalmapViewState;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.utils.LogUtil;
import com.palmap.library.utils.ResourceManager;
import com.palmap.library.utils.ViewUtils;
import com.palmaplus.nagrand.data.CategoryModel;
import com.palmaplus.nagrand.data.DataSource;


/**
 * Created by 王天明 on 2016/6/22.
 */
public class YanTaiPoiMenuLayout extends LinearLayout {

    LinearLayout layoutNormal;

    private int height = 0;
    private Listener listener;
    private PoiModel poiModel;

    private ClickHandler clickHandler = new ClickHandler();

    private PalmapViewState state = PalmapViewState.Select;

    private View btn_end;
    private View btn_start;
    private View layout_start;
    private View layout_tip;
    private ViewGroup layout_routeInfo;
    private View img_close;
    private TextView btn_navi;

    private TextView tv_start, tv_end, tv_distance;

    private TextView poi_name;
    private TextView poi_address;

    private ImageView poi_img;

    private DataSource dataSource;

    private int largerHeight, normalHeight;

    private boolean isHaveLocationPoint = false;

    public void setHaveLocationPoint(boolean haveLocationPoint) {
        isHaveLocationPoint = haveLocationPoint;
    }

    public void exitNavigate() {
        btn_navi.setText("开始导航");
    }

    public interface Listener {
        void onStartClick(View v);

        void onEndClick(View v);

        boolean onGoClick(View v);

        void onClearClick();

        void onStartNavigateClick(View v);
    }

    public YanTaiPoiMenuLayout(Context context) {
        this(context, null);
    }

    public YanTaiPoiMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        largerHeight = DeviceUtils.dip2px(context, 113);
        normalHeight = DeviceUtils.dip2px(context, 63);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.yantai_dialog_poimenu, this);
        bindView();
        height = ViewUtils.measureView(this).y;
    }

    public void readRemainingLength(int mRemainingLength) {
        if (tv_distance == null) {
            tv_distance = (TextView) findViewById(R.id.tv_distance);
        }
        if (tv_distance == null) return;
        tv_distance.setText("剩余"+ mRemainingLength + "米");
    }

    private void bindView() {
        layoutNormal = (LinearLayout) findViewById(R.id.layout_normal);
        layout_routeInfo = (ViewGroup) findViewById(R.id.layout_routeInfo);

        img_close = findViewById(R.id.img_close);
        btn_end = findViewById(R.id.btn_end);
        btn_start = findViewById(R.id.btn_start);
        layout_tip = findViewById(R.id.layout_tip);
        layout_start = findViewById(R.id.layout_start);
        btn_navi = (TextView) findViewById(R.id.btn_navi);

        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);
        poi_name = (TextView) findViewById(R.id.poi_name);
        poi_address = (TextView) findViewById(R.id.poi_address);

        poi_img = (ImageView) findViewById(R.id.poi_img);

        btn_end.setOnClickListener(clickHandler);
        btn_start.setOnClickListener(clickHandler);
        img_close.setOnClickListener(clickHandler);
        findViewById(R.id.img_cancel).setOnClickListener(clickHandler);
        findViewById(R.id.img_cancelStart).setOnClickListener(clickHandler);
        btn_navi.setOnClickListener(clickHandler);
    }

    public void refreshView(PoiModel poiModel, PalmapViewState state) {
        this.poiModel = poiModel;
        refreshView(state);
    }

    public void refreshView(PalmapViewState state) {
        this.state = state;

        if (this.poiModel != null) {
            if (null != poi_name) {
                poi_name.setText(this.poiModel.getDisPlay());
            }
            if (null != poi_address) {
//                if (poiModel.getZ() == Config.ID_FLOOR_F1) {
//                    poi_address.setText("F1");
//                } else if (poiModel.getZ() == Config.ID_FLOOR_F2) {
//                    poi_address.setText("F2");
//                } else {
                    poi_address.setText(this.poiModel.getAddress());
//                }
            }
            if (getContext().getString(R.string.ngr_theWay).equals(this.poiModel.getName())) {
                poi_name.setText(this.poiModel.getName());
                poi_img.setImageResource(R.mipmap.zh_logo);
            } else {
                try {
                    int resId = 0;
                    resId = ResourceManager.getRidByName(getContext(), ResourceManager.ResourceType.MIPMAP,
                            "poi_" + this.poiModel.getCategoryId());
                    if (resId != 0) {
                        poi_img.setImageResource(resId);
                    } else {
                        poi_img.setImageResource(R.mipmap.zh_logo);
                    }
                } catch (Exception e) {
                    poi_img.setImageResource(R.mipmap.zh_logo);
                }
                if (TextUtils.isEmpty(this.poiModel.getDisPlay())) {
                    if (dataSource == null) {
                        dataSource = new DataSource(Config.MAP_SERVER_URL);
                    }
                    dataSource.requestCategory(this.poiModel.getCategoryId(), new DataSource.OnRequestDataEventListener<CategoryModel>() {
                        @Override
                        public void onRequestDataEvent(DataSource.ResourceState resourceState, CategoryModel categoryModel) {
                            if (resourceState == DataSource.ResourceState.OK
                                    || resourceState == DataSource.ResourceState.CACHE) {
                                String name4 = CategoryModel.NAME4.get(categoryModel);
                                poiModel.setName(name4);
                                poi_name.setText(name4);
                            }
                        }
                    });
                }
            }
        }
        viewState(state);
    }

    private void viewState(PalmapViewState state) {
        LogUtil.e("PalmapViewState:" + state);
        layout_start.setVisibility(GONE);
        layout_tip.setVisibility(GONE);
        layout_routeInfo.setVisibility(GONE);
        btn_end.setVisibility(VISIBLE);

        if (state == PalmapViewState.END_SET) {
            btn_end.setVisibility(INVISIBLE);
            layout_start.setVisibility(VISIBLE);
        }
        if (state == PalmapViewState.RoutePlanning) {
            layout_routeInfo.setVisibility(VISIBLE);
            if (isHaveLocationPoint) {
                showNaviView();
            }
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public PoiModel getPoiModel() {
        return poiModel;
    }

    public void showNaviView() {
        btn_navi.setText("开始导航");
        btn_navi.setVisibility(VISIBLE);
        animLarger();
    }

    public void hideNaviView() {
        btn_navi.setVisibility(GONE);
        animSmall();
    }

    public void animShow() {
        if (getVisibility() == VISIBLE) return;
        setVisibility(VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setObjectValues(this);
        valueAnimator.setDuration(500);
        int nextHeight = 0;
        nextHeight = height;
        final int thisHeight = nextHeight;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fractuion = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = (int) (thisHeight * fractuion);
                setLayoutParams(lp);
            }
        });
        valueAnimator.start();
    }

    private void animSmall() {
        if (getVisibility() != VISIBLE) {
            return;
        }
        final int currentHeight = getHeight();
        if (currentHeight <= normalHeight) {
            return;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setObjectValues(this);
        valueAnimator.setDuration(500);
        final int thisHeight = normalHeight;
        final int offset = Math.abs(currentHeight - thisHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fractuion = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = (int) (currentHeight - (offset * fractuion));
                setLayoutParams(lp);
            }
        });
        valueAnimator.start();
    }

    private void animLarger() {
        if (getVisibility() != VISIBLE) return;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setObjectValues(this);
        valueAnimator.setDuration(500);
        final int currentHeight = getHeight();
        final int thisHeight = largerHeight;
        final int offset = Math.abs(currentHeight - thisHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fractuion = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = getLayoutParams();
//                lp.height = (int) (thisHeight * fractuion);
                lp.height = (int) (currentHeight + (offset * fractuion));
                setLayoutParams(lp);
            }
        });
        valueAnimator.start();
    }

    public void animHide() {
        if (getVisibility() != VISIBLE) return;
        final int currentHeight = getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
        valueAnimator.setObjectValues(this);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fractuion = animation.getAnimatedFraction();
                ViewGroup.LayoutParams lp = getLayoutParams();
                lp.height = (int) (currentHeight * (1 - fractuion));
                setLayoutParams(lp);
                if (fractuion >= 1) {
                    setVisibility(GONE);
                }
            }
        });
        valueAnimator.start();
    }

    public boolean layoutTipIsShow() {
        return layout_tip.getVisibility() == VISIBLE;
    }

    private final class ClickHandler implements OnClickListener {
        @Override
        public void onClick(View v) {
            if (listener == null) {
                return;
            }
            int id = v.getId();
            if (id == R.id.btn_start) {
                listener.onStartClick(v);
            } else if (id == R.id.btn_end) {
                if (!listener.onGoClick(v)) {
                    layout_tip.setVisibility(VISIBLE);
                }
            } else if (id == R.id.img_close || id == R.id.img_cancel || id == R.id.img_cancelStart) {//关闭
                listener.onClearClick();
                animHide();
            } else if (id == R.id.btn_navi) {
                btn_navi.setText("退出导航");
                listener.onStartNavigateClick(v);
            }
        }
    }

    public void showStartMsg(String lable, String msg) {
        if (TextUtils.isEmpty(lable)) lable = "辅助";
        tv_start.setText(lable);
    }

    public void showEndMsg(String lable, String msg) {
        if (TextUtils.isEmpty(lable)) lable = "辅助";
        if (this.poiModel != null) lable = this.poiModel.getDisPlay();
        tv_end.setText(lable);
    }

    public void showDetailsMsg(String msg) {
        if (tv_distance == null) {
            tv_distance = (TextView) findViewById(R.id.tv_distance);
        }
        if (tv_distance == null) return;
        tv_distance.setText(msg);
    }

    public void showRouteInfo() {
        layout_routeInfo.setVisibility(VISIBLE);
    }
}
