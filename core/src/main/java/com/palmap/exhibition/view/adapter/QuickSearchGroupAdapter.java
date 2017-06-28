package com.palmap.exhibition.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.palmap.exhibition.R;
import com.palmap.exhibition.model.QuickSearchKeyWordModel;
import com.palmap.exhibition.widget.flowtag.FlowTagLayout;
import com.palmap.exhibition.widget.flowtag.OnTagClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.palmap.library.utils.ResourceManager.getRidByName;

/**
 * Created by jinhuang.yang on 2017/6/26.
 * E-mail:jinhuang.yang@palmaplus.com
 */

public class QuickSearchGroupAdapter extends BaseExpandableListAdapter {

    private Context mContext = null;
    private List<QuickSearchKeyWordModel> mKeyWordList = null;
    private Map<String, TagAdapter> mTagAdaptMap = new HashMap<>();
    private LayoutInflater mInflater = null;
    private OnItemClickListener mOnItemClickListener = null;

    public QuickSearchGroupAdapter(Context context, List<QuickSearchKeyWordModel> dataList) {
        mContext = context;
        mKeyWordList = dataList == null ? new ArrayList<QuickSearchKeyWordModel>() : dataList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addAll(List<QuickSearchKeyWordModel> dataList) {
        mKeyWordList.addAll(dataList);
    }

    public List<QuickSearchKeyWordModel> getKeyWordList() {
        return mKeyWordList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getGroupCount() {
        return mKeyWordList == null ? 0 : mKeyWordList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return mKeyWordList == null ? null : mKeyWordList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mKeyWordList == null ? null : mKeyWordList.get(i).getChild().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_search_group_list, viewGroup, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        QuickSearchKeyWordModel groupModel = mKeyWordList.get(i);
        if (groupModel != null) {
            int resID = getRidByName(mContext, groupModel.getIconName());
            if (resID == 0) {
                resID = R.mipmap.ic_list_menzhen;
            }
            holder.mImgVGroupIcon.setBackgroundResource(resID);
            holder.mTvGroupTitle.setText(groupModel.getTitle());
            holder.imgVExpand.setBackgroundResource(
                    b ? R.mipmap.ic_list_arrow_up : R.mipmap.ic_list_arrow_down);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        FlowTagLayout flowTagLayout;
        if (view == null) {
            flowTagLayout = new FlowTagLayout(mContext);
        } else {
            flowTagLayout = (FlowTagLayout) view;
        }
        final QuickSearchKeyWordModel groupModel = mKeyWordList.get(i);
        if (groupModel != null) {
            TagAdapter tagAdapter = mTagAdaptMap.get(groupModel.getTitle());
            if (tagAdapter == null) {
                tagAdapter = new TagAdapter(groupModel.getChild());
                mTagAdaptMap.put(groupModel.getTitle(), tagAdapter);
            }
            flowTagLayout.setAdapter(tagAdapter);
            flowTagLayout.setOnTagClickListener(new OnTagClickListener() {
                @Override
                public void onItemClick(FlowTagLayout parent, View view, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(groupModel.getChild().get(position));
                    }
                }
            });
            tagAdapter.notifyDataSetChanged();
        }
        return flowTagLayout;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private class TagAdapter extends BaseAdapter {

        private final List<QuickSearchKeyWordModel.ChildBean> mDataList;

        TagAdapter(List<QuickSearchKeyWordModel.ChildBean> dataList) {
            mDataList = dataList;
        }

        @Override
        public int getCount() {
            return mDataList == null ? 0 : mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_flow_label, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.tvTag);
            QuickSearchKeyWordModel.ChildBean childBean = mDataList.get(position);
            if (childBean != null) {
                textView.setText(childBean.getDisplayName());
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        View mRootView;
        ImageView mImgVGroupIcon;
        TextView mTvGroupTitle;
        ImageView imgVExpand;

        ViewHolder(View rootView) {
            mRootView = rootView;
            mImgVGroupIcon = (ImageView) mRootView.findViewById(R.id.imgVGroupIcon);
            mTvGroupTitle = (TextView) mRootView.findViewById(R.id.tvGroupTitle);
            imgVExpand = (ImageView) mRootView.findViewById(R.id.imgVGroupExpand);
        }
    }

    public interface OnItemClickListener {
        void onClick(QuickSearchKeyWordModel.ChildBean childBean);
    }

}
