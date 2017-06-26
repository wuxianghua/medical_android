package com.palmap.exhibition.model;

import java.util.List;

/**
 * Created by 王天明 on 2017/6/26.
 * 搜索界面 搜索名称对应实际关键字搜索.使用disPlay字段显示 使用keyWord search
 */
public class QuickSearchKeyWordModel {

    /**
     * 一级名称
     */
    private String title;
    /**
     * 图标文件名
     */
    private String iconName;
    /**
     * //0是不展开 // 1 就是展开
     */
    private int state;
    private List<ChildBean> child;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    public static class ChildBean {
        private String displayName;
        private String searchKeyWord;
        private String iconName;

        public String getIconName() {
            return iconName;
        }

        public void setIconName(String iconName) {
            this.iconName = iconName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getSearchKeyWord() {
            return searchKeyWord;
        }

        public void setSearchKeyWord(String searchKeyWord) {
            this.searchKeyWord = searchKeyWord;
        }
    }
}
