package dev.notify;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class History extends AppCompatActivity {
    List<HistoryModel> listHistory;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.history_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listHistory = new ArrayList<>();
        getDataFromCloud();
    }

    private void getDataFromCloud() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://notify-163706.appspot.com/api/items")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet Conenction", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response.body().string());

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String itemName = (String) jsonObject.get("name");
                        int itemAmount = (int) jsonObject.get("amount");
                        String itemMember = (String) jsonObject.get("users_username");
                        String itemCreated = (String) jsonObject.get("created");
                        String itemExpire_date = (String) jsonObject.get("expire_date");
                        sendToObject(itemName, itemAmount, itemExpire_date, itemCreated, itemMember);
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

    private void sendToObject(String itemName, int itemAmount, String itemExpire_date, String itemCreated, String itemMember) {
        HistoryModel history = new HistoryModel(itemName, itemExpire_date, itemCreated, itemMember, itemAmount);
        listHistory.add(history);
    }
}
