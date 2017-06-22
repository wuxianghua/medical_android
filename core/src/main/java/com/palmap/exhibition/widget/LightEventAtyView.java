package com.palmap.exhibition.widget;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.Api_ActivityInfo;
import com.palmap.exhibition.view.adapter.TaskRecyclerViewAdapter;
import com.palmap.library.utils.DeviceUtils;
import com.palmap.library.widget.adapters.SlideInRightAnimationAdapter;

/**
 * Created by 王天明 on 2016/10/19.
 * 点亮活动 视图 view
 */
public class LightEventAtyView extends LinearLayout {

    private ImageView img_task;
    private TaskMenuDialog taskMenuDialog;

    public LightEventAtyView(Context context) {
        super(context);
    }

    public LightEventAtyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_light_event_atyview, this);
        bindView();
    }

    private void bindView() {
        img_task = (ImageView) findViewById(R.id.img_task);
        img_task.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openTaskMenu();
            }
        });

        taskMenuDialog = new TaskMenuDialog(getContext());
    }

    public void loadViewData(Api_ActivityInfo api_activityInfo) {
        img_task.setVisibility(VISIBLE);
        taskMenuDialog.setTaskListData(api_activityInfo);
    }

    private void openTaskMenu() {
        taskMenuDialog.show();
    }

    public void hideTaskMenu() {
        if (taskMenuDialog != null) {
            taskMenuDialog.dismiss();
        }
    }

    public void setFunctionClickCallBack(TaskMenuDialog.FunctionClickCallBack functionClickCallBack) {
        taskMenuDialog.setFunctionClickCallBack(functionClickCallBack);
    }

    public static class TaskMenuDialog extends Dialog {

        private RecyclerView recyclerView;
        private Api_ActivityInfo taskListData;
        private TaskRecyclerViewAdapter taskRecyclerViewAdapter;
        private TextView tv_hideActivityMarks;

        private ViewGroup layout_details;
        private TextView tv_taskName, tv_taskDetails;

        public interface FunctionClickCallBack {
            void onStartClick(Api_ActivityInfo.ObjBean objBean);

            void onShareClick(Api_ActivityInfo.ObjBean objBean);

            void onHideActivityMarksClick(boolean canShow);
        }

        private FunctionClickCallBack functionClickCallBack;

        TaskMenuDialog(Context context) {
            super(context, R.style.dialog_task);
            setContentView(R.layout.dialog_taskmenu);

            findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            tv_hideActivityMarks = (TextView) findViewById(R.id.tv_hideActivityMarks);
            tv_hideActivityMarks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv_hideActivityMarks.setSelected(!tv_hideActivityMarks.isSelected());
                    tv_hideActivityMarks.setText(tv_hideActivityMarks.isSelected() ? "显示活动引导" : "隐藏活动引导");
                    if (functionClickCallBack != null) {
                        functionClickCallBack.onHideActivityMarksClick(!tv_hideActivityMarks.isSelected());
                    }
                }
            });

            findViewById(R.id.img_exitDetails).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideDetailsLayout();
                }
            });

            layout_details = (ViewGroup) findViewById(R.id.layout_details);

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

            recyclerView.setLayoutManager(layoutManager);

            //设置Item增加、移除动画
            taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(getContext(), taskListData);
            SlideInRightAnimationAdapter slideInRightAnimationAdapter = new SlideInRightAnimationAdapter(taskRecyclerViewAdapter);
            recyclerView.setAdapter(slideInRightAnimationAdapter);

            taskRecyclerViewAdapter.setOnDetailsClick(new TaskRecyclerViewAdapter.OnDetailsClick() {
                @Override
                public void onDetailsClick(Api_ActivityInfo.ObjBean objBean) {
                    showDetailsLayout(objBean);
                }
            });

            tv_taskName = (TextView) findViewById(R.id.tv_taskName);
            tv_taskDetails = (TextView) findViewById(R.id.tv_taskDetails);
        }

        void setTaskListData(Api_ActivityInfo taskListData) {
            this.taskListData = taskListData;
            taskRecyclerViewAdapter.setApi_activityInfo(taskListData);
        }

        @Override
        public void show() {
            super.show();
            taskRecyclerViewAdapter.refresh();
        }

        void setFunctionClickCallBack(FunctionClickCallBack functionClickCallBack) {
            this.functionClickCallBack = functionClickCallBack;
            taskRecyclerViewAdapter.setFunctionClickCallBack(functionClickCallBack);
        }

        void showDetailsLayout(Api_ActivityInfo.ObjBean objBean) {

            tv_taskName.setText(objBean.getActivityName());
            tv_taskDetails.setText(objBean.getActivityDesc());

            layout_details.setVisibility(View.VISIBLE);
            ObjectAnimator anim = ObjectAnimator.ofFloat(layout_details,
                    "translationX", DeviceUtils.getWidth(getContext()), 0);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(1000);
            anim.start();
        }

        void hideDetailsLayout() {
            ObjectAnimator anim = ObjectAnimator.ofFloat(layout_details,
                    "translationX", 0, DeviceUtils.getWidth(getContext()));
            anim.setDuration(500);
            anim.start();
            //layout_details.setVisibility(View.GONE);
        }

    }


}