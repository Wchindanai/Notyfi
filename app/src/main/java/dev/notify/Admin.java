package dev.notify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

public class Admin extends AppCompatActivity {
    private static final String TAG = "History";
    List<HistoryModel> listHistory;
    RecyclerView recyclerView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        recyclerView = (RecyclerView) findViewById(R.id.adRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listHistory = new ArrayList<>();
        getDataFromCloud();

    }

    private void getDataFromCloud() {
        String user = getUser();
        String url = "https://notify-166704.appspot.com/api/items";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Admin.this.runOnUiThread(new Runnable() {
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
                        String name = (String) jsonObject.get("name");
                        String created = (String) jsonObject.get("created");
                        String expire = (String) jsonObject.get("expire_date");
                        String member = (String) jsonObject.get("users_username");
                        int amount = (int) jsonObject.get("amount");
                        boolean isOut = (boolean) jsonObject.get("is_out");
                        String outDate = jsonObject.optString("out_date");
                        if (!isOut){
                            outDate = "-";
                        }
                        sendToObject(name, amount, expire, created, member, outDate);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Admin.this.runOnUiThread(new Runnable() {
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

    private void sendToObject(String itemName, int itemAmount, String itemExpire_date, String itemCreated, String itemMember, String outDate) {
        HistoryModel history = new HistoryModel(itemName, itemExpire_date, itemCreated, itemMember, itemAmount, outDate);
        listHistory.add(history);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                SharedPreferences sharedPreferences = getSharedPreferences("notify", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

