package com.palmap.exhibition.widget.overlayer;

import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.library.utils.LogUtil;
import com.palmaplus.nagrand.core.Types;
import com.palmaplus.nagrand.view.MapView;
import com.palmaplus.nagrand.view.overlay.OverlayCell;

/**
 * Created by 王天明 on 2017/2/15.
 * 梯子mark
 * 显示导航中的 楼梯 电梯 扶梯
 */
public class LadderMark extends LinearLayout implements OverlayCell {

    private double[] position;

    private MapView mapView;

    private ImageView img_mark;

    private TextView tv_msg;

    private long floorId;

    public LadderMark(MapView context,long floorId,long categoryId, String msg) {
        super(context.getContext());
        this.floorId = floorId;
        LayoutInflater.from(context.getContext()).inflate(R.layout.mark_ladder,this);

        img_mark = (ImageView) findViewById(R.id.img_mark);
        tv_msg = (TextView) findViewById(R.id.tv_msg);

        this.mapView = context;
//        setImageResource(R.mipmap.ico_marker);
        LogUtil.e("categoryId:" + categoryId);
        switch ((int) categoryId) {
            case 24091000: //电梯
            case 24092000: //无障碍电梯
                setImageResource(R.mipmap.ico_ladder_marker_elevator);
                tv_msg.setText("乘电梯" + msg);
                break;
            case 24093000://"扶梯"
            case 24094000://"无障碍扶梯"
            case 24095000://"上行扶梯"
            case 24096000://"下行扶梯"
                setImageResource(R.mipmap.ico_ladder_marker_escalator);
                tv_msg.setText("乘扶梯" + msg);
                break;
            case 24097000://"楼梯"
            case 24098000://"无障碍楼梯"
                setImageResource(R.mipmap.ico_ladder_marker_stairs);
                tv_msg.setText("乘楼梯" + msg);
                break;
            default:
                setImageResource(R.mipmap.ico_ladder_marker_stairs);
                tv_msg.setText("乘楼梯" + msg);
                break;
        }
    }

    @Override
    public void init(double[] doubles) {
        position = doubles;
    }

    /**
     * 屏幕坐标
     *
     * @param x
     * @param y
     */
    public void init(double x, double y) {
        Types.Point point = mapView.converToWorldCoordinate(x, y);
        init(new double[]{point.x, point.y});
    }

    @Override
    public double[] getGeoCoordinate() {
        return position;
    }

    @Override
    public void position(double[] doubles) {
        setX((float) doubles[0] - getWidth() / 2);
        setY((float) doubles[1] - getHeight());
    }

    @Override
    public long getFloorId() {
        return floorId;
    }

    public void setImageResource(int imageResource) {
        if (img_mark != null) {
            img_mark.setImageResource(imageResource);
        }
    }
}
