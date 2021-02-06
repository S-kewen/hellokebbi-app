package com.example.hellokeb_bi.pattern.mediator;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class aNetwork extends Component {

    private JSONObject jsonObject;

    public aNetwork(Mediator m) {
        super(m);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void post(String url, String token, FormBody formBody) {
        /**建立連線*/
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
        /**設置傳送需求*/
        Request request = new Request.Builder()
                .addHeader("Authorization", token)
                .url(url)
                .post(formBody)
                .build();
        /**設置回傳*/
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                mediator.handle("network error!");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                jsonObject = JSONObject.parseObject(response.body().string());
                System.out.println(jsonObject.toJSONString());
                mediator.handle("network response");
            }
        });
    }
}
