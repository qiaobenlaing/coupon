package com.huift.hfq.base.pojo;

import java.util.List;

/**
 * Created by Administrator on 2018/9/19/019.
 */

public class BussinessDistrictListBean {

    private List<ZoneBean> zone;
    private int code;

    public List<ZoneBean> getZone() {
        return zone;
    }

    public void setZone(List<ZoneBean> zone) {
        this.zone = zone;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class ZoneBean {

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
