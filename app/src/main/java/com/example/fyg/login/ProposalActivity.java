package com.example.fyg.login;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProposalActivity extends AppCompatActivity {
    private Button btnQ;
    private Button btnD;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal);
        _CollectorActivity.addActivity(this);

        btnQ=findViewById(R.id.btnQ);
        btnD=findViewById(R.id.btnD);
        btnQ.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Succeed();
            }
        });
        btnD.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ;
            }
        });
        init();
    }

    @Override
    protected void onDestroy(){
        _CollectorActivity.removeActivity(this);
        super.onDestroy();
    }

    public void init(){
        Order order=new Order();
        String dstplace=order.dstplace;
        String srcplace=order.srcplace;
        String username=order.username;
        String rece_time=order.rece_time;
        String phone=order.phone;
        String price=order.price;
        String size=order.size;
        TextView text = (TextView) this.findViewById(R.id.textView3);
        String str = "接单者：" +username+"\n"+
                "接单者电话：" +phone+"\n"+
                "取货地点：" +srcplace+"\n"+
                "收货地点：" +dstplace+"\n"+
                "快件大小："+size+"\n"+
                "价格：" +price+"\n"+
                "收货时间：" +rece_time+"\n";
        text.setText(str);
    }

    public void Succeed(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        Proposer proposer=new Proposer();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;
        String item_id=proposer.item_id;

        HashMap<String, String> map = new HashMap<>();
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
                int i;
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonObject = new JSONObject(str);
                        String state=jsonObject.getString("state");

                        if (state.equals("success")) {
                            flag=true;
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
}
