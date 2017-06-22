package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.palmap.exhibition.AndroidApplication;
import com.palmap.exhibition.R;
import com.palmap.exhibition.dao.ActivityModel;
import com.palmap.exhibition.dao.business.ActivityInfoBusiness;
import com.palmap.exhibition.dao.business.CoordinateBusiness;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.widget.LightEventAtyView;
import com.palmap.exhibition.widget.RectProgressBar;
import com.palmap.library.utils.LogUtil;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 王天明 on 2016/10/21.
 * 任务列表 适配器
 */
public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ItemViewHolder> {

    private Api_ActivityInfo api_activityInfo;
    private Context context;
    private LightEventAtyView.TaskMenuDialog.FunctionClickCallBack functionClickCallBack;
    private ActivityInfoBusiness activityInfoBusiness;
    private CoordinateBusiness coordinateBusiness;

    private OnDetailsClick onDetailsClick;

    public void refresh() {
        sortData();
        notifyDataSetChanged();
    }

    public interface OnDetailsClick {
        void onDetailsClick(Api_ActivityInfo.ObjBean objBean);
    }

    private int[] colorArr = {
            0xffb2f9bd,
            0xffa8e1fe,
            0xffe79f14,
            0xfff6066c,
            0xffe43eda,
            0xff77bbca,
    };

    public TaskRecyclerViewAdapter(Context context, Api_ActivityInfo api_activityInfo) {
        this.context = context;
        this.api_activityInfo = api_activityInfo;
        activityInfoBusiness = AndroidApplication.getInstance().getApplicationComponent().activityInfoBusiness();
        coordinateBusiness = AndroidApplication.getInstance().getApplicationComponent().coordinateBusiness();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View content = LayoutInflater.from(context).inflate(R.layout.item_recy_task, parent, false);
        return new ItemViewHolder(content);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final Api_ActivityInfo.ObjBean objBean = api_activityInfo.getObj().get(position);
        holder.taskName.setText(objBean.getActivityName());
        holder.imgDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDetailsClick != null) {
                    onDetailsClick.onDetailsClick(objBean);
                }
            }
        });

        holder.tvState.setTextColor(Color.WHITE);

        holder.rectPro.setVisibility(View.INVISIBLE);

        int atyId = objBean.getId();
        ActivityModel activityModel = activityInfoBusiness.findOneByAtyId(atyId);
        if (activityModel != null) {
            //0 待接收 1 进行中 2 完成等待分享 3 分享完成
            LogUtil.e(objBean.getActivityName() + "=>状态:" + activityModel.getState());
            switch (activityModel.getState()) {
                case 0://进行中

                    int pro = coordinateBusiness.findListByAtyId(atyId + "").size();

                    holder.tvState.setTextColor(0xff666666);
//                    holder.tvState.setText("进行中");
                    String stateStr = pro + "/" + activityModel.getAimsCount();
                    SpannableStringBuilder spBuilder = new SpannableStringBuilder(stateStr);
                    int end = (pro + "").length();
                    spBuilder.setSpan(new ForegroundColorSpan(0xff00b09f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tvState.setText(spBuilder);
                    holder.tvState.setSelected(false);
                    holder.tvState.setEnabled(false);
                    holder.rectPro.setVisibility(View.VISIBLE);
                    holder.rectPro.setColor(
                            colorArr[position % colorArr.length]
                    );
                    holder.rectPro.setMax(activityModel.getAimsCount());
                    holder.rectPro.setProgress(activityModel.getAimsCount());
                    holder.rectPro.setProgress(pro);
                    break;
                case 2:
                    holder.tvState.setText("分享");
                    holder.tvState.setSelected(true);
                    holder.tvState.setEnabled(true);
                    holder.tvState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (functionClickCallBack != null) {
                                functionClickCallBack.onShareClick(objBean);
                            }
                        }
                    });
                    break;
                case 3:
                    holder.tvState.setTextColor(context.getResources().getColor(R.color.txt9));
                    holder.tvState.setText("完成");
                    holder.tvState.setSelected(false);
                    holder.tvState.setEnabled(false);
                    break;
            }
        } else {//没有保存过 说没有领取过任务
            holder.tvState.setSelected(false);
            holder.tvState.setEnabled(true);
            holder.tvState.setText("开启");
            holder.tvState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (functionClickCallBack != null) {
                        functionClickCallBack.onStartClick(objBean);
                    }
                }
            });
        }
    }

    public void setFunctionClickCallBack(LightEventAtyView.TaskMenuDialog.FunctionClickCallBack functionClickCallBack) {
        this.functionClickCallBack = functionClickCallBack;
    }

    @Override
    public int getItemCount() {
        return api_activityInfo == null ? 0 : api_activityInfo.getObj().size();
    }

    public void setApi_activityInfo(Api_ActivityInfo api_activityInfo) {
        this.api_activityInfo = api_activityInfo;
        //对数据进行排序
        sortData();
    }


    private void sortData(){
        Collections.sort(this.api_activityInfo.getObj(), new Comparator<Api_ActivityInfo.ObjBean>() {
            @Override
            public int compare(Api_ActivityInfo.ObjBean lhs, Api_ActivityInfo.ObjBean rhs) {
                ActivityModel lModel = activityInfoBusiness.findOneByAtyId(lhs.getId());
                ActivityModel rModel = activityInfoBusiness.findOneByAtyId(rhs.getId());
                if (lModel == null) {
                    return 1;
                }
                if (rModel == null) {
                    return -1;
                }
                return lModel.getState() - rModel.getState();
            }
        });
    }

    public void setOnDetailsClick(OnDetailsClick onDetailsClick) {
        this.onDetailsClick = onDetailsClick;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView taskName;
        ImageView imgDetails;
        TextView tvState;
        RectProgressBar rectPro;

        ItemViewHolder(View itemView) {
            super(itemView);
            taskName = (TextView) itemView.findViewById(R.id.tv_taskName);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            imgDetails = (ImageView) itemView.findViewById(R.id.img_details);
            rectPro = (RectProgressBar) itemView.findViewById(R.id.rectPro);
        }
    }
}
