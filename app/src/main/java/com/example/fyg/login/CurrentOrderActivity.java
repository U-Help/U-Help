package com.example.fyg.login;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.IOException;;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CurrentOrderActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);
        _CollectorActivity.addActivity(this);

        mListView = (ListView) findViewById(R.id.listview);
        /**
         * listview绑定adapter
         */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Proposer proposer=new Proposer();
                if(i==0||i==proposer.length+1){ ; }
                else{
                    if(i<=proposer.length) {
                        postReInfo();
                        startActivity(new Intent(CurrentOrderActivity.this, ProposalActivity.class));
                    }
                    else{
                        postGeInfo();
                    }
                }
            }
        });
    }

    private ArrayList<String> getData() {
        Proposer proposer=new Proposer();
        Accepter accepter=new Accepter();
        int i;
        //System.out.println(order.length);
        if(proposer.length==0&&accepter.length==0) {
            list.add("暂时无订单");
            return list;
        }
        if(proposer.length!=0&&accepter.length==0) {
            list.add("  收/取货    是否有人接单     接单人   收货时间");
            for (i = 0; i < proposer.length; i++) {
                list.add(i + 1 + ".收货    " + proposer.proposers[i].express_state + "     " + proposer.proposers[i].user + "     " + proposer.proposers[i].rece_time);
            }
            return list;
        }
        else if(proposer.length==0&&accepter.length!=0){
            list.add("  收/取货    收货人   收货时间   取货地点   收货地点");
            for (i = 0; i < accepter.length; i++) {
                list.add(i + 1 + ".取货    " + accepter.accepters[i].user + "     " + accepter.accepters[i].rece_time + "     " + accepter.accepters[i].srcplace+"     "+accepter.accepters[i].dstplace);
            }
            return list;
        }
        else{
            list.add("  收/取货    是否有人接单     接单人   收货时间");
            for (i = 0; i < proposer.length; i++) {
                list.add(i + 1 + ".收货    " + proposer.proposers[i].express_state + "     " + proposer.proposers[i].user + "     " + proposer.proposers[i].rece_time);
            }
            list.add("  收/取货    收货人   收货时间   取货地点   收货地点");
            for (i=0; i < accepter.length; i++) {
                list.add(i + proposer.length + ".取货    " + accepter.accepters[i].user + "     " + accepter.accepters[i].rece_time + "     " + accepter.accepters[i].srcplace+"     "+accepter.accepters[i].dstplace);
            }
            return list;
        }
    }

    public void postReInfo(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        Proposer proposer=new Proposer();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=proposer.item_id;
        String express_state=proposer.express_state;

        HashMap<String, String> map = new HashMap<>();
        map.put("express_state",express_state);
        map.put("item_id",item_id);
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/item/proposer_user2")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonStr = new JSONObject(str);

                        if (jsonStr.getString("state").equals("success")) {
                            flag = true;
                            Order order=new Order();
                            order.dstplace=jsonStr.getString("dstplace");
                            order.srcplace=jsonStr.getString("srcplace");
                            order.username=jsonStr.getString("username");
                            order.rece_time=jsonStr.getString("rece_time");
                            order.phone=jsonStr.getString("acc_phone");
                            order.price=jsonStr.getString("price");
                            order.size=jsonStr.getString("size");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    //Toast.makeText(GetActivity.this, "服务器未响应" + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    public void postGeInfo(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();

        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("offset","0");
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/item/hall")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int i;
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonObject = new JSONObject(str);
                        String state=jsonObject.getString("state");

                        if (state.equals("success")) {
                            flag=true;
                            Order order=new Order();
                            order.length=0;
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for(i = 0;i < jsonArray.length();i++){
                                Order s=new Order();
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                s.setDstplace(jsonObject1.getString("dstplace"));
                                //System.out.println(s.dstplace);
                                s.setIs_available(jsonObject1.getString("is_available"));
                                //System.out.println(s.is_available);
                                s.setItem_id(jsonObject1.getString("item_id"));
                                //System.out.println(s.item_id);
                                s.setPhone(jsonObject1.getString("phone"));
                                //System.out.println(s.phone);
                                s.setPrice(jsonObject1.getString("price"));
                                //System.out.println(s.price);
                                s.setProp_time(jsonObject1.getString("prop_time"));
                                //System.out.println(s.prop_time);
                                s.setRece_time(jsonObject1.getString("rece_time"));
                                //System.out.println(s.rece_time);
                                s.setRev_password(jsonObject1.getString("rev_password"));
                                //System.out.println(s.rev_password);
                                s.setSize(jsonObject1.getString("size"));
                                //System.out.println(s.size);
                                s.setSrcplace(jsonObject1.getString("srcplace"));
                                //System.out.println(s.srcplace);
                                s.setUsername(jsonObject1.getString("username"));
                                s.setMsg(jsonObject1.getString("msg"));
                                order.orders[i]=s;
                                order.length=i+1;
                            }
                            System.out.println(order.length);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        //Toast.makeText(GetActivity.this, "获取订单失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    //Toast.makeText(GetActivity.this, "服务器未响应" + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }
}
