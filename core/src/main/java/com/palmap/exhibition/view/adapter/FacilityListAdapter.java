package com.palmap.exhibition.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.FacilityModel;
import com.palmap.library.utils.DeviceUtils;

import java.util.List;

/**
 * Created by 王天明 on 2016/5/5.
 * 显示公共设施的适配器
 */
public class FacilityListAdapter<T extends FacilityModel> extends RecyclerView.Adapter<FacilityListAdapter.ViewHolder> {

    private List<T> data;
    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    private RecyclerView recyclerView;

    private int selectPosition = -1;

    private int maxItemWidth = 30;
    private int maxItemNumber = 5;

    public FacilityListAdapter(List<T> data, RecyclerView recyclerView) {
        this.data = data;
        this.recyclerView = recyclerView;
        inflater = LayoutInflater.from(recyclerView.getContext());
        //根据数据来控制RecyclerView的宽度
        int maxWidth = DeviceUtils.dip2px(recyclerView.getContext(), maxItemWidth * maxItemNumber);

        int currentSizeWidth = DeviceUtils.dip2px(recyclerView.getContext(), data.size() * maxItemWidth);
        currentSizeWidth = currentSizeWidth > maxWidth ? maxWidth : currentSizeWidth;
        ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
        lp.width = currentSizeWidth;
        recyclerView.setLayoutParams(lp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_list_facility, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.img.setImageResource(getItem(position).getResId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(recyclerView, v, position, getItem(position));
                }
            }
        });
        holder.itemView.setSelected(selectPosition == position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public T getItem(int position) {
        return data.get(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;

        ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView recyclerView, View itemView, int position, Object data);
    }
}
