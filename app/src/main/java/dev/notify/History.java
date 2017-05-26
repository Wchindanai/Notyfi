package dev.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static dev.notify.R.attr.layoutManager;

public class History extends AppCompatActivity {
    private static final String TAG = "History";
    List<HistoryModel> listHistory;
    RecyclerView recyclerView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        recyclerView = (RecyclerView) findViewById(R.id.history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                1);
        recyclerView.addItemDecoration(dividerItemDecoration);

        listHistory = new ArrayList<>();
        getDataFromCloud();

    }

    private void getDataFromCloud() {
        String user = getUser();
        String url = "https://notify-166704.appspot.com/api/items?filter={\"where\":{\"users_username\":\"" + user + "\"}}";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                History.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Please Check Your Internet Conenction", Toast.LENGTH_LONG).show();
                    }
                });

            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    for (int i = 0 ; i < jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int id = (int) jsonObject.get("id");
                        String name = (String) jsonObject.get("name");
                        String created = (String) jsonObject.get("created");
                        String expire = (String) jsonObject.get("expire_date");
                        String member = (String) jsonObject.get("users_username");
                        boolean isOut = (boolean) jsonObject.get("is_out");
                        String outDate = jsonObject.optString("out_date");
                        int amount = (int) jsonObject.get("amount");
                        if (!isOut){
                            outDate = "-";
                        }
                        sendToObject(id,name, amount, expire, created, member, outDate);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                History.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(new HistoryAdapter(listHistory, getApplicationContext()));
                    }
                });
            }
        });
    }

    private String getUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("notify", Context.MODE_PRIVATE);

        String user = sharedPreferences.getString("username", null);
        return user;
    }

    private void sendToObject(int id, String itemName, int itemAmount, String itemExpire_date, String itemCreated, String itemMember, String outDate) {
        HistoryModel history = new HistoryModel(id ,itemName, itemExpire_date, itemCreated, itemMember, itemAmount, outDate);
        listHistory.add(history);
    }
}
