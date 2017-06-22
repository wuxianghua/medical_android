package com.palmap.exhibition.model;

/**
 * Created by 王天明 on 2016/8/31.
 */
public class Api_SimpleInfo {

    /**
     * returnCode : 0
     * errorMsg : null
     * obj : {"description":"索尼（日语：ソニー株式会社，英语：Sony Corporation），是日本的一家全球知名的大型综合性跨国企业集团。索尼是世界视听、电子游戏、通讯产品和信息技术等领域的先导者，是世界最早便携式数码产品的开创者，是世界最大的电子产品制造商之一、世界电子游戏业三大巨头之一、美国好莱坞六大电影公司之一。","logo":"http://obd58rxfm.bkt.clouddn.com/1107535_logo.png?rnd=1472466081188"}
     */

    private int returnCode;
    private Object errorMsg;
    /**
     * description : 索尼（日语：ソニー株式会社，英语：Sony Corporation），是日本的一家全球知名的大型综合性跨国企业集团。索尼是世界视听、电子游戏、通讯产品和信息技术等领域的先导者，是世界最早便携式数码产品的开创者，是世界最大的电子产品制造商之一、世界电子游戏业三大巨头之一、美国好莱坞六大电影公司之一。
     * logo : http://obd58rxfm.bkt.clouddn.com/1107535_logo.png?rnd=1472466081188
     */

    private ObjBean obj;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public Object getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Object errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public boolean isOK() {
        return returnCode == 0;
    }

    public static class ObjBean {
        private String description;
        private String logo;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
