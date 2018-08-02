package com.example.fyg.login;

public class Accepter {
    public String user;
    public String rece_time;
    public String srcplace;
    public String dstplace;
    public String item_id;
    public static int length;
    public static Accepter[] accepters=new Accepter[1000];

    public void setUser(String user){
        this.user=user;
    }

    public void setRece_time(String rece_time){
        this.rece_time=rece_time;
    }

    public void setSrcplace(String srcplace){
        this.user=user;
    }

    public void setDstplace(String dstplace){
        this.dstplace=dstplace;
    }

    public void setItem_id(String item_id){
        this.item_id=item_id;
    }
}
