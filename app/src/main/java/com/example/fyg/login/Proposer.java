package com.example.fyg.login;

public class Proposer {
    public String express_state;//是否有人接单

    public String user;//接单者姓名

    public String rece_time;//接单时间

    public String rephone;//接单者电话

    public String gettime;//收货时间

    public String item_id;

    public static int length;

    public  static Proposer[] proposers=new Proposer[1000];

    public void setExpress_state(String express_state) {
        this.express_state = express_state;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setRece_time(String rece_time) {
        this.rece_time = rece_time;
    }
}
